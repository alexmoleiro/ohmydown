package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.healthCheck.*;
import com.alexmoleiro.healthchecker.infrastructure.repositories.HealthChecksInMemory;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;

import static java.time.LocalDateTime.now;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

class HealthChecksInMemoryTest {

  private static final Endpoint ENDPOINT = new Endpoint(new HttpUrl("http://www.id.com"));
  private static final String A_URL = "https://www.a.com";
  private HealthCheckRepository healthCheckResultsInMemory = new HealthChecksInMemory();

  @Test
  void returnHealthCheckResults() {
    final HealthCheckResponse healthCheckResponse =
        new HealthCheckResponse(new HttpUrl(A_URL), OK.value(), now(), now());

    final HealthCheckResponses healthCheckResponses =
        new HealthCheckResponses(ENDPOINT, healthCheckResponse);

    healthCheckResultsInMemory.add(ENDPOINT, healthCheckResponse);

    final List<HealthCheckResponses> siteResults = healthCheckResultsInMemory.getResponses();

    assertThat(siteResults).usingRecursiveComparison().isEqualTo(of(healthCheckResponses));
  }

  @Test
  void shouldReturnOnlyOneResults() {

    final HealthCheckResponse healthCheckResponse =
        new HealthCheckResponse(new HttpUrl(A_URL), OK.value(), now(), now());

    healthCheckResultsInMemory.add(ENDPOINT, healthCheckResponse);
    healthCheckResultsInMemory.add(ENDPOINT, healthCheckResponse);
    healthCheckResultsInMemory.add(ENDPOINT, healthCheckResponse);

    final List<HealthCheckResponses> timedResults = healthCheckResultsInMemory.getResponses();

    assertThat(timedResults.size()).isEqualTo(1);
    assertThat(timedResults.get(0).getHealthCheckResponse().size()).isEqualTo(3);
  }

  @Test
  void shouldGetHCResponsesOfListOfId() {
    final HealthCheckResponse response =
        new HealthCheckResponse(new HttpUrl("http://www.e.com"), OK.value(), now(), now());

    final HealthCheckResponse response2 =
        new HealthCheckResponse(new HttpUrl("http://www.f.com"), OK.value(), now(), now());

    healthCheckResultsInMemory.add(new Endpoint(new HttpUrl("http://www.e.com")), response);
    healthCheckResultsInMemory.add(new Endpoint(new HttpUrl("http://www.f.com")), response2);

    final List<HealthCheckResponses> responses =
        healthCheckResultsInMemory.getResponses(Set.of(new Endpoint(new HttpUrl("http://www.e.com")), new Endpoint(new HttpUrl("http://www.f.com"))));
    assertThat(responses).extracting("endpoint").extracting("id").contains("http://www.e.com", "http://www.f.com");
  }
}
