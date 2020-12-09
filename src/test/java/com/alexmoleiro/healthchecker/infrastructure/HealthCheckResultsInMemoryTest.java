package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.Id;
import com.alexmoleiro.healthchecker.core.TimedHealthCheckResponses;
import com.alexmoleiro.healthchecker.infrastructure.repositories.HealthCheckResultsInMemory;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static java.time.Duration.ofMillis;
import static java.time.LocalDateTime.now;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

class HealthCheckResultsInMemoryTest {

  private static final Id ID = new Id("id");
  private static final String A_URL = "https://www.a.com";
  private static final int MILLIS = 123;

  @Test
  void returnHealthCheckResults() throws MalformedURLException {
    final HealthCheckResponse healthCheckResponse =
        new HealthCheckResponse(new URL(A_URL), OK.value(), ofMillis(MILLIS), now());

    final TimedHealthCheckResponses timedHealthCheckResponses =
        new TimedHealthCheckResponses(ID, healthCheckResponse);

    final HealthCheckResultsInMemory healthCheckResultsInMemory = new HealthCheckResultsInMemory();

    healthCheckResultsInMemory.add(ID, healthCheckResponse);

    final List<TimedHealthCheckResponses> siteResults = healthCheckResultsInMemory.getTimedResults();

    assertThat(siteResults).usingRecursiveComparison().isEqualTo(of(timedHealthCheckResponses));
  }

  @Test
  void shouldReturnOnlyOneResults() throws MalformedURLException {

    final HealthCheckResponse healthCheckResponse =
        new HealthCheckResponse(new URL(A_URL), OK.value(), ofMillis(MILLIS), now());

    final HealthCheckResultsInMemory healthCheckResultsInMemory = new HealthCheckResultsInMemory();

    healthCheckResultsInMemory.add(ID, healthCheckResponse);
    healthCheckResultsInMemory.add(ID, healthCheckResponse);
    healthCheckResultsInMemory.add(ID, healthCheckResponse);

    final List<TimedHealthCheckResponses> timedResults = healthCheckResultsInMemory.getTimedResults();

    assertThat(timedResults.size()).isEqualTo(1);
    assertThat(timedResults.get(0).getHealthCheckResponse().size()).isEqualTo(3);
  }
}
