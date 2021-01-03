package com.alexmoleiro.healthchecker.core.healthCheck;

import java.util.List;
import java.util.Set;

public interface HealthCheckRepository {

  List<HealthCheckResponses> getResponses();
  List<HealthCheckResponses> getResponses(Set<Endpoint> endpoints);
  HealthCheckResponses getResponses(Endpoint endpoint);
  void add(Endpoint endpoint, HealthCheckResponse response);
  void deleteAll();
}