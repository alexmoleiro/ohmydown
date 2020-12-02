package com.alexmoleiro.healthchecker.infrastructure;

import java.net.http.HttpResponse;

import static com.alexmoleiro.healthchecker.infrastructure.SiteStatus.DOWN;
import static com.alexmoleiro.healthchecker.infrastructure.SiteStatus.UP;

public class SiteCheckerResponse {

  private SiteStatus status;
  private String url;
  private long delay;

  public SiteCheckerResponse(SiteStatus status, long delay, String url) {
    this.status = status;
    this.delay = delay;
    this.url = url;
  }

  public SiteCheckerResponse(HttpResponse<Void> response, long delay) {
    this.status = (response.statusCode() > 200) ? DOWN : UP;
    this.delay = delay;
    this.url = response.uri().toString();
  }

  public SiteStatus getStatus() {
    return status;
  }

  public String getUrl() {
    return url;
  }

  public long getDelay() {
    return delay;
  }
}
