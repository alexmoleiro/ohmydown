package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.service.CheckDaemon;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfiguration {

  private final CheckDaemon checkDaemon;

  public SchedulerConfiguration(CheckDaemon checkDaemon) {
    this.checkDaemon = checkDaemon;
  }

  @Scheduled(cron = "${cron.expression}")
  public void run() {
    checkDaemon.run();
  }

}
