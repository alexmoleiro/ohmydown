package com.alexmoleiro.healthchecker.core.healthCheck;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;


class HealthCheckResponsesTest {

  @Test
  void shouldReturnUptime() {
    final Endpoint endpoint = new Endpoint(new HttpUrl("https://www.as.com"));
    final LocalDateTime now = LocalDateTime.now();
    final HealthCheckResponse response = new HealthCheckResponse(endpoint.getHttpUrl(), HttpStatus.OK.value(), now, now);
    final HealthCheckResponse response2 = new HealthCheckResponse(endpoint.getHttpUrl(), HttpStatus.FORBIDDEN.value(), now, now);

    final HealthCheckResponses healthCheckResponses = new HealthCheckResponses(endpoint, response);
    healthCheckResponses.addLast(response2);

    Assertions.assertThat(healthCheckResponses.getUptime()).isEqualTo(BigDecimal.valueOf(50.00));
  }
}