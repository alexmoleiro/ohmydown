package com.alexmoleiro.healthchecker.core.healthCheck;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.Duration.between;

public class HealthCheckResponse {

  private final int statusCode;
  private final LocalDateTime time;
  private HttpUrl url;
  private Duration delay;

  public HealthCheckResponse(HttpUrl url, int statusCode, LocalDateTime before, LocalDateTime now) {
    this.url = url;
    this.delay = between(before, now);
    this.statusCode = statusCode;
    this.time = now;
  }

  public int getStatus() {
    return statusCode;
  }

  public String getUrl() {
    return url.toString();
  }

  public long getDelay() {
    return delay.toMillis();
  }

  public LocalDateTime getTime() {
    return time;
  }

  @Override
  public String toString() {
    return "{" +
        "url='" + url + '\'' +
        ", status=" + statusCode +
        ", delay=" + delay.toMillis() +
        '}';
  }
}
