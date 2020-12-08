package com.alexmoleiro.healthchecker.infrastructure.dto;

import java.net.URL;
import java.time.LocalDateTime;

public class HistoricResultsDto {

  private final URL url;
  private final long delay;
  private final long status;
  private final LocalDateTime time;

  public HistoricResultsDto(URL url, long delay, long status, LocalDateTime time) {
    this.url = url;
    this.delay = delay;
    this.status = status;
    this.time = time;
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

  public String time() {
    return time.toString();
  }

}
