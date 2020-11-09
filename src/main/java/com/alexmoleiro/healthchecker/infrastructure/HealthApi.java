package com.alexmoleiro.healthchecker.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;

@RestController
public class HealthApi {

  @Autowired
  HttpClient httpClient;

  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping("/status")
  SiteCheckerResponse webStatusResult(@RequestBody WebStatusRequest webStatusRequest)
      throws IOException, InterruptedException {
    final SiteCheckerResponse response = new SiteChecker(httpClient).check(URI.create(webStatusRequest.getUrl()));
    return response;
  }
}
