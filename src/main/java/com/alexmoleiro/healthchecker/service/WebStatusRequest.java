package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.infrastructure.WebStatusRequestDto;

import java.net.MalformedURLException;
import java.net.URL;

public class WebStatusRequest {
  private final WebStatusRequestDto webStatusRequestDto;

  public WebStatusRequest(WebStatusRequestDto webStatusRequestDto) {
    this.webStatusRequestDto = webStatusRequestDto;
  }

  public URL getUrl() throws MalformedURLException {
    return new URL(webStatusRequestDto.getUrl());
  }
}
