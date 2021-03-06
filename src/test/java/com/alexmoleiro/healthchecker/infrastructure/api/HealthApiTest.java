package com.alexmoleiro.healthchecker.infrastructure.api;


import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthChecker;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.time.LocalDateTime.of;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
class HealthApiTest {

  @LocalServerPort
  int port;

  @MockBean
  HealthChecker healthChecker;

  @MockBean
  HttpClient httpClient;

  @ParameterizedTest
  @MethodSource("urls")
  void shouldReturnHttpStatus(HttpUrl url, HttpStatus serverStatusCode) {

    LocalDateTime before = of(2020, 12, 9, 10, 11, 2);
    LocalDateTime now = of(2020, 12, 10, 10, 11, 2);

    when(healthChecker.check(
        argThat(webRequest-> webRequest.getUrl().toString().equals(url.toString()))))
        .thenReturn(new HealthCheckResponse(url, serverStatusCode.value(), before, now));

    given()
        .contentType(JSON)
        .body("""
            {"url":"%s"}""".formatted(url))
        .post("http://localhost:%d/status".formatted(port))
        .then().assertThat().statusCode(200).body(equalTo("""
        {"status":%d,"url":"%s","delay":%d}""".formatted(serverStatusCode.value(),url, 86400000)));
  }

  private static Stream<Arguments> urls() {
    return Stream.of(
        of(new HttpUrl("https://www.down.com"), BAD_REQUEST),
        of(new HttpUrl("https://www.up.com"), OK)
    );
  }

  @ParameterizedTest
  @MethodSource("invalidUrls")
  void shouldReturnBadRequest(String invalidUrl, String expectedDomain, String errorMessage) {
    given()
        .contentType(JSON)
        .body("""
            {"url":"%s"}""".formatted(invalidUrl))
        .post("http://localhost:%d/status".formatted(port))
        .then().assertThat().statusCode(BAD_REQUEST.value())
        .body(equalTo("""
        {"url":"%s","message":"%s"}""".formatted(expectedDomain, errorMessage)));
    }

  private static Stream<Arguments> invalidUrls() {
    return Stream.of(
        of("randomMessage","http://randommessage","Invalid domain name"),
        of("ftps://hola","ftps://hola","unknown protocol: ftps")
    );
  }

  @ParameterizedTest
  @MethodSource("cases")
  void shouldReturnProperStatusCode(HttpStatus statusCode, Throwable e)
      throws InterruptedException, IOException {

    doThrow(e).when(httpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));

    given()
        .contentType(JSON)
        .body("""
            {"url":"%s"}""".formatted("http://anything.com"))
        .post("http://localhost:%d/status".formatted(port))
        .then().assertThat().statusCode(statusCode.value());
  }

  private static Stream<Arguments> cases() {
    return Stream.of(
        of(OK, new SSLHandshakeException("")),
        of(OK, new ConnectException()),
        of(OK, new IOException()),
        of(OK, new InterruptedException()),
        of(OK, new HttpConnectTimeoutException("timeout"))
    );
  }

}