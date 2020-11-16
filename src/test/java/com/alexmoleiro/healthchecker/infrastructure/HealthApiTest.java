package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.service.SiteChecker;
import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import static com.alexmoleiro.healthchecker.infrastructure.SiteStatus.DOWN;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class HealthApiTest {

  @LocalServerPort
  int port;

  @MockBean
  SiteChecker siteChecker;

  @ParameterizedTest
  @MethodSource("urls")
  void shouldReturnHttpStatus(String url, String status) throws IOException, InterruptedException, URISyntaxException {

    when(siteChecker.check(any(WebStatusRequest.class)))
        .thenReturn(new SiteCheckerResponse(DOWN, 200, "https://www.down.com"));

    given()
        .contentType(JSON)
        .body("""
            {"url":"%s"}""".formatted(url))
        .post("http://localhost:%d/status".formatted(port))
        .then().assertThat().statusCode(200).body(equalTo("""
        {"status":"%s","url":"%s","delay":200}""".formatted(status, url)))
        .body("url", equalTo(url));
  }

  private static Stream<Arguments> urls() {
    return Stream.of(
        of("https://www.down.com", "DOWN")
    );
  }
}