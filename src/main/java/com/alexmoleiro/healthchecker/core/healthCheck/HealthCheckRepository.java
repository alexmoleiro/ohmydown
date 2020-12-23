package com.alexmoleiro.healthchecker.core.healthCheck;

import java.util.List;
import java.util.Set;

public interface HealthCheckRepository {

  List<HealthCheckResponses> getResponses();
  List<HealthCheckResponses> getResponses(Set<Id> ids);
  HealthCheckResponses getResponses(Id id);
  void add(Id id, HealthCheckResponse response);
}