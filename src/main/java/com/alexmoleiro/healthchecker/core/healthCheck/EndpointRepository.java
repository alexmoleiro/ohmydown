package com.alexmoleiro.healthchecker.core.healthCheck;

public interface EndpointRepository {

    void add(Endpoint endpoint);
    Endpoint get(String id);
}
