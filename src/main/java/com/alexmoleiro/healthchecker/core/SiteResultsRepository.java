package com.alexmoleiro.healthchecker.core;

import java.util.List;

public interface SiteResultsRepository {

  void add(SiteCheckerResponse siteCheckerResponse);
  List<SiteCheckerResponse> getSiteResults();

}
