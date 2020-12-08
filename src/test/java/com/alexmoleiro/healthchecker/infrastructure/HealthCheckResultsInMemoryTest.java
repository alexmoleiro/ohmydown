package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.Id;
import com.alexmoleiro.healthchecker.core.TimedHealthCheckResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static java.time.Duration.ofMillis;
import static java.time.LocalDateTime.now;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

class HealthCheckResultsInMemoryTest {

  @Test
  void returnHealthCheckResults() throws MalformedURLException {
    final HealthCheckResponse healthCheckResponse =
        new HealthCheckResponse(new URL("https://www.a.com"), HttpStatus.OK.value(), ofMillis(123));

    final TimedHealthCheckResponse timedHealthCheckResponse =
        new TimedHealthCheckResponse(new Id("id"), now(), healthCheckResponse);

    final HealthCheckResultsInMemory healthCheckResultsInMemory = new HealthCheckResultsInMemory();

    healthCheckResultsInMemory.add(timedHealthCheckResponse);

    final List<TimedHealthCheckResponse> siteResults = healthCheckResultsInMemory.getSiteResults();

    assertThat(siteResults).usingRecursiveComparison().isEqualTo(of(timedHealthCheckResponse));
  }

  @Test
  void shouldReturnOnlyOneResults() throws MalformedURLException {
    final String anid = "id";

    final HealthCheckResponse healthCheckResponse =
        new HealthCheckResponse(new URL("https://www.a.com"), 200, ofMillis(123));

    final TimedHealthCheckResponse timedHealthCheckResponse =
        new TimedHealthCheckResponse(new Id(anid), now(), healthCheckResponse);


    final TimedHealthCheckResponse timedHealthCheckResponse2 =
        new TimedHealthCheckResponse(new Id(anid), now(), healthCheckResponse);

    final HealthCheckResultsInMemory healthCheckResultsInMemory = new HealthCheckResultsInMemory();

    healthCheckResultsInMemory.add(timedHealthCheckResponse);
    healthCheckResultsInMemory.add(timedHealthCheckResponse2);

    final List<TimedHealthCheckResponse> siteResults = healthCheckResultsInMemory.getSiteResults();

    assertThat(siteResults.size()).isEqualTo(1);

  }
}
