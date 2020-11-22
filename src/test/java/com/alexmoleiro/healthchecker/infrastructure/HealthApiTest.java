package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.service.SiteChecker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.stream.Stream;

import static com.alexmoleiro.healthchecker.infrastructure.SiteStatus.DOWN;
import static com.alexmoleiro.healthchecker.infrastructure.SiteStatus.UP;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class HealthApiTest {

  @LocalServerPort
  int port;

  @MockBean
  SiteChecker siteChecker;
  public static final int DELAY = new Random().nextInt();

  @ParameterizedTest
  @MethodSource("urls")
  void shouldReturnHttpStatus(String url, int statusCode, SiteStatus siteStatus)
      throws IOException, InterruptedException, URISyntaxException {

    final String domainName = url.substring(8);

    when(siteChecker.check(
        argThat(webRequest-> webRequest.getUrl().getHost().equals(domainName))))
        .thenReturn(new SiteCheckerResponse(siteStatus, DELAY, url));

    given()
        .contentType(JSON)
        .body("""
            {"url":"%s"}""".formatted(url))
        .post("http://localhost:%d/status".formatted(port))
        .then().assertThat().statusCode(statusCode).body(equalTo("""
        {"status":"%s","url":"%s","delay":%d}""".formatted(siteStatus.toString(), url, DELAY)))
        .body("url", equalTo(url));
  }

  private static Stream<Arguments> urls() {
    return Stream.of(
        of("https://www.down.com", 200, DOWN),
        of("https://www.up.com", 200, UP)
    );
  }
}