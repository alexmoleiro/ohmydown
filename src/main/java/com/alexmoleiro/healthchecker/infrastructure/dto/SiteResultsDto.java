package com.alexmoleiro.healthchecker.infrastructure.dto;

import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponses;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class SiteResultsDto {

  List<HealthCheckResponses> responses;

  public SiteResultsDto(List<HealthCheckResponses> responses) {
    this.responses = responses;
  }

  public List<TimedSitedResultDto> getResponses() {
    return responses.stream()
        .map(
            responses ->
                new TimedSitedResultDto(
                    new HealthCheckResponses(
                        responses.getEndpoint(), responses.getHealthCheckResponse().getLast())))
        .collect(toList());
  }

  public int getNumUrls() {
    return responses.size();
  }
}
