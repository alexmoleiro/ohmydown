package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.HealthChecker;
import com.alexmoleiro.healthchecker.core.HealthCheckResultsRepository;
import com.alexmoleiro.healthchecker.core.HealthCheckRequest;
import com.alexmoleiro.healthchecker.core.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.Id;
import com.alexmoleiro.healthchecker.core.TimedHealthCheckResponse;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.time.Clock.systemUTC;
import static java.time.LocalDateTime.now;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.stream.IntStream.rangeClosed;
import static org.slf4j.LoggerFactory.getLogger;

public class HealthCheckerCrawler {
  private static final Logger LOGGER = getLogger(HealthCheckerCrawler.class);
  private final HealthChecker healthChecker;
  private final HealthCheckResultsRepository healthCheckResultsRepository;
  private final int nThreads;

  public HealthCheckerCrawler(HealthChecker healthChecker, HealthCheckResultsRepository healthCheckResultsRepository, int nThreads) {
    this.healthChecker = healthChecker;
    this.healthCheckResultsRepository = healthCheckResultsRepository;
    this.nThreads = nThreads;
  }

  public void run(List<String> domains) {
    ConcurrentLinkedDeque<String> domainsQueue = new ConcurrentLinkedDeque<>(domains);
    rangeClosed(1, nThreads).forEach(thread -> runAsync(() -> getHealthStatus(domainsQueue, now(systemUTC()))));
  }

  private void getHealthStatus(ConcurrentLinkedDeque<String> domains, LocalDateTime now) {

    while (domains.peek() != null) {
      final String polledDomain = domains.poll();
      final HealthCheckResponse response = healthChecker.check(new HealthCheckRequest(polledDomain));
      healthCheckResultsRepository.add(new TimedHealthCheckResponse(new Id(polledDomain), now, response));
      LOGGER.info(response.toString());
    }
  }
}
