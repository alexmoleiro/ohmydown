package com.alexmoleiro.healthchecker.core.healthCheck;

import java.util.List;

public interface HealthCheckRepository {

  List<HealthCheckResponses> getResponses();
  List<HealthCheckResponses> getResponses(List<Id> ids);
  HealthCheckResponses getResponses(Id id);
  void add(Id id, HealthCheckResponse response);
}