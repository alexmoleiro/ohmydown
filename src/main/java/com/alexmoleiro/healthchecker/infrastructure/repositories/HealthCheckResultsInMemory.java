package com.alexmoleiro.healthchecker.infrastructure.repositories;

import com.alexmoleiro.healthchecker.core.HealthCheckResultsRepository;
import com.alexmoleiro.healthchecker.core.Id;
import com.alexmoleiro.healthchecker.core.TimedHealthCheckResponses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class HealthCheckResultsInMemory implements HealthCheckResultsRepository {

  private Map<Id, TimedHealthCheckResponses> siteResults = new HashMap<>();

  public HealthCheckResultsInMemory() {}

  @Override
  public void add(TimedHealthCheckResponses timedHealthCheckResponses) {
    siteResults.put(timedHealthCheckResponses.getId(), timedHealthCheckResponses);
  }

  @Override
  public List<TimedHealthCheckResponses> getSiteResults() {
    return siteResults.values().stream().collect(toList());
  }

  @Override
  public TimedHealthCheckResponses getResponse(Id id) {
    return siteResults.get(id);
  }
}