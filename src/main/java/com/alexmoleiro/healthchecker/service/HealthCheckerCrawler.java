package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthChecker;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.stream.IntStream.rangeClosed;
import static org.slf4j.LoggerFactory.getLogger;

public class HealthCheckerCrawler {
  private static final Logger LOGGER = getLogger(HealthCheckerCrawler.class);
  private final HealthChecker healthChecker;
  private final HealthCheckRepository healthCheckRepository;
  private final int nThreads;

  public HealthCheckerCrawler(
      HealthChecker healthChecker,
      HealthCheckRepository healthCheckRepository,
      int nThreads) {
    this.healthChecker = healthChecker;
    this.healthCheckRepository = healthCheckRepository;
    this.nThreads = nThreads;
  }

  public void run(List<String> domains) {
    ConcurrentLinkedDeque<String> domainsQueue = new ConcurrentLinkedDeque<>(domains);
    rangeClosed(1, nThreads).forEach(thread -> runAsync(() -> getHealthStatus(domainsQueue)));
  }

  private void getHealthStatus(ConcurrentLinkedDeque<String> domains) {

    while (domains.peek() != null) {
      final String polledDomain = domains.poll();
      final HealthCheckResponse response =
          healthChecker.check(new HttpUrl(polledDomain));
      healthCheckRepository.add(new Endpoint(polledDomain), response);
      LOGGER.info(response.toString());
    }
  }
}
