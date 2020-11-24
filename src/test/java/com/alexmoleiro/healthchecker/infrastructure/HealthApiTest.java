package com.alexmoleiro.healthchecker.infrastructure;


import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.service.SiteChecker;
import com.alexmoleiro.healthchecker.service.SiteCheckerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URISyntaxException;
import java.net.http.HttpConnectTimeoutException;
import java.util.Random;
import java.util.stream.Stream;

import static com.alexmoleiro.healthchecker.infrastructure.SiteStatus.DOWN;
import static com.alexmoleiro.healthchecker.infrastructure.SiteStatus.UP;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.REQUEST_TIMEOUT;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class HealthApiTest {

  @LocalServerPort
  int port;

  @MockBean
  SiteChecker siteChecker;
  public static final int DELAY = new Random().nextInt();

  @ParameterizedTest
  @MethodSource("urls")
  void shouldReturnHttpStatus(String url, int statusCode, SiteStatus siteStatus)
      throws InterruptedException, URISyntaxException, IOException {

    final String domainName = url.substring(8);

    when(siteChecker.check(
        argThat(webRequest-> webRequest.getUrl().getHost().equals(domainName))))
        .thenReturn(new SiteCheckerResponse(siteStatus, DELAY, url));

    given()
        .contentType(JSON)
        .body("""
            {"url":"%s"}""".formatted(url))
        .post("http://localhost:%d/status".formatted(port))
        .then().assertThat().statusCode(statusCode).body(equalTo("""
        {"status":"%s","url":"%s","delay":%d}""".formatted(siteStatus.toString(), url, DELAY)));
  }

  private static Stream<Arguments> urls() {
    return Stream.of(
        of("https://www.down.com", OK.value(), DOWN),
        of("https://www.up.com", OK.value(), UP)
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
        of("randomMessage","https://randomMessage","Invalid domain name"),
        of("ftps://hola","ftps://hola","unknown protocol: ftps")
    );
  }

  @Test
  void shouldReturn408WhenTimeout() throws URISyntaxException, InterruptedException, IOException {

    doThrow(new SiteCheckerException(new HttpConnectTimeoutException("timeout")))
        .when(siteChecker).check(any(WebStatusRequest.class));

      given()
          .contentType(JSON)
          .body("""
            {"url":"%s"}""".formatted("http://anything.com"))
          .post("http://localhost:%d/status".formatted(port))
          .then().assertThat().statusCode(REQUEST_TIMEOUT.value());
    }

  @Test
  void shouldReturnUnknownWhenUnresolvedAddressException()
      throws URISyntaxException, InterruptedException, IOException {

    doThrow(new ConnectException())
        .when(siteChecker).check(any(WebStatusRequest.class));

    given()
        .contentType(JSON)
        .body("""
            {"url":"%s"}""".formatted("http://anything.com"))
        .post("http://localhost:%d/status".formatted(port))
        .then().assertThat().statusCode(NOT_FOUND.value());
  }

  @Test
  void shouldReturnUnknownWhenCertificateExpiredExceptionException()
      throws URISyntaxException, InterruptedException, IOException {

    doThrow(new SSLHandshakeException(""))
        .when(siteChecker).check(any(WebStatusRequest.class));

    given()
        .contentType(JSON)
        .body("""
            {"url":"%s"}""".formatted("http://anything.com"))
        .post("http://localhost:%d/status".formatted(port))
        .then().assertThat().statusCode(495);
  }

}