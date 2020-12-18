package com.alexmoleiro.healthchecker.infrastructure.dto;

import com.alexmoleiro.healthchecker.core.healthCheck.TimedHealthCheckResponses;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class SiteResultsDto {

  List<TimedHealthCheckResponses> responses;

  public SiteResultsDto(List<TimedHealthCheckResponses> responses) {
    this.responses = responses;
  }

  public List<TimedSitedResultDto> getResponses() {
    return responses.stream()
        .map(
            responses ->
                new TimedSitedResultDto(
                    new TimedHealthCheckResponses(
                        responses.getId(), responses.getHealthCheckResponse().getLast())))
        .collect(toList());
  }

  public int getNumUrls() {
    return responses.size();
  }
}
