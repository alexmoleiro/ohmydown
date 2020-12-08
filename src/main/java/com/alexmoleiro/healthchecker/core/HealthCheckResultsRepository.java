package com.alexmoleiro.healthchecker.core;

import java.util.List;

public interface HealthCheckResultsRepository {

  void add(HealthCheckResponse healthCheckResponse);
  List<HealthCheckResponse> getSiteResults();
}