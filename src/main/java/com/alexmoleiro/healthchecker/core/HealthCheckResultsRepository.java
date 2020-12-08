package com.alexmoleiro.healthchecker.core;

import java.util.List;

public interface HealthCheckResultsRepository {

  void add(TimedHealthCheckResponse timedHealthCheckResponse);
  List<TimedHealthCheckResponse> getSiteResults();
}