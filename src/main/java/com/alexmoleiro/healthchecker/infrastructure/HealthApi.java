package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.service.WebStatusRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class HealthApi {

  private final SiteChecker siteChecker;

  public HealthApi(SiteChecker siteChecker) {
    this.siteChecker = siteChecker;
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping("/status")
  SiteCheckerResponse webStatusResult(@RequestBody WebStatusRequestDto webStatusRequestDto)
      throws IOException, InterruptedException, URISyntaxException {
    return siteChecker.check(new WebStatusRequest(webStatusRequestDto));
  }
}
