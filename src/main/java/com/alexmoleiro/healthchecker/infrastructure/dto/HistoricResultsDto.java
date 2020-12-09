package com.alexmoleiro.healthchecker.infrastructure.dto;

import com.alexmoleiro.healthchecker.core.TimedHealthCheckResponses;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class HistoricResultsDto {

  private TimedHealthCheckResponses responses;
  private URL url;
  private long delay;
  private long status;
  private LocalDateTime time;

  public HistoricResultsDto(TimedHealthCheckResponses responses) {
    this.responses = responses;
  }

  private HistoricResultsDto(URL url, long delay, long status, LocalDateTime time) {
    this.url = url;
    this.delay = delay;
    this.status = status;
    this.time = time;
  }

  public List<HistoricResultsDto> list() {
    return responses.getHealthCheckResponse().stream()
        .map(
            response ->
                new HistoricResultsDto(
                    response.getUrl(),
                    response.getDelay(),
                    response.getStatus(),
                    response.getTime()))
        .collect(toList());
  }

  public String getUrl() {
    return url.toString();
  }

  public long getDelay() {
    return delay;
  }

  public long getStatus() {
    return status;
  }

  public String getTime() {
    return time.toString();
  }
}
