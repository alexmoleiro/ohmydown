package com.alexmoleiro.healthchecker.core;

import com.alexmoleiro.healthchecker.infrastructure.WebStatusRequestDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.MalformedURLException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebStatusRequestTest {

  @ParameterizedTest
  @MethodSource("invalidUrl")
  void shouldThrowExceptionWhenInvalidUrl(String invalidUrl) {

    final WebStatusRequestDto webStatusRequestDto = mock(WebStatusRequestDto.class);
    when(webStatusRequestDto.getUrl()).thenReturn(invalidUrl);

    assertThatThrownBy(
        ()-> new WebStatusRequest(webStatusRequestDto)
    ).isInstanceOf(MalformedURLException.class);

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
        of("ftp://www")
    );
  }
}