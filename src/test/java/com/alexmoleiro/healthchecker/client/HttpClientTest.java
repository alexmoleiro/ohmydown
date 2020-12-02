package com.alexmoleiro.healthchecker.client;

import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.infrastructure.WebStatusRequestDto;
import com.alexmoleiro.healthchecker.service.HttpChecker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URISyntaxException;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureWireMock(port = 8765)
public class HttpClientTest {

  private HttpClient client = newBuilder().build();

  @Test
  void shouldCallSite() throws InterruptedException, URISyntaxException, IOException {

    stubFor(get(urlEqualTo("/log")).willReturn(aResponse().withStatus(200)));

    final WebStatusRequestDto webStatusRequestDtoMock = mock(WebStatusRequestDto.class);
    when(webStatusRequestDtoMock.getUrl()).thenReturn("http://localhost:8765/log");

    new HttpChecker(client, ofSeconds(2)).check(new WebStatusRequest(webStatusRequestDtoMock));

    verify(getRequestedFor(urlMatching("/log")));
  }

}
