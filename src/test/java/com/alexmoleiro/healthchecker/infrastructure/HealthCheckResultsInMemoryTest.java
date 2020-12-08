package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.TimedHealthCheckResponse;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

class HealthCheckResultsInMemoryTest {

  @Test
  void returnHealthCheckResults() throws MalformedURLException {
    final HealthCheckResponse healthCheckResponse =
        new HealthCheckResponse(new URL("https://www.a.com"), 200, Duration.ofMillis(123));
    final TimedHealthCheckResponse timedHealthCheckResponse =
        new TimedHealthCheckResponse("id", LocalDateTime.now(), healthCheckResponse);

    final HealthCheckResultsInMemory healthCheckResultsInMemory = new HealthCheckResultsInMemory();

    healthCheckResultsInMemory.add(timedHealthCheckResponse);

    final List<HealthCheckResponse> siteResults = healthCheckResultsInMemory.getSiteResults();

    assertThat(siteResults).isEqualTo(of(healthCheckResponse));
  }
}
