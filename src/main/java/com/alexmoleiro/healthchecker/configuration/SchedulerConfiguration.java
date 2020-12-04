package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.service.CheckStatusCrawler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfiguration {

  private final CheckStatusCrawler checkStatusCrawler;

  public SchedulerConfiguration(CheckStatusCrawler checkStatusCrawler) {
    this.checkStatusCrawler = checkStatusCrawler;
  }

  @Scheduled(cron = "${cron.expression}")
  public void run() {
    checkStatusCrawler.run();
  }

}
