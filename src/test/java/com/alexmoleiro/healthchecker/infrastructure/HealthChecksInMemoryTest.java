package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.Id;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponses;
import com.alexmoleiro.healthchecker.infrastructure.repositories.HealthChecksInMemory;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import static java.time.LocalDateTime.now;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

class HealthChecksInMemoryTest {

  private static final Id ID = new Id("id");
  private static final String A_URL = "https://www.a.com";
  private HealthCheckRepository healthCheckResultsInMemory = new HealthChecksInMemory();

  @Test
  void returnHealthCheckResults() throws MalformedURLException {
    final HealthCheckResponse healthCheckResponse =
        new HealthCheckResponse(new URL(A_URL), OK.value(), now(), now());

    final HealthCheckResponses healthCheckResponses =
        new HealthCheckResponses(ID, healthCheckResponse);

    healthCheckResultsInMemory.add(ID, healthCheckResponse);

    final List<HealthCheckResponses> siteResults = healthCheckResultsInMemory.getResponses();

    assertThat(siteResults).usingRecursiveComparison().isEqualTo(of(healthCheckResponses));
  }

  @Test
  void shouldReturnOnlyOneResults() throws MalformedURLException {

    final HealthCheckResponse healthCheckResponse =
        new HealthCheckResponse(new URL(A_URL), OK.value(), now(), now());

    healthCheckResultsInMemory.add(ID, healthCheckResponse);
    healthCheckResultsInMemory.add(ID, healthCheckResponse);
    healthCheckResultsInMemory.add(ID, healthCheckResponse);

    final List<HealthCheckResponses> timedResults = healthCheckResultsInMemory.getResponses();

    assertThat(timedResults.size()).isEqualTo(1);
    assertThat(timedResults.get(0).getHealthCheckResponse().size()).isEqualTo(3);
  }

  //TODO flaky
  @Test
  void shouldGetHCResponsesOfListOfId() throws MalformedURLException {
    final HealthCheckResponse response =
        new HealthCheckResponse(new URL("http://www.e.com"), OK.value(), now(), now());

    final HealthCheckResponse response2 =
        new HealthCheckResponse(new URL("http://www.f.com"), OK.value(), now(), now());

    healthCheckResultsInMemory.add(new Id("e"), response);
    healthCheckResultsInMemory.add(new Id("f"), response2);

    final List<HealthCheckResponses> responses = healthCheckResultsInMemory.getResponses(Set.of(new Id("e"), new Id("f")));

    assertThat(responses.get(0)).usingRecursiveComparison().isEqualTo(new HealthCheckResponses(new Id("e"), response));
    assertThat(responses.get(1)).usingRecursiveComparison().isEqualTo(new HealthCheckResponses(new Id("f"), response2));

  }
}
