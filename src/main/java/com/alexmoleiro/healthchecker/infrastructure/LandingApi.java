package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.SiteResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.alexmoleiro.healthchecker.infrastructure.SiteStatus.UP;
import static java.util.List.of;

@RestController
public class LandingApi {

  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping(value = "/landing-list", produces = "application/json")
  SiteResultsDto webStatusResult() throws IOException {

    final List<SiteResult> siteResults = of(
        new SiteResult(new URL("https://www.alexmoleiro.com"), 200, UP)
    );

    return new SiteResultsDto(siteResults, siteResults.size());
  }
}
