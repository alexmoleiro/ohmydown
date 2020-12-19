package com.alexmoleiro.healthchecker.core.healthCheck;

import java.util.List;

public interface HealthCheckResultsRepository {

  List<HealthCheckResponses> getTimedResults();
  HealthCheckResponses getResponses(Id id);
  void add(Id id, HealthCheckResponse response);
}