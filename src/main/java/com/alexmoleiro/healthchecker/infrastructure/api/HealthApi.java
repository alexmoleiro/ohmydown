package com.alexmoleiro.healthchecker.infrastructure.api;

import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthChecker;
import com.alexmoleiro.healthchecker.core.healthCheck.InvalidHttpUrlException;
import com.alexmoleiro.healthchecker.infrastructure.dto.HealthCheckResponseDto;
import com.alexmoleiro.healthchecker.infrastructure.dto.UrlDto;
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
  HealthCheckResponseDto healthCheck(@RequestBody UrlDto urlDto) {
    return new HealthCheckResponseDto(healthChecker.check(new HttpUrl(urlDto.getUrl())));
  }

  @ResponseStatus(value= BAD_REQUEST)
  @ExceptionHandler(InvalidHttpUrlException.class)
  public String invalidDomainNames(InvalidHttpUrlException e) {
    return e.toString();
  }

}