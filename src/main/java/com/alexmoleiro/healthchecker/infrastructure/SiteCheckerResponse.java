package com.alexmoleiro.healthchecker.infrastructure;

public class SiteCheckerResponse {

  private SiteStatus status;
  private String url;
  private long delay;

  public SiteCheckerResponse(SiteStatus status, long delay, String url) {
      this.status = status;
      this.delay = delay;
      this.url = url;
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
