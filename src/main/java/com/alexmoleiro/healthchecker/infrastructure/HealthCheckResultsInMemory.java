package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.HealthCheckResultsRepository;
import com.alexmoleiro.healthchecker.core.Id;
import com.alexmoleiro.healthchecker.core.TimedHealthCheckResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class HealthCheckResultsInMemory implements HealthCheckResultsRepository {

  private Map<Id, TimedHealthCheckResponse> siteResults = new HashMap<>();

  public HealthCheckResultsInMemory() {}

  @Override
  public void add(TimedHealthCheckResponse timedHealthCheckResponse) {
    siteResults.put(timedHealthCheckResponse.getId(), timedHealthCheckResponse);
  }

  @Override
  public List<HealthCheckResponse> getSiteResults() {
    return siteResults.values().stream().map(x->x.getHealthCheckResponse()).collect(toList());
  }
}
