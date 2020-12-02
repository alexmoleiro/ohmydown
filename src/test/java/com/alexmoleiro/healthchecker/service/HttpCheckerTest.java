package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.infrastructure.SiteCheckerResponse;
import com.alexmoleiro.healthchecker.infrastructure.WebStatusRequestDto;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpTimeoutException;
import java.util.Optional;
import java.util.Random;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 200 OK 400-499 ISSUE => 403 forbidden, 404 me equivoco url, 429 too much requests, 500 down =>
 * imposible timeout (qué hacemos: retry o aumentar el timeout) falsos negativos
 */
class HttpCheckerTest {

  public static final String URL = "www.alexmoleiro.com";

  @Test
  void shouldReturnStatusCode() throws InterruptedException, IOException {

    final HttpClient mock = mock(HttpClient.class);
    final int statusCode = new Random().nextInt();

    when(mock.send(any(HttpRequest.class), any(BodyHandler.class)))
        .thenReturn(getHttpResponse(statusCode, URL));
    final WebStatusRequest webStatusRequest = new WebStatusRequest(new WebStatusRequestDto(URL));

    final SiteCheckerResponse check = new HttpChecker(mock, ofSeconds(5)).check(webStatusRequest);

    assertThat(check.getStatus()).isEqualTo(statusCode);
  }

  @Test
  void shouldReturnTimeout() throws IOException, InterruptedException {
    final HttpClient mock = mock(HttpClient.class);
    doThrow(new HttpTimeoutException("timeout"))
        .when(mock)
        .send(any(HttpRequest.class), any(BodyHandler.class));

    final WebStatusRequest webStatusRequest = new WebStatusRequest(new WebStatusRequestDto(URL));
    final SiteCheckerResponse check = new HttpChecker(mock, ofSeconds(5)).check(webStatusRequest);

    assertThat(check.getStatus()).isEqualTo(504);
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
