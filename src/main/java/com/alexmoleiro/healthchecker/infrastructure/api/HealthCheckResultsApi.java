package com.alexmoleiro.healthchecker.infrastructure.api;

import com.alexmoleiro.healthchecker.core.healthCheck.EndpointRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.infrastructure.dto.HistoricResultsDto;
import com.alexmoleiro.healthchecker.infrastructure.dto.SiteResultsDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.alexmoleiro.healthchecker.core.healthCheck.EndpointType.LANDING;
import static com.alexmoleiro.healthchecker.infrastructure.dto.HistoricResultsDto.list;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@RestController
public class HealthCheckResultsApi {

  private final HealthCheckRepository healthCheckRepository;
  private final EndpointRepository endpointRepository;

  public HealthCheckResultsApi(
      HealthCheckRepository healthCheckRepository, EndpointRepository endpointRepository) {
    this.healthCheckRepository = healthCheckRepository;
    this.endpointRepository = endpointRepository;
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping(value = "/landing-list", produces = "application/json")
  SiteResultsDto webStatusResult() {
    return new SiteResultsDto(
        healthCheckRepository.getResponses().stream()
            .filter(response -> response.getEndpoint().getEndpointType().equals(LANDING))
            .collect(toList()));
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @GetMapping(value = "/historical/{id}", produces = "application/json")
  List<HistoricResultsDto> historical(@PathVariable String id) {
    return endpointRepository
        .get(id)
        .map(e -> list(healthCheckRepository.getResponses(e)))
        .orElse(emptyList());
  }
}
