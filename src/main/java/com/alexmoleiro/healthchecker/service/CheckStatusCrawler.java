package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.infrastructure.SiteCheckerResponse;
import com.alexmoleiro.healthchecker.infrastructure.WebStatusRequestDto;

import java.time.Clock;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.stream.IntStream.rangeClosed;

public class CheckStatusCrawler {
  // private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerConfiguration.class);
  private final HttpChecker httpChecker;
  private static final String PATTERN = "yyyyMMdd-HHmmss";

  public CheckStatusCrawler(HttpChecker httpChecker) {
    this.httpChecker = httpChecker;
  }

  public void run() {
    final String tickTime = now(Clock.systemUTC()).format(ofPattern(PATTERN));
    rangeClosed(1, 5).forEach(x -> runAsync(() -> getCheck()));
  }

  private SiteCheckerResponse getCheck() {
    final WebStatusRequest webStatusRequest = new WebStatusRequest(new WebStatusRequestDto("www.elpais.com"));
    return httpChecker.check(webStatusRequest);
  }
}
