package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static java.time.LocalDateTime.now;
import static java.util.List.of;
import static java.util.stream.Collectors.toSet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

class HealthCheckerCrawlerTest {

  private static final int ONCE = 1;
  private static final int TIMEOUT = 2_000;

  @Test
  void shouldCallOnlyOnce() {

    final HealthCheckerClient healthCheckerClient = mock(HealthCheckerClient.class);
    final HealthCheckRepository healthCheckRepository =
        mock(HealthCheckRepository.class);
    final List<String> domains = of("www.a.com", "www.b.com", "www.c.com", "www.d.es", "www.e.com");
    Set<Endpoint> endpoints = domains.stream().map(domain -> new Endpoint(new HttpUrl(domain))).collect(toSet());
    final int nThreads = 5;
    final HttpUrl url = new HttpUrl("http://www.j.com");

    when(healthCheckerClient.check(any(HttpUrl.class)))
        .thenReturn(new HealthCheckResponse(url, OK.value(), now(), now()));

    new HealthCheckerCrawler(healthCheckerClient, healthCheckRepository, nThreads)
        .run(endpoints);

    domains.stream()
        .forEach(
            domain -> {
              verify(healthCheckerClient, timeout(TIMEOUT).atLeast(ONCE))
                  .check(
                      argThat(request -> request.getUrl().toString().equals("http://" + domain)));

              verify(healthCheckRepository, timeout(TIMEOUT).atLeast(nThreads))
                  .add(any(Endpoint.class),any(HealthCheckResponse.class));
            });
  }
}
