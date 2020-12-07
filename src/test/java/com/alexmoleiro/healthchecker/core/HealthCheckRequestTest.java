package com.alexmoleiro.healthchecker.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.of;

class HealthCheckRequestTest {

  @ParameterizedTest
  @MethodSource("invalidUrl")
  void shouldThrowExceptionWhenInvalidUrl(String invalidUrl) {

    assertThatThrownBy(() -> new HealthCheckRequest(invalidUrl))
        .isInstanceOf(WebStatusRequestException.class);
  }

  private static Stream<Arguments> invalidUrl() {
    return Stream.of(
        of("ftps://"),
        of("*"),
        of(".."),
        of("hola.."),
        of("-.."),
        of("/"),
        of(""),
        of("ftp://www"));
  }
}