package com.alexmoleiro.healthchecker.infrastructure.dto;

import com.alexmoleiro.healthchecker.core.TimedHealthCheckResponse;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class SiteResultsDto {

  List<TimedHealthCheckResponse> responses;

  public SiteResultsDto(List<TimedHealthCheckResponse> responses) {
    this.responses = responses;
  }

  public List<TimedSitedResultDto> getResponses() {
    return responses.stream()
        .map(x -> new TimedSitedResultDto(new TimedHealthCheckResponse(x.getId(), x.getLocalDateTime(), x.getHealthCheckResponse()))).collect(
            toList());
  }

  public int getNumUrls() {
    return responses.size();
  }
}
