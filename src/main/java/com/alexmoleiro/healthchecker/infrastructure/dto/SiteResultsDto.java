package com.alexmoleiro.healthchecker.infrastructure.dto;

import com.alexmoleiro.healthchecker.core.TimedHealthCheckResponses;

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
            x ->
                new TimedSitedResultDto(
                    new TimedHealthCheckResponses(
                        x.getId(), x.getLocalDateTime(), x.getHealthCheckResponse().get(0))))
        .collect(toList());
  }

  public int getNumUrls() {
    return responses.size();
  }
}
