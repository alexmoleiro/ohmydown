package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.SiteResultsRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LandingApi {


  private final SiteResultsRepository siteResultsRepository;

  public LandingApi(SiteResultsRepository siteResultsRepository) {
    this.siteResultsRepository = siteResultsRepository;
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping(value = "/landing-list", produces = "application/json")
  SiteResultsDto webStatusResult() {
    return new SiteResultsDto(siteResultsRepository.getSiteResults());
  }
}
