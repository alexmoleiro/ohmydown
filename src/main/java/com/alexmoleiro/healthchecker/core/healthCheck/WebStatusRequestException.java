package com.alexmoleiro.healthchecker.core.healthCheck;

public class WebStatusRequestException extends RuntimeException {
  private final String url;
  private final String message;

  public WebStatusRequestException(String url, String message) {
    this.url = url;
    this.message = message;
  }

  @Override
  public String toString() {
    return """
        {"url":"%s","message":"%s"}""".formatted(url, message);
  }
}
