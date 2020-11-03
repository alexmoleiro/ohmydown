package com.alexmoleiro.healthchecker.infrastructure;

public class WebStatusResponse {

  private String status;
  private String url;
  private Integer delay;


  public WebStatusResponse(WebStatusRequest webStatusRequest) {
    this.url = webStatusRequest.getUrl();
    this.delay = 200;
    this.status = "UP";
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
