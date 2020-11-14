package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.infrastructure.WebStatusRequestDto;

import java.net.URI;

import static java.net.URI.create;

public class WebStatusRequest {
  private final WebStatusRequestDto webStatusRequestDto;

  public WebStatusRequest(WebStatusRequestDto webStatusRequestDto) {
    this.webStatusRequestDto = webStatusRequestDto;
  }

  public URI getUri() {
    return create(webStatusRequestDto.getUrl());
  }
}
