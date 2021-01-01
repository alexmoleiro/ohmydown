package com.alexmoleiro.healthchecker.infrastructure.repositories;


import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import org.junit.jupiter.api.Test;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;

class EndpointInMemoryTest {

    private EndpointInMemory endpointInMemory = new EndpointInMemory();

    @Test
    void shouldReturnEmpty() {

        assertThat(endpointInMemory.get("anId")).isEqualTo(empty());
    }

    @Test
    void shouldFindOnlyOneEndpointIfItsTheSame() {
        Endpoint endpoint = new Endpoint(new HttpUrl("www.alexmoleiro.com"));
        endpointInMemory.add(endpoint);
        endpointInMemory.add(endpoint);

        assertThat(endpointInMemory.getAll())
                .containsOnly(endpoint);

    }
}