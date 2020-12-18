package com.alexmoleiro.healthchecker.core.healthCheck;

import java.util.List;

public interface HealthCheckResultsRepository {

  List<TimedHealthCheckResponses> getTimedResults();
  TimedHealthCheckResponses getResponses(Id id);
  void add(Id id, HealthCheckResponse response);
}