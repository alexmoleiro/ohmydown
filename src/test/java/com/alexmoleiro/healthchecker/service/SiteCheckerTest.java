package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.infrastructure.SiteCheckerResponse;
import com.alexmoleiro.healthchecker.infrastructure.WebStatusRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shaded.org.apache.maven.model.Site;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.time.Duration;
import java.util.Optional;

import static java.net.http.HttpClient.Redirect.ALWAYS;
import static java.net.http.HttpClient.newBuilder;
import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 *  200 OK
 *  400-499 ISSUE => 403 forbidden, 404 me equivoco url, 429 too much requests,
 *  500
 * down => imposible
 * timeout (qué hacemos: retry o aumentar el timeout) falsos negativos
 */
class SiteCheckerTest {

  @Disabled
  void shouldRetur500() throws InterruptedException, IOException, URISyntaxException {
    //final HttpClient httpClient = newBuilder().followRedirects(ALWAYS).build();
    final HttpClient mock = Mockito.mock(HttpClient.class);
    when(mock.send(any(HttpRequest.class), any(BodyHandler.class))).thenReturn(
        new HttpResponse() {
          @Override
          public int statusCode() {
            return 400;
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
            URI uri=null;
            try {
              uri= new URI("www.alexmoleiro.com");
            } catch (URISyntaxException e) {
              e.printStackTrace();
            }
            return uri;
          }

          @Override
          public HttpClient.Version version() {
            return null;
          }
        });
    final WebStatusRequest webStatusRequest = new WebStatusRequest(new WebStatusRequestDto("www.alexmoleiro.com"));

    final SiteCheckerResponse check = new SiteChecker(mock, ofSeconds(5)).check(webStatusRequest);

    Assertions.assertThat(check.getStatus()).isEqualTo(400);

  }
}
