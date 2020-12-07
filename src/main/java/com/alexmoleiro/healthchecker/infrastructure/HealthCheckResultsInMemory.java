package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.HealthCheckResultsRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class HealthCheckResultsInMemory implements HealthCheckResultsRepository {

  private Map<String, HealthCheckResponse> siteResults = new HashMap<>();

  public HealthCheckResultsInMemory() {}

  @Override
  public void add(HealthCheckResponse healthCheckResponse) {
    siteResults.put(healthCheckResponse.getUrl(), healthCheckResponse);
  }

  @Override
  public List<HealthCheckResponse> getSiteResults() {
    return siteResults.values().stream().collect(toList());
  }
}
