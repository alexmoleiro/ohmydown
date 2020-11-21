package com.alexmoleiro.healthchecker.client;

import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.infrastructure.WebStatusRequestDto;
import com.alexmoleiro.healthchecker.service.SiteChecker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static java.net.http.HttpClient.newBuilder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureWireMock(port = 8765)
public class HttpClientTest {

  private HttpClient client = newBuilder().build();

  @Test
  void shouldCallSite() throws IOException, InterruptedException, URISyntaxException {

    stubFor(get(urlEqualTo("/log")));

    final WebStatusRequestDto mock = mock(WebStatusRequestDto.class);
    when(mock.getUrl()).thenReturn("http://localhost:8765/log");

    new SiteChecker(client).check(new WebStatusRequest(mock));

    verify(getRequestedFor(urlMatching("/log")));
  }
}
