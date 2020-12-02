package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.infrastructure.SiteCheckerResponse;
import org.slf4j.Logger;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.alexmoleiro.healthchecker.core.CheckResultCode.SERVER_TIMEOUT;
import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpResponse.BodyHandlers.discarding;
import static java.time.Duration.between;
import static java.time.LocalDateTime.now;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpHeaders.USER_AGENT;

public class HttpChecker {

  public static final String MOZILLA =
      "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";
  private final HttpClient client;
  private final Duration timeout;
  private static Logger logger = getLogger(HttpChecker.class);

  public HttpChecker(HttpClient client, Duration timeout) {
    this.client = client;
    this.timeout = timeout;
  }

  public SiteCheckerResponse check(WebStatusRequest webStatusRequest)
      throws InterruptedException, IOException {

    SiteCheckerResponse siteCheckerResponse;
    final LocalDateTime beforeRequest = now();
    try {
      siteCheckerResponse = httpClient(webStatusRequest, beforeRequest);
    } catch (HttpTimeoutException e) {
      return new SiteCheckerResponse(
          getResponse(webStatusRequest, SERVER_TIMEOUT.value()),
          between(beforeRequest, now()).toMillis());
    }
    return siteCheckerResponse;
  }

  private SiteCheckerResponse httpClient(
      WebStatusRequest webStatusRequest, LocalDateTime beforeRequest)
      throws IOException, InterruptedException {

    return new SiteCheckerResponse(
        client.send(
            newBuilder()
                .GET()
                .uri(URI.create(webStatusRequest.getUrl().toString()))
                .setHeader(USER_AGENT, MOZILLA)
                .timeout(timeout)
                .build(),
            discarding()),
        between(beforeRequest, now()).toMillis());
  }

  private HttpResponse<Void> getResponse(WebStatusRequest webStatusRequest, final int statusCode) {
    return new HttpResponse<Void>() {
      @Override
      public int statusCode() {
        return statusCode;
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
        return URI.create(webStatusRequest.getUrl().toString());
      }

      @Override
      public HttpClient.Version version() {
        return null;
      }
    };
  }
}
