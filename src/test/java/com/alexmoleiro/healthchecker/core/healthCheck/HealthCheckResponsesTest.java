package com.alexmoleiro.healthchecker.core.healthCheck;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;


class HealthCheckResponsesTest {

  @Test
  void shouldReturnUptime() {
    final Endpoint endpoint = new Endpoint(new HttpUrl("https://www.as.com"));
    final LocalDateTime now = LocalDateTime.now();
    final HealthCheckResponses healthCheckResponses = new HealthCheckResponses(endpoint);

    HealthCheckResponse response = new HealthCheckResponse(endpoint.getHttpUrl(), FORBIDDEN.value(), now, now);
    healthCheckResponses.addLast(response);

    IntStream.rangeClosed(1,287).forEach(i-> {
      final HealthCheckResponse response2 =
          new HealthCheckResponse(endpoint.getHttpUrl(), OK.value(), now, now);
      healthCheckResponses.addLast(response2);
    });


    assertThat(healthCheckResponses.getUptime()).isEqualTo(99.65f);
  }
}