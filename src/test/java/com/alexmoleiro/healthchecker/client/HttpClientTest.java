package com.alexmoleiro.healthchecker.client;

import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.service.HealthChecker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.http.HttpClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static java.net.http.HttpClient.newBuilder;
import static java.time.Duration.ofSeconds;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(SpringExtension.class)
@AutoConfigureWireMock(port = 8765)
public class HttpClientTest {

  private HttpClient client = newBuilder().build();

  @Test
  void shouldCallSite() {

    stubFor(get(urlEqualTo("/log")).willReturn(aResponse().withStatus(OK.value())));

    new HealthChecker(client, ofSeconds(2)).check(new WebStatusRequest("http://localhost:8765/log"));

    verify(getRequestedFor(urlMatching("/log")));
  }

}
