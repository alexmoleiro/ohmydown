package com.alexmoleiro.healthchecker.infrastructure.repositories;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.EndpointRepository;

import java.util.HashMap;
import java.util.Map;

public class EndpointInMemory implements EndpointRepository {

    Map<String, Endpoint> endpoints = new HashMap<>();

    @Override
    public void add(Endpoint endpoint) {
            endpoints.put(endpoint.getId(), endpoint);
    }

    @Override
    public Endpoint get(String id) {
        return endpoints.get(id);
    }
}
