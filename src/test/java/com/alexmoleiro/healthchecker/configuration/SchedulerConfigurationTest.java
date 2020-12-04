package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.service.CheckStatusCrawler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.ConcurrentLinkedDeque;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest
class SchedulerConfigurationTest {

  public static final int ONCE = 1;
  public static final int TIMEOUT = 5_000;
  @MockBean
  CheckStatusCrawler checkStatusCrawler;

  @Test
  void shouldCallRun() {
    verify(checkStatusCrawler, timeout(TIMEOUT).atLeast(ONCE)).run(any(ConcurrentLinkedDeque.class));
  }
}