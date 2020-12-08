package com.alexmoleiro.healthchecker.core;

import java.util.Random;

public enum UserAgent {
  WIN10("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36"),
  FIREFOX("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:82.0) Gecko/20100101 Firefox/82.0"),
  IPHONE("Mozilla/5.0 (iPhone; CPU iPhone OS 12_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148"),
  SAFARI("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.1 Safari/605.1.15"),
  EDGE("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36 Edg/86.0.622.69");


  private final String string;

  UserAgent(String string) {

    this.string = string;
  }

  public static String random() {
    final int length = values().length;
    return values()[new Random().nextInt(length)].getValue();
  }

  private String getValue() {
    return string;
  }
}