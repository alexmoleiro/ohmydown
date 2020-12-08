package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.Id;
import com.alexmoleiro.healthchecker.core.TimedHealthCheckResponses;
import com.alexmoleiro.healthchecker.infrastructure.repositories.HealthCheckResultsInMemory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.Duration.ofMillis;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

class HealthCheckResultsInMemoryTest {

  @Test
  void returnHealthCheckResults() throws MalformedURLException {
    final HealthCheckResponse healthCheckResponse =
        new HealthCheckResponse(new URL("https://www.a.com"), HttpStatus.OK.value(), ofMillis(123), LocalDateTime.now());

    final TimedHealthCheckResponses timedHealthCheckResponses =
        new TimedHealthCheckResponses(new Id("id"), healthCheckResponse);

    final HealthCheckResultsInMemory healthCheckResultsInMemory = new HealthCheckResultsInMemory();

    healthCheckResultsInMemory.add(timedHealthCheckResponses);

    final List<TimedHealthCheckResponses> siteResults = healthCheckResultsInMemory.getSiteResults();

    assertThat(siteResults).usingRecursiveComparison().isEqualTo(of(timedHealthCheckResponses));
  }

  @Test
  void shouldReturnOnlyOneResults() throws MalformedURLException {
    final String anid = "id";

    final HealthCheckResponse healthCheckResponse =
        new HealthCheckResponse(new URL("https://www.a.com"), 200, ofMillis(123), LocalDateTime.now());

    final TimedHealthCheckResponses timedHealthCheckResponses =
        new TimedHealthCheckResponses(new Id(anid), healthCheckResponse);


    final TimedHealthCheckResponses timedHealthCheckResponses2 =
        new TimedHealthCheckResponses(new Id(anid), healthCheckResponse);


    final TimedHealthCheckResponses timedHealthCheckResponses3 =
        new TimedHealthCheckResponses(new Id(anid), healthCheckResponse);

    final HealthCheckResultsInMemory healthCheckResultsInMemory = new HealthCheckResultsInMemory();

    healthCheckResultsInMemory.add(timedHealthCheckResponses);
    healthCheckResultsInMemory.add(timedHealthCheckResponses2);
    healthCheckResultsInMemory.add(timedHealthCheckResponses3);

    final List<TimedHealthCheckResponses> siteResults = healthCheckResultsInMemory.getSiteResults();

    assertThat(siteResults.size()).isEqualTo(1);
    assertThat(siteResults.get(0).getHealthCheckResponse().size()).isEqualTo(3);

  }
}
