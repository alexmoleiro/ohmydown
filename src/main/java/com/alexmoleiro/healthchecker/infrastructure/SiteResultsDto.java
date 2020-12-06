package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.SiteCheckerResponse;

import java.util.List;

public class SiteResultsDto {

  List<SiteCheckerResponse> responses;

  public SiteResultsDto(List<SiteCheckerResponse> responses) {
    this.responses = responses;
  }

  public List<SiteCheckerResponse> getResponses() {
    return responses;
  }

  public int getNumUrls() {
    return responses.size();
  }
}
