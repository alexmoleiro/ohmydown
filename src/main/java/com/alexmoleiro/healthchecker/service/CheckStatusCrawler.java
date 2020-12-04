package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.infrastructure.WebStatusRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.stream.IntStream.rangeClosed;

public class CheckStatusCrawler {
  private static final Logger LOGGER = LoggerFactory.getLogger(CheckStatusCrawler.class);
  private final HttpChecker httpChecker;
  private static final String PATTERN = "yyyyMMdd-HHmmss";

  public CheckStatusCrawler(HttpChecker httpChecker) {
    this.httpChecker = httpChecker;
  }

  public void run(ConcurrentLinkedDeque<String> domains, int nThreads) {
    final String tickTime = now(Clock.systemUTC()).format(ofPattern(PATTERN));
    rangeClosed(1, nThreads).forEach(x -> runAsync(() -> getCheck(domains, tickTime)));
  }

  private void getCheck(ConcurrentLinkedDeque<String> domains, String tickTime) {
    while (domains.peek() != null) {
      final WebStatusRequest webStatusRequest =
          new WebStatusRequest(new WebStatusRequestDto(domains.poll()));
      httpChecker.check(webStatusRequest);
    }
  }
}
