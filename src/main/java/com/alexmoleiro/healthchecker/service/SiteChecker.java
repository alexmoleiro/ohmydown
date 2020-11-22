package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.infrastructure.SiteCheckerResponse;
import com.alexmoleiro.healthchecker.infrastructure.SiteStatus;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static com.alexmoleiro.healthchecker.infrastructure.SiteStatus.DOWN;
import static com.alexmoleiro.healthchecker.infrastructure.SiteStatus.UP;
import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.time.Duration.between;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

public class SiteChecker {

  private final HttpClient client;

  public SiteChecker(HttpClient client) {
    this.client = client;
  }

  public SiteCheckerResponse check(WebStatusRequest webStatusRequest)
      throws IOException, InterruptedException, URISyntaxException {
    final HttpRequest request = newBuilder().GET().uri(webStatusRequest.getUrl().toURI()).build();
    final LocalDateTime beforeRequest = now();
    final HttpResponse<String> send = client.send(request, ofString());
    final long delay = between(beforeRequest, now()).toMillis();
    SiteStatus status = (send.statusCode() == OK.value()) ? UP : DOWN;
    return new SiteCheckerResponse(status, delay, send.uri().toString());
  }
}
