package com.alexmoleiro.healthchecker.infrastructure.repositories;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class HealthChecksInMemory implements HealthCheckRepository {

    private Map<Endpoint, HealthCheckResponses> siteResults = new HashMap<>();

    public HealthChecksInMemory() {
    }

    @Override
    public List<HealthCheckResponses> getResponses() {
        return new ArrayList<>(siteResults.values());
    }

    @Override
    public List<HealthCheckResponses> getResponses(Set<Endpoint> endpoints) {
        return endpoints.stream()
                .map(this::getResponses)
                .collect(toList());
    }

    @Override
    public HealthCheckResponses getResponses(Endpoint endpoint) {
        return siteResults.getOrDefault(endpoint, new HealthCheckResponses(endpoint));
    }

    @Override
    public void add(Endpoint endpoint, HealthCheckResponse response) {
        if (!siteResults.containsKey(endpoint)) {
            siteResults.put(endpoint, new HealthCheckResponses(endpoint, response));
        } else {
            siteResults.get(endpoint).addLast(response);
        }
    }

    public void deleteAll() {
        siteResults.clear();
    }
}