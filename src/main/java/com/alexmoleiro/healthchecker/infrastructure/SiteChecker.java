package com.alexmoleiro.healthchecker.infrastructure;

import java.io.IOException;
import java.net.URI;
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

  public SiteCheckerResponse check(URI uri) throws IOException, InterruptedException {
    final HttpRequest request = newBuilder().GET().uri(uri).build();
    final LocalDateTime now = now();
    final HttpResponse<String> send = client.send(request, ofString());
    final long delay = between(now, now()).toMillis();
    String status = (send.statusCode() == 200) ? "UP" : "DOWN";
    return new SiteCheckerResponse(status, 200, uri.toString());
  }
}
