package com.alexmoleiro.healthchecker.infrastructure;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.params.provider.Arguments.of;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class HealthApiTest {

  @LocalServerPort
  int port;

  @ParameterizedTest
  @MethodSource("urls")
  void shouldReturnHttpStatus(String url, String status) {
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
        of("http://www.up.com", "UP"),
        of("http://www.down.com", "DOWN")
    );
  }
}