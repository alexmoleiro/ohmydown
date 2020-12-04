package com.alexmoleiro.healthchecker.core;

import com.alexmoleiro.healthchecker.infrastructure.SiteCheckerResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SiteResults {

  private Map<String, SiteCheckerResponse> siteResults = new HashMap<>();

  public SiteResults() {}

  public void add(SiteCheckerResponse siteCheckerResponse) {
    siteResults.put(siteCheckerResponse.getUrl(), siteCheckerResponse);
  }

  public List<SiteCheckerResponse> getSiteResults() {
    return siteResults.values().stream().collect(Collectors.toList());
  }
}
