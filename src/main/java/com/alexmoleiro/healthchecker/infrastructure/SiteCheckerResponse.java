package com.alexmoleiro.healthchecker.infrastructure;


import java.net.http.HttpResponse;

public class SiteCheckerResponse {

  private String url;
  private final HttpResponse<Void> response;
  private long delay;

  public SiteCheckerResponse(HttpResponse<Void> response, long delay) {
    this.response = response;
    this.delay = delay;
    this.url = response.uri().toString();
  }

  public int getStatus() {
    return response.statusCode();
  }

  public String getUrl() {
    return url;
  }

  public long getDelay() {
    return delay;
  }

  @Override
  public String toString() {
    return "{" +
        "url='" + url + '\'' +
        ", status=" + getStatus() +
        ", delay=" + delay +
        '}';
  }
}
