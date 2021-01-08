package com.alexmoleiro.healthchecker.infrastructure.dto;

import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponses;

import java.util.List;

import static com.alexmoleiro.healthchecker.core.healthCheck.EndpointType.LANDING;
import static java.util.stream.Collectors.toList;

public class SiteResultsDto {

  List<HealthCheckResponses> responses;

  public SiteResultsDto(List<HealthCheckResponses> responses) {
    this.responses = responses.stream()
        .filter(response->response.getEndpoint().getEndpointType().equals(LANDING))
        .collect(toList());
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
