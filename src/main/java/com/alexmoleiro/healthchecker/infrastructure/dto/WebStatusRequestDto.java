package com.alexmoleiro.healthchecker.infrastructure.dto;

public class WebStatusRequestDto {

  private  String url;

  public WebStatusRequestDto() {
  }

  public WebStatusRequestDto(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

}
