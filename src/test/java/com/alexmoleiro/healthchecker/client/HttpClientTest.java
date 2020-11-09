package com.alexmoleiro.healthchecker.client;

import com.alexmoleiro.healthchecker.infrastructure.SiteChecker;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static java.net.URI.create;

public class HttpClientTest {

  private WireMockServer wireMockServer = new WireMockServer(8080);
  private HttpClient client = HttpClient.newBuilder().build();
  ;

  @BeforeEach
  void setUp() {
    wireMockServer.start();
  }

  @AfterEach
  void tearDown() {
    wireMockServer.stop();
  }

  @Test
  void shouldCallSite() throws IOException, InterruptedException {

    stubFor(WireMock.get(urlEqualTo("/log")));

    new SiteChecker(client).check(create("http://localhost:8080/log"));

    verify(getRequestedFor(urlMatching("/log")));
  }
}
