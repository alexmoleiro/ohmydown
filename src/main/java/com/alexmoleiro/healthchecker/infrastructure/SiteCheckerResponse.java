package com.alexmoleiro.healthchecker.infrastructure;


public class SiteCheckerResponse {

  private final int statusCode;
  private String url;
  private long delay;


  public SiteCheckerResponse(String url, int statusCode, long delay) {
    this.url = url;
    this.delay = delay;
    this.statusCode = statusCode;
  }

  public int getStatus() {
    return statusCode;
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
        ", status=" + statusCode +
        ", delay=" + delay +
        '}';
  }
}
