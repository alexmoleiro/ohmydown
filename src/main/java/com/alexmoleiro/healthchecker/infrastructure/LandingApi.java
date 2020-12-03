package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.SiteResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LandingApi {

  @Autowired
  SiteResults siteResults;

  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping(value = "/landing-list", produces = "application/json")
  SiteResultsDto webStatusResult() {
    return new SiteResultsDto(siteResults.getSiteResults());
  }
}
