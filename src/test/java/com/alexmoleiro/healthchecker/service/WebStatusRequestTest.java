package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.infrastructure.WebStatusRequestDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebStatusRequestTest {

  @ParameterizedTest
  @MethodSource("invalidUrl")
  void shouldThrowExceptionWhenInvalidUrl(String invalidUrl) {

    final WebStatusRequestDto webStatusRequestDto = mock(WebStatusRequestDto.class);
    when(webStatusRequestDto.getUrl()).thenReturn(invalidUrl);
  }

  private static Stream<Arguments> invalidUrl() {
    return Stream.of(
        Arguments.of("www"),
        Arguments.of("ftps://")
    );
  }
}