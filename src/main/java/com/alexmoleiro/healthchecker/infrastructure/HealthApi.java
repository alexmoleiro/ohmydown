package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.core.WebStatusRequestException;
import com.alexmoleiro.healthchecker.service.SiteChecker;
import com.alexmoleiro.healthchecker.service.SiteCheckerException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.REQUEST_TIMEOUT;

@RestController
public class HealthApi {

  private final SiteChecker siteChecker;

  public HealthApi(SiteChecker siteChecker) {
    this.siteChecker = siteChecker;
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping("/status")
  SiteCheckerResponse webStatusResult(@RequestBody WebStatusRequestDto webStatusRequestDto)
      throws InterruptedException, URISyntaxException {
    return siteChecker.check(new WebStatusRequest(webStatusRequestDto));
  }

  @ResponseStatus(value= BAD_REQUEST)
  @ExceptionHandler(WebStatusRequestException.class)
  public String conflict(WebStatusRequestException e) {
    return e.toString();
  }

  @ResponseStatus(value= REQUEST_TIMEOUT)
  @ExceptionHandler(SiteCheckerException.class)
  public void timeout() {
  }
}