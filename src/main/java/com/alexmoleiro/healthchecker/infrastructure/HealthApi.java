package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.service.HttpChecker;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthApi {

  private final HttpChecker httpChecker;

  public HealthApi(HttpChecker httpChecker) {
    this.httpChecker = httpChecker;
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping("/status")
  SiteCheckerResponse webStatusResult(@RequestBody WebStatusRequestDto webStatusRequestDto) {
    return httpChecker.check(new WebStatusRequest(webStatusRequestDto));
  }
}