package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.service.WebStatusRequest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.time.Duration.between;
import static java.time.LocalDateTime.now;

public class SiteChecker {

  private final HttpClient client;

  public SiteChecker(HttpClient client) {
    this.client = client;
  }

  public SiteCheckerResponse check(WebStatusRequest webStatusRequest)
      throws IOException, InterruptedException, URISyntaxException {
    final HttpRequest request = newBuilder().GET().uri(webStatusRequest.getUrl().toURI()).build();
    final LocalDateTime now = now();
    final HttpResponse<String> send = client.send(request, ofString());
    final long delay = between(now, now()).toMillis();
    String status = (send.statusCode() == 200) ? "UP" : "DOWN";
    return new SiteCheckerResponse(status, delay, webStatusRequest.getUrl().toString());
  }
}
