package com.alexmoleiro.healthchecker.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;

@RestController
public class HealthApi {

  @Autowired
  SiteChecker siteChecker;

  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping("/status")
  SiteCheckerResponse webStatusResult(@RequestBody WebStatusRequest webStatusRequest)
      throws IOException, InterruptedException {
    return siteChecker.check(URI.create(webStatusRequest.getUrl()));
  }
}
