package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.service.HealthCheckerCrawler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest
class SchedulerConfigurationTest {

  public static final int ONCE = 1;
  public static final int TIMEOUT = 5_000;

  @MockBean
  HealthCheckerCrawler healthCheckerCrawler;

  @Test
  void shouldCallRun() {
    verify(healthCheckerCrawler, timeout(TIMEOUT).atLeast(ONCE)).run(anyList());
  }
}