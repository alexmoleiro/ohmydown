package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.infrastructure.SiteCheckerResponse;
import com.alexmoleiro.healthchecker.infrastructure.SiteStatus;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static com.alexmoleiro.healthchecker.infrastructure.SiteStatus.DOWN;
import static com.alexmoleiro.healthchecker.infrastructure.SiteStatus.UP;
import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpResponse.BodyHandlers.discarding;
import static java.time.Duration.between;
import static java.time.LocalDateTime.now;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.OK;

public class SiteChecker {

  private final HttpClient client;
  private final Duration timeout;
  private static Logger logger = getLogger(SiteChecker.class);

  public SiteChecker(HttpClient client, Duration timeout) {
    this.client = client;
    this.timeout = timeout;
  }

  public SiteCheckerResponse check(WebStatusRequest webStatusRequest)
      throws URISyntaxException, InterruptedException, IOException {
    final HttpRequest request =
        newBuilder()
            .GET()
            .uri(webStatusRequest.getUrl().toURI())
            .timeout(timeout)
            .build();
    final LocalDateTime beforeRequest = now();
    HttpResponse<Void> send = client.send(request, discarding());
    final long delay = between(beforeRequest, now()).toMillis();
    SiteStatus status = (send.statusCode() == OK.value()) ? UP : DOWN;
    return new SiteCheckerResponse(status, delay, send.uri().toString());
  }
}