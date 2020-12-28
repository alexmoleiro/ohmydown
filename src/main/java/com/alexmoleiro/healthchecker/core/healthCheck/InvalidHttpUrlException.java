package com.alexmoleiro.healthchecker.core.healthCheck;

public class InvalidHttpUrlException extends RuntimeException {
  private final String url;
  private final String message;

  public InvalidHttpUrlException(String url, String message) {
    this.url = url;
    this.message = message;
  }

  @Override
  public String toString() {
    return """
        {"url":"%s","message":"%s"}""".formatted(url, message);
  }
}
