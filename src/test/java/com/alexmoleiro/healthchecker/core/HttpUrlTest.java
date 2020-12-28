package com.alexmoleiro.healthchecker.core;

import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import com.alexmoleiro.healthchecker.core.healthCheck.WebStatusRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.URL;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.of;

class HttpUrlTest {

  @Test
  void shouldReturnValidHttpUrl() {
    URL url = new HttpUrl("a.com").getUrl();
    assertThat(url.toString()).isEqualTo("http://a.com");

  }


  @ParameterizedTest
  @MethodSource("invalidUrl")
  void shouldThrowExceptionWhenInvalidUrl(String invalidUrl) {

    assertThatThrownBy(() -> new HttpUrl(invalidUrl))
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