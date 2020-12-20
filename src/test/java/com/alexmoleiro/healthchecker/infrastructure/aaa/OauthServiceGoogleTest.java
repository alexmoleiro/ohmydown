package com.alexmoleiro.healthchecker.infrastructure.aaa;

import com.alexmoleiro.healthchecker.infrastructure.api.InvalidTokenException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OauthServiceGoogleTest {

  @Test
  void shouldReturnInvalidTokenException() {

    assertThatThrownBy(() -> new OauthServiceGoogle("aGoogleId").getUser(""))
        .isInstanceOf(InvalidTokenException.class)
        .hasCauseInstanceOf(IllegalArgumentException.class);
  }
}