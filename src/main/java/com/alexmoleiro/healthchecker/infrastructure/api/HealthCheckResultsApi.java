package com.alexmoleiro.healthchecker.infrastructure.api;

import com.alexmoleiro.healthchecker.core.HealthCheckResultsRepository;
import com.alexmoleiro.healthchecker.core.Id;
import com.alexmoleiro.healthchecker.core.TimedHealthCheckResponses;
import com.alexmoleiro.healthchecker.infrastructure.dto.HistoricResultsDto;
import com.alexmoleiro.healthchecker.infrastructure.dto.SiteResultsDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class HealthCheckResultsApi {

  private final HealthCheckResultsRepository healthCheckResultsRepository;

  public HealthCheckResultsApi(HealthCheckResultsRepository healthCheckResultsRepository) {
    this.healthCheckResultsRepository = healthCheckResultsRepository;
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping(value = "/landing-list", produces = "application/json")
  SiteResultsDto webStatusResult() {
    return new SiteResultsDto(healthCheckResultsRepository.getSiteResults());
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @GetMapping(value = "/historical", produces = "application/json")
  List<HistoricResultsDto> historic() {
    final TimedHealthCheckResponses responses =
        healthCheckResultsRepository.getResponses(new Id("www.a.com"));
    return responses.getHealthCheckResponse().stream()
        .map(
            response ->
                new HistoricResultsDto(
                    response.getUrl(), response.getDelay(), response.getStatus(), response.getTime()))
        .collect(toList());
  }
}
