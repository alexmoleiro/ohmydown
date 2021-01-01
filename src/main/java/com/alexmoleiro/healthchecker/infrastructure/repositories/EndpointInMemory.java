package com.alexmoleiro.healthchecker.infrastructure.repositories;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.EndpointRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

public class EndpointInMemory implements EndpointRepository {

    Map<String, Endpoint> endpoints = new HashMap<>();

    @Override
    public void add(Endpoint endpoint) {
        if(endpoints.containsValue(endpoint)) {
            throw new EndpointExistingException();
        }
        endpoints.put(endpoint.getId(), endpoint);
    }

    @Override
    public Optional<Endpoint> get(String id) {
        return ofNullable(endpoints.get(id));
    }

    @Override
    public Set<Endpoint> getAll() {
        return new HashSet<>(endpoints.values());
    }
}
