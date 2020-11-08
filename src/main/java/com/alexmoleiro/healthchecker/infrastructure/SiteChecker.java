package com.alexmoleiro.healthchecker.infrastructure;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

import static java.net.http.HttpRequest.newBuilder;

public class SiteChecker {

  private final HttpClient client;

  public SiteChecker(HttpClient client) {
    this.client = client;
  }

  public void check(URI uri) throws IOException, InterruptedException {
    final HttpRequest request = newBuilder().GET().uri(uri).build();
    client.send(request, BodyHandlers.ofString());
  }
}
