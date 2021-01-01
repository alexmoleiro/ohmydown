package com.alexmoleiro.healthchecker.core.healthCheck;

import java.util.Optional;
import java.util.Set;

public interface EndpointRepository {

    void add(Endpoint endpoint);
    Optional<Endpoint> get(String id);
    Set<Endpoint> getAll();
}
