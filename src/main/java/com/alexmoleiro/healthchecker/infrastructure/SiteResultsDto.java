package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.SiteResult;

import java.util.List;

public class SiteResultsDto {

  List<SiteResult> siteResults;
  int numUrls;

  public SiteResultsDto(List<SiteResult> siteResults, int numUrls) {
    this.siteResults = siteResults;
    this.numUrls = numUrls;
  }

  public List<SiteResult> getSiteResults() {
    return siteResults;
  }

  public int getNumUrls() {
    return numUrls;
  }
}
