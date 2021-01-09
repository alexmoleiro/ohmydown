package com.alexmoleiro.healthchecker.core.healthCheck;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static java.util.stream.IntStream.rangeClosed;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;


class HealthCheckResponsesTest {

  public static final Endpoint ENDPOINT = new Endpoint(new HttpUrl("https://www.as.com"));

  @Test
  void shouldReturnUptime() {
    final LocalDateTime now = LocalDateTime.now();
    final HealthCheckResponses healthCheckResponses = new HealthCheckResponses(ENDPOINT);

    HealthCheckResponse response = new HealthCheckResponse(ENDPOINT.getHttpUrl(), FORBIDDEN.value(), now, now);
    healthCheckResponses.addLast(response);

    rangeClosed(1,287).forEach(i-> {
      final HealthCheckResponse response2 =
          new HealthCheckResponse(ENDPOINT.getHttpUrl(), OK.value(), now, now);
      healthCheckResponses.addLast(response2);
    });

    assertThat(healthCheckResponses.getUptime()).isEqualTo(99.65f);
  }

  @Test
  void shouldCalculateAverage() {

    final HealthCheckResponses responses = new HealthCheckResponses(ENDPOINT);
    final LocalDateTime now = LocalDateTime.now();

    final HealthCheckResponse response =
        new HealthCheckResponse(ENDPOINT.getHttpUrl(), OK.value(), now.minusSeconds(2), now);
    responses.addLast(response);

    rangeClosed(1,287).forEach(i-> {

      final HealthCheckResponse response2 =
          new HealthCheckResponse(ENDPOINT.getHttpUrl(), OK.value(), now.minusSeconds(1), now);
      responses.addLast(response2);
    });


    assertThat(responses.getAverage())
        .isEqualTo(1003.47);
  }
}