package com.alexmoleiro.healthchecker.core;

import java.time.LocalDateTime;
import java.util.List;

public interface HealthCheckResultsRepository {

  void add(String polledDomain, LocalDateTime now, HealthCheckResponse healthCheckResponse);
  List<HealthCheckResponse> getSiteResults();
}