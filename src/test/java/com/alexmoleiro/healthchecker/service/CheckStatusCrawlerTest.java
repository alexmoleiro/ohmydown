package com.alexmoleiro.healthchecker.service;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.util.List.of;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

class CheckStatusCrawlerTest {

  private static final int ONCE = 1;
  private static final int TIMEOUT = 10_000;

  @Test
  void shouldCallOnlyOnce() {

    final HttpChecker httpChecker = mock(HttpChecker.class);
    final List<String> domains = of("www.a.com", "www.b.com", "www.c.com", "www.d.es", "www.e.com");
    final ConcurrentLinkedDeque<String> domainsQueue = new ConcurrentLinkedDeque<>(domains);
    final int nThreads = 5;

    new CheckStatusCrawler(httpChecker, nThreads).run(domainsQueue);

    domains.stream()
        .forEach(
            domain -> verify(httpChecker, timeout(TIMEOUT).atLeast(ONCE))
                .check(argThat(request -> request.getUrl().toString().equals("http://" + domain))));
  }
}