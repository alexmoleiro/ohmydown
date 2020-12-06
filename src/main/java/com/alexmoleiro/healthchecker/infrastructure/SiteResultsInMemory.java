package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.SiteCheckerResponse;
import com.alexmoleiro.healthchecker.core.SiteResultsRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class SiteResultsInMemory implements SiteResultsRepository {

  private Map<String, SiteCheckerResponse> siteResults = new HashMap<>();

  public SiteResultsInMemory() {}

  @Override
  public void add(SiteCheckerResponse siteCheckerResponse) {
    siteResults.put(siteCheckerResponse.getUrl(), siteCheckerResponse);
  }

  @Override
  public List<SiteCheckerResponse> getSiteResults() {
    return siteResults.values().stream().collect(toList());
  }
}
