package com.alexmoleiro.healthchecker.infrastructure.repositories;

import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResultsRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.Id;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class HealthCheckResultsInMemory implements HealthCheckResultsRepository {

  private Map<Id, HealthCheckResponses> siteResults = new HashMap<>();

  public HealthCheckResultsInMemory() {}

  @Override
  public List<HealthCheckResponses> getTimedResults() {
    return siteResults.values().stream().collect(toList());
  }

  @Override
  public HealthCheckResponses getResponses(Id id) {
    return siteResults.get(id);
  }

  @Override
  public void add(Id id, HealthCheckResponse response) {
    if (!siteResults.containsKey(id)) {
      siteResults.put(id, new HealthCheckResponses(id, response));
    } else {
      siteResults.get(id).addLast(response);
    }
  }
}