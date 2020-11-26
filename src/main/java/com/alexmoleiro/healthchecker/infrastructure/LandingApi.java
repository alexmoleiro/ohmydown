package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.SiteResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URL;

import static com.alexmoleiro.healthchecker.infrastructure.SiteStatus.DOWN;
import static com.alexmoleiro.healthchecker.infrastructure.SiteStatus.UP;
import static java.util.List.of;

@RestController
public class LandingApi {

  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping(value="/landing-list", produces = "application/json" )
  SiteResultsDto webStatusResult() throws MalformedURLException {

    return new SiteResultsDto(
        of(
            new SiteResult(new URL("https://www.yavendras.com"), 200, UP),
            new SiteResult(new URL("https://www.alexmoleiro.com"), 123, DOWN)
        ), 2);
  }
}
