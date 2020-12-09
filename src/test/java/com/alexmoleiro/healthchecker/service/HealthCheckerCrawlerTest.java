package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.HealthCheckRequest;
import com.alexmoleiro.healthchecker.core.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.HealthCheckResultsRepository;
import com.alexmoleiro.healthchecker.core.Id;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.List.of;
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
  void shouldCallOnlyOnce() throws MalformedURLException {

    final HealthCheckerClient healthCheckerClient = mock(HealthCheckerClient.class);
    final HealthCheckResultsRepository healthCheckResultsRepository =
        mock(HealthCheckResultsRepository.class);
    final List<String> domains = of("www.a.com", "www.b.com", "www.c.com", "www.d.es", "www.e.com");
    final int nThreads = 5;
    final URL url = new URL("http://www.j.com");

    when(healthCheckerClient.check(any(HealthCheckRequest.class)))
        .thenReturn(new HealthCheckResponse(url, OK.value(), now(), now()));

    new HealthCheckerCrawler(healthCheckerClient, healthCheckResultsRepository, nThreads)
        .run(domains);

    domains.stream()
        .forEach(
            domain -> {
              verify(healthCheckerClient, timeout(TIMEOUT).atLeast(ONCE))
                  .check(
                      argThat(request -> request.getUrl().toString().equals("http://" + domain)));

              verify(healthCheckResultsRepository, timeout(TIMEOUT).atLeast(nThreads))
                  .add(any(Id.class),any(HealthCheckResponse.class));
            });
  }
}
