package com.alexmoleiro.healthchecker.core;


import java.net.URL;

public class HealthCheckResponse {

  private final int statusCode;
  private URL url;
  private long delay;


  public HealthCheckResponse(URL url, int statusCode, long delay) {
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
    return delay;
  }

  @Override
  public String toString() {
    return "{" +
        "url='" + url + '\'' +
        ", status=" + statusCode +
        ", delay=" + delay +
        '}';
  }
}
