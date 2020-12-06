package com.alexmoleiro.healthchecker.infrastructure;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LandingApi {


  private final SiteResultsInMemory siteResultsInMemory;

  public LandingApi(SiteResultsInMemory siteResultsInMemory) {
    this.siteResultsInMemory = siteResultsInMemory;
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping(value = "/landing-list", produces = "application/json")
  SiteResultsDto webStatusResult() {
    return new SiteResultsDto(siteResultsInMemory.getSiteResults());
  }
}
