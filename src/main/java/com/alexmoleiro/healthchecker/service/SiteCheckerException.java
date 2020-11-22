package com.alexmoleiro.healthchecker.service;

import java.io.IOException;

public class SiteCheckerException extends RuntimeException {

  private final IOException e;

  public SiteCheckerException(IOException e) {
    this.e = e;
  }

  @Override
  public String getMessage() {
    return e.getMessage();
  }

}