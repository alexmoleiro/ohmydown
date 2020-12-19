package com.alexmoleiro.healthchecker.core.healthCheck;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.Duration.between;

public class HealthCheckResponse {

  private final int statusCode;
  private final LocalDateTime time;
  private URL url;
  private Duration delay;

  public HealthCheckResponse(URL url, int statusCode, LocalDateTime before, LocalDateTime now) {
    this.url = url;
    this.delay = between(before, now);
    this.statusCode = statusCode;
    this.time = now;
  }

  public int getStatus() {
    return statusCode;
  }

  public URL getUrl() {
    return url;
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
