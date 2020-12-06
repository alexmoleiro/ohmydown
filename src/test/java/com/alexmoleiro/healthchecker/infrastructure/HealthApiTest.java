package com.alexmoleiro.healthchecker.infrastructure;


import com.alexmoleiro.healthchecker.service.HealthChecker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class HealthApiTest {

  @LocalServerPort
  int port;

  @MockBean
  HealthChecker healthChecker;

  @MockBean
  HttpClient httpClient;

  public static final int DELAY = new Random().nextInt();

  @ParameterizedTest
  @MethodSource("urls")
  void shouldReturnHttpStatus(String url, int serverStatusCode) {

    final String domainName = url.substring(8);

    when(healthChecker.check(
        argThat(webRequest-> webRequest.getUrl().getHost().equals(domainName))))
        .thenReturn(new SiteCheckerResponse(url, serverStatusCode, DELAY));

    given()
        .contentType(JSON)
        .body("""
            {"url":"%s"}""".formatted(url))
        .post("http://localhost:%d/status".formatted(port))
        .then().assertThat().statusCode(200).body(equalTo("""
        {"url":"%s","delay":%d,"status":%d}""".formatted(url, DELAY, serverStatusCode)));
  }

  private static Stream<Arguments> urls() {
    return Stream.of(
        of("https://www.down.com", BAD_REQUEST.value()),
        of("https://www.up.com", OK.value())
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
  void shouldReturnProperStatusCode(int statusCode, Throwable e)
      throws InterruptedException, IOException {

    doThrow(e).when(httpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));

    given()
        .contentType(JSON)
        .body("""
            {"url":"%s"}""".formatted("http://anything.com"))
        .post("http://localhost:%d/status".formatted(port))
        .then().assertThat().statusCode(statusCode);
  }

  private static Stream<Arguments> cases() {
    return Stream.of(
        of(200, new SSLHandshakeException("")),
        of(200, new ConnectException()),
        of(200, new IOException()),
        of(200, new InterruptedException()),
        of(200, new HttpConnectTimeoutException("timeout"))
    );
  }

}