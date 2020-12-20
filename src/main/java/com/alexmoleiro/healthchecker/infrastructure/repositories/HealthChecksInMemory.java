package com.alexmoleiro.healthchecker.infrastructure.repositories;

import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponses;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.Id;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class HealthChecksInMemory implements HealthCheckRepository {

  private Map<Id, HealthCheckResponses> siteResults = new HashMap<>();

  public HealthChecksInMemory() {}

  @Override
  public List<HealthCheckResponses> getResponses() {
    return siteResults.values().stream().collect(toList());
  }

  @Override
  public List<HealthCheckResponses> getResponses(List<Id> ids) {
    return  ids.stream().map(id -> getResponses(id)).collect(toList());
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