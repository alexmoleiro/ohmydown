package com.alexmoleiro.healthchecker.infrastructure.api;

import com.alexmoleiro.healthchecker.core.HealthCheckResultsRepository;
import com.alexmoleiro.healthchecker.core.Id;
import com.alexmoleiro.healthchecker.infrastructure.dto.HistoricResultsDto;
import com.alexmoleiro.healthchecker.infrastructure.dto.SiteResultsDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HealthCheckResultsApi {

  private final HealthCheckResultsRepository healthCheckResultsRepository;

  public HealthCheckResultsApi(HealthCheckResultsRepository healthCheckResultsRepository) {
    this.healthCheckResultsRepository = healthCheckResultsRepository;
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping(value = "/landing-list", produces = "application/json")
  SiteResultsDto webStatusResult() {
    return new SiteResultsDto(healthCheckResultsRepository.getTimedResults());
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @GetMapping(value = "/historical/{id}", produces = "application/json")
  List<HistoricResultsDto> historic(@PathVariable String id) {
    return new HistoricResultsDto(healthCheckResultsRepository.getResponses(new Id(id))).list();
  }
}
