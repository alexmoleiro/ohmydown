package com.alexmoleiro.healthchecker.infrastructure.api;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import com.alexmoleiro.healthchecker.infrastructure.dto.HistoricResultsDto;
import com.alexmoleiro.healthchecker.infrastructure.dto.SiteResultsDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.alexmoleiro.healthchecker.infrastructure.dto.HistoricResultsDto.list;

@RestController
public class HealthCheckResultsApi {

  private final HealthCheckRepository healthCheckRepository;

  public HealthCheckResultsApi(HealthCheckRepository healthCheckRepository) {
    this.healthCheckRepository = healthCheckRepository;
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping(value = "/landing-list", produces = "application/json")
  SiteResultsDto webStatusResult() {
    return new SiteResultsDto(healthCheckRepository.getResponses());
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @GetMapping(value = "/historical/{id}", produces = "application/json")
  List<HistoricResultsDto> historic(@PathVariable String id) {
    return list(healthCheckRepository.getResponses(new Endpoint(new HttpUrl(id))));
  }
}
