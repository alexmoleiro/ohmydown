package com.alexmoleiro.healthchecker.core;

import com.alexmoleiro.healthchecker.infrastructure.SiteCheckerResponse;

import java.util.ArrayList;
import java.util.List;

public class SiteResults {

  private List<SiteCheckerResponse> siteResults = new ArrayList<>();

  public SiteResults() {
  }

  public void add(SiteCheckerResponse siteCheckerResponse) {
    siteResults.add(siteCheckerResponse);
  }

  public List<SiteCheckerResponse> getSiteResults() {
    return this.siteResults;
  }
}
