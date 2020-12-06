package com.alexmoleiro.healthchecker.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class SiteResults {

  private Map<String, SiteCheckerResponse> siteResults = new HashMap<>();

  public SiteResults() {}

  public void add(SiteCheckerResponse siteCheckerResponse) {
    siteResults.put(siteCheckerResponse.getUrl(), siteCheckerResponse);
  }

  public List<SiteCheckerResponse> getSiteResults() {
    return siteResults.values().stream().collect(toList());
  }
}
