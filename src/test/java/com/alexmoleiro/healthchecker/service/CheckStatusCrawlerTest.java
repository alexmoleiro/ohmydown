package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

class CheckStatusCrawlerTest {

  private static final int INVOCATIONS = 5;
  private static final int TIMEOUT = 10_000;

  @Test
  void shouldCall() {

    final HttpChecker httpChecker = mock(HttpChecker.class);

    new CheckStatusCrawler(httpChecker).run();

    verify(httpChecker, timeout(TIMEOUT).atLeast(INVOCATIONS)).check(any(WebStatusRequest.class));
  }
}
