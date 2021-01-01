package com.alexmoleiro.healthchecker.infrastructure.repositories;


import org.junit.jupiter.api.Test;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;

class EndpointInMemoryTest {


    @Test
    void shouldReturnEmpty() {
        EndpointInMemory endpointInMemory = new EndpointInMemory();

        assertThat(endpointInMemory.get("anId")).isEqualTo(empty());
    }
}