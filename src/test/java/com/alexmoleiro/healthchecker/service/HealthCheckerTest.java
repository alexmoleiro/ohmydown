package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.infrastructure.SiteCheckerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpTimeoutException;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static com.alexmoleiro.healthchecker.core.CheckResultCode.SERVER_TIMEOUT;
import static com.alexmoleiro.healthchecker.core.CheckResultCode.SSL_CERTIFICATE_ERROR;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HealthCheckerTest {

  public static final String URL = "www.alexmoleiro.com";

  @Test
  void shouldReturnStatusCode() throws InterruptedException, IOException {

    final HttpClient mock = mock(HttpClient.class);
    final int statusCode = new Random().nextInt();

    when(mock.send(any(HttpRequest.class), any(BodyHandler.class)))
        .thenReturn(getHttpResponse(statusCode, URL));

    final SiteCheckerResponse check = new HealthChecker(mock, ofSeconds(5)).check(new WebStatusRequest(URL));

    assertThat(check.getStatus()).isEqualTo(statusCode);
  }

  @ParameterizedTest
  @MethodSource("exceptions")
  void shouldReturnTimeout(Throwable e, int statusCode) throws IOException, InterruptedException {
    final HttpClient mock = mock(HttpClient.class);
    doThrow(e).when(mock).send(any(HttpRequest.class), any(BodyHandler.class));

    final SiteCheckerResponse check = new HealthChecker(mock, ofSeconds(5)).check(new WebStatusRequest(URL));

    assertThat(check.getStatus()).isEqualTo(statusCode);
  }

  private static Stream<Arguments> exceptions() {
    return Stream.of(
        of(new HttpTimeoutException("timeout"), SERVER_TIMEOUT.value()),
        of(new ConnectException(), HttpStatus.SERVICE_UNAVAILABLE.value()),
        of(new SSLHandshakeException("reason"), SSL_CERTIFICATE_ERROR.value()),
        of(new IOException(), HttpStatus.SERVICE_UNAVAILABLE.value() )
    );
  }

  private HttpResponse getHttpResponse(final int statusCode, final String url) {
    return new HttpResponse() {
      @Override
      public int statusCode() {
        return statusCode;
      }

      @Override
      public HttpRequest request() {
        return null;
      }

      @Override
      public Optional<HttpResponse> previousResponse() {
        return Optional.empty();
      }

      @Override
      public HttpHeaders headers() {
        return null;
      }

      @Override
      public Object body() {
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
    };
  }
}
