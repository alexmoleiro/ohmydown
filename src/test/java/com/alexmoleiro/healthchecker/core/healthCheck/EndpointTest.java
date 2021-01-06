package com.alexmoleiro.healthchecker.core.healthCheck;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class EndpointTest {

  @Test
  void shouldCreateIdSeo() {
    assertThat(new Endpoint(new HttpUrl("www.as.com")).getId())
            .asString().startsWith("as.com-");
  }

  @ParameterizedTest
  @MethodSource("domainNames")
  void shouldCreateGroup(String domainName, String group) {
    Endpoint endpoint = new Endpoint(new HttpUrl(domainName));

    assertThat(endpoint.getGroup()).isEqualTo(group);
  }

  private static Stream<Arguments> domainNames() {
    return Stream.of(
            of("a.com", "a.com"),
            of("www.a.com", "a.com"),
            of("alu-etsetb.upc.es", "upc.es"),
            of("b3.alu-etsetb.upc.es", "upc.es")
    );
  }
}
