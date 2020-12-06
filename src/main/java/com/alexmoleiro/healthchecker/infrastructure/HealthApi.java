package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.HealthChecker;
import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.core.WebStatusRequestException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
public class HealthApi {

  private final HealthChecker healthChecker;

  public HealthApi(HealthChecker healthChecker) {
    this.healthChecker = healthChecker;
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping("/status")
  SiteCheckerResponse webStatusResult(@RequestBody WebStatusRequestDto webStatusRequestDto) {
    return healthChecker.check(new WebStatusRequest(webStatusRequestDto.getUrl()));
  }

  @ResponseStatus(value= BAD_REQUEST)
  @ExceptionHandler(WebStatusRequestException.class)
  public String invalidDomainNames(WebStatusRequestException e) {
    return e.toString();
  }

}