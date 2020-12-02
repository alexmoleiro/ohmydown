package com.alexmoleiro.healthchecker.infrastructure;


import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.service.HttpChecker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;

import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static com.alexmoleiro.healthchecker.core.CheckResultCode.SSL_CERTIFICATE_ERROR;
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
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.REQUEST_TIMEOUT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class HealthApiTest {

  @LocalServerPort
  int port;

  @MockBean
  HttpChecker httpChecker;
  public static final int DELAY = new Random().nextInt();

  @ParameterizedTest
  @MethodSource("urls")
  void shouldReturnHttpStatus(String url, int serverStatusCode)
      throws InterruptedException, URISyntaxException, IOException {

    final String domainName = url.substring(8);

    when(httpChecker.check(
        argThat(webRequest-> webRequest.getUrl().getHost().equals(domainName))))
        .thenReturn(new SiteCheckerResponse(new HttpResponse<>() {
          @Override
          public int statusCode() {
            return serverStatusCode;
          }

          @Override
          public HttpRequest request() {
            return null;
          }

          @Override
          public Optional<HttpResponse<Void>> previousResponse() {
            return Optional.empty();
          }

          @Override
          public HttpHeaders headers() {
            return null;
          }

          @Override
          public Void body() {
            return null;
          }

          @Override
          public Optional<SSLSession> sslSession() {
            return Optional.empty();
          }

          @Override
          public URI uri() {
            return URI.create(url);
          }

          @Override
          public HttpClient.Version version() {
            return null;
          }
        }, DELAY));

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
      throws URISyntaxException, InterruptedException, IOException {

    doThrow(e)
        .when(httpChecker).check(any(WebStatusRequest.class));

    given()
        .contentType(JSON)
        .body("""
            {"url":"%s"}""".formatted("http://anything.com"))
        .post("http://localhost:%d/status".formatted(port))
        .then().assertThat().statusCode(statusCode);
  }

  private static Stream<Arguments> cases() {
    return Stream.of(
        of(SSL_CERTIFICATE_ERROR.value(), new SSLHandshakeException("")),
        of(NOT_FOUND.value(), new ConnectException()),
        of(REQUEST_TIMEOUT.value(), new HttpConnectTimeoutException("timeout"))
    );
  }

}