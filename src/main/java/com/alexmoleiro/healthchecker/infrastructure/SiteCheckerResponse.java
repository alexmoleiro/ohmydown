package com.alexmoleiro.healthchecker.infrastructure;

public class SiteCheckerResponse {

  private String status;
  private String url;
  private long delay;

  public SiteCheckerResponse(String status, long delay, String url) {
      this.status = status;
      this.delay = delay;
      this.url = url;
  }

  public String getStatus() {
    return status;
  }

  public String getUrl() {
    return url;
  }

  public long getDelay() {
    return delay;
  }
}
