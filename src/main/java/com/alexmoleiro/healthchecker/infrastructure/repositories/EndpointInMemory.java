package com.alexmoleiro.healthchecker.infrastructure.repositories;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.EndpointRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;

public class EndpointInMemory implements EndpointRepository {

    private Map<String, Endpoint> endpoints = new HashMap<>();

    @Override
    public void add(Endpoint endpoint) {
        endpoints.putIfAbsent(endpoint.getId(), endpoint);
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
