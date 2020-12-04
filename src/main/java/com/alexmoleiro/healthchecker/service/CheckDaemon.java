package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.configuration.SchedulerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;

public class CheckDaemon {
  private static final String PATTERN = "yyyyMMdd-HHmmss";
  private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerConfiguration.class);

  public void run() {
    final String format = now(Clock.systemUTC()).format(ofPattern(PATTERN));
    LOGGER.info("hola " + format);
  }
}
