package com.alexmoleiro.healthchecker.core;

import org.junit.jupiter.api.Test;

import static com.alexmoleiro.healthchecker.core.UserAgent.random;
import static org.assertj.core.api.Assertions.assertThat;

class UserAgentTest {

  @Test
  void returnsValidUserAgent() {
    assertThat(random()).startsWith("Mozilla");
  }
}