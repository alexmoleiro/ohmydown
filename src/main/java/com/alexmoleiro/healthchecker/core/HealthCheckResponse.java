package com.alexmoleiro.healthchecker.core;


import java.net.URL;
import java.time.Duration;

public class HealthCheckResponse {

  private final int statusCode;
  private URL url;
  private Duration delay;


  public HealthCheckResponse(URL url, int statusCode, Duration delay) {
    this.url = url;
    this.delay = delay;
    this.statusCode = statusCode;
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

  @Override
  public String toString() {
    return "{" +
        "url='" + url + '\'' +
        ", status=" + statusCode +
        ", delay=" + delay.toMillis() +
        '}';
  }
}
