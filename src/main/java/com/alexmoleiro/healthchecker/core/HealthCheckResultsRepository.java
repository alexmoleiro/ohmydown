package com.alexmoleiro.healthchecker.core;

import java.util.List;

public interface HealthCheckResultsRepository {

  void add(TimedHealthCheckResponses timedHealthCheckResponses);
  List<TimedHealthCheckResponses> getSiteResults();
  TimedHealthCheckResponses getResponses(Id id);
}