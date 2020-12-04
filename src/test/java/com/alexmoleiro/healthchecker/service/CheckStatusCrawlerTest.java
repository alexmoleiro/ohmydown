package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.SiteResults;
import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.infrastructure.SiteCheckerResponse;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.util.List.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CheckStatusCrawlerTest {

  private static final int ONCE = 1;
  private static final int TIMEOUT = 2_000;

  @Test
  void shouldCallOnlyOnce() {

    final HttpChecker httpChecker = mock(HttpChecker.class);
    final SiteResults siteResults = mock(SiteResults.class);
    final List<String> domains = of("www.a.com", "www.b.com", "www.c.com", "www.d.es", "www.e.com");
    final ConcurrentLinkedDeque<String> domainsQueue = new ConcurrentLinkedDeque<>(domains);
    final int nThreads = 5;

    final String anyString = "www.j.com";
    when(httpChecker.check(any(WebStatusRequest.class)))
        .thenReturn(new SiteCheckerResponse(anyString, 200, 123));

    new CheckStatusCrawler(httpChecker, siteResults, nThreads).run(domainsQueue);

    domains.stream()
        .forEach(
            domain -> {
              verify(httpChecker, timeout(TIMEOUT).atLeast(ONCE))
                  .check(argThat(request -> request.getUrl().toString().equals("http://" + domain)));

              verify(siteResults, timeout(TIMEOUT).atLeast(nThreads))
                  .add(argThat(x->x.getUrl().equals(anyString)));
            });
  }
}
