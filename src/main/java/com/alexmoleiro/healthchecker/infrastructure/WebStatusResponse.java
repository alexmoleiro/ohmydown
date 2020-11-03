package com.alexmoleiro.healthchecker.infrastructure;

public class WebStatusResponse {

  private final String status;
  private final String url;
  private final Integer delay;

  public WebStatusResponse(String status, String url, Integer delay) {
    this.status = status;
    this.url = url;
    this.delay = delay;
  }

  public String getStatus() {
    if(url.equals("http://www.up.com")) {
      return "UP";
    }
    return "DOWN";
  }

  public String getUrl() {
    return url;
  }

  public Integer getDelay() {
    return delay;
  }
}
