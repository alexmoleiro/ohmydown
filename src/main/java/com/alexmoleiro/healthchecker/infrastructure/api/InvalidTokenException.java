package com.alexmoleiro.healthchecker.infrastructure.api;

public class InvalidTokenException extends RuntimeException {
  public InvalidTokenException(Exception e) {
    super(e);
  }

  public InvalidTokenException() {
  }
}