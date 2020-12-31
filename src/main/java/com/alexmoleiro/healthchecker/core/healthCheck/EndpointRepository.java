package com.alexmoleiro.healthchecker.core.healthCheck;

import java.util.Set;

public interface EndpointRepository {

    void add(Endpoint endpoint);
    Endpoint get(String id);
    Set<Endpoint> getAll();
}
