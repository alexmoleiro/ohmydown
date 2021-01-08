package com.alexmoleiro.healthchecker.infrastructure.repositories;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponses;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static com.alexmoleiro.healthchecker.core.healthCheck.EndpointType.DEFAULT;
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

        final List<HealthCheckResponses> siteResults = healthCheckResultsInMemory.getResponses(DEFAULT);

        assertThat(siteResults).usingRecursiveComparison().isEqualTo(of(healthCheckResponses));
    }

    @Test
    void shouldReturnOnlyOneResults() {

        final HealthCheckResponse healthCheckResponse =
                new HealthCheckResponse(new HttpUrl(A_URL), OK.value(), now(), now());

        healthCheckResultsInMemory.add(ENDPOINT, healthCheckResponse);
        healthCheckResultsInMemory.add(ENDPOINT, healthCheckResponse);
        healthCheckResultsInMemory.add(ENDPOINT, healthCheckResponse);

        final List<HealthCheckResponses> timedResults = healthCheckResultsInMemory.getResponses(DEFAULT);

        assertThat(timedResults.size()).isEqualTo(1);
        assertThat(timedResults.get(0).getHealthCheckResponse().size()).isEqualTo(3);
    }

    @Test
    void shouldGetHCResponsesOfListOfId() {
        HttpUrl httpUrlE = new HttpUrl("http://www.e.com");
        final HealthCheckResponse response = new HealthCheckResponse(httpUrlE, OK.value(), now(), now());

        HttpUrl httpUrlF = new HttpUrl("http://www.f.com");
        final HealthCheckResponse response2 = new HealthCheckResponse(httpUrlF, OK.value(), now(), now());

        healthCheckResultsInMemory.add(new Endpoint(httpUrlE), response);
        healthCheckResultsInMemory.add(new Endpoint(httpUrlF), response2);

        final List<HealthCheckResponses> responses =
                healthCheckResultsInMemory.getResponses(Set.of(new Endpoint(httpUrlE), new Endpoint(httpUrlF)));
        assertThat(responses).extracting("endpoint")
                .extracting("httpUrl")
                .containsOnly(httpUrlE, httpUrlF);

        assertThat(responses.get(0).getHealthCheckResponse()).isNotEmpty();
        assertThat(responses.get(1).getHealthCheckResponse()).isNotEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenNoHealthCheckResultsPresentYetForAGivenEndpoint() {
        HealthCheckResponses responsesA = healthCheckResultsInMemory.getResponses(new Endpoint(new HttpUrl("www.a.com")));
        HealthCheckResponses responsesB = healthCheckResultsInMemory.getResponses(new Endpoint(new HttpUrl("www.b.com")));

        assertThat(responsesA.getHealthCheckResponse()).isEmpty();
        assertThat(responsesB.getHealthCheckResponse()).isEmpty();

    }

    @Test
    void shouldntReturnResponsesForEndpointThatDoesntHaveAnyChecksYet() {

        HttpUrl httpUrlE = new HttpUrl("http://www.e.com");
        final HealthCheckResponse response = new HealthCheckResponse(httpUrlE, OK.value(), now(), now());

        healthCheckResultsInMemory.add(new Endpoint(httpUrlE), response);

        HttpUrl httpWithoutResults = new HttpUrl("http://www.endpointWithoutResults.com");
        final List<HealthCheckResponses> responses =
                healthCheckResultsInMemory.getResponses(Set.of(
                        new Endpoint(httpUrlE),
                        new Endpoint(httpWithoutResults))
                );

        assertThat(responses).extracting("endpoint")
                .extracting("httpUrl")
                .containsOnly(httpUrlE, httpWithoutResults);
    }

}
