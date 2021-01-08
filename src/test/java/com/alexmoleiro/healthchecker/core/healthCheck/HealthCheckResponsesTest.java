package com.alexmoleiro.healthchecker.core.healthCheck;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class HealthCheckResponsesTest {

  @Test
  void shouldReturnUptime() {
      Assertions.assertThat(1).isEqualTo(2);
  }
}