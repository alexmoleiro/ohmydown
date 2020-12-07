package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.HealthCheckResponse;

import java.util.List;

public class SiteResultsDto {

  List<HealthCheckResponse> responses;

  public SiteResultsDto(List<HealthCheckResponse> responses) {
    this.responses = responses;
  }

  public List<HealthCheckResponse> getResponses() {
    return responses;
  }

  public int getNumUrls() {
    return responses.size();
  }
}
