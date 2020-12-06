package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.HealthChecker;
import com.alexmoleiro.healthchecker.core.SiteResults;
import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.core.SiteCheckerResponse;
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
  private final SiteResults siteResults;
  private final int nThreads;

  public HealthCheckerCrawler(HealthChecker healthChecker, SiteResults siteResults, int nThreads) {
    this.healthChecker = healthChecker;
    this.siteResults = siteResults;
    this.nThreads = nThreads;
  }

  public void run(List<String> domains) {
    ConcurrentLinkedDeque<String> domainsQueue = new ConcurrentLinkedDeque<>(domains);
    rangeClosed(1, nThreads).forEach(thread -> runAsync(() -> getHealthStatus(domainsQueue, now(systemUTC()))));
  }

  private void getHealthStatus(ConcurrentLinkedDeque<String> domains, LocalDateTime now) {

    while (domains.peek() != null) {
      final SiteCheckerResponse response = healthChecker.check(new WebStatusRequest(domains.poll()));
      siteResults.add(response);
      LOGGER.info(response.toString());
    }
  }
}
