package com.alexmoleiro.healthchecker.core;

public enum UserAgent {
  MOZILLA(
      "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

  private final String string;

  UserAgent(String string) {

    this.string = string;
  }

  public String getValue() {
    return string;
  }
}
