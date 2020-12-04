package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.service.CheckStatusCrawler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.util.List.of;

@Configuration
@EnableScheduling
public class SchedulerConfiguration {

  private final CheckStatusCrawler checkStatusCrawler;

  public SchedulerConfiguration(CheckStatusCrawler checkStatusCrawler) {
    this.checkStatusCrawler = checkStatusCrawler;
  }

  @Scheduled(cron = "${cron.expression}")
  public void crawlerJob() {
    final List<String> domains = of("www.alexmoleiro.com", "www.yavendras.com");
    ConcurrentLinkedDeque<String> queueDomains = new ConcurrentLinkedDeque<>(domains);
    final int nThreads = 5;
    checkStatusCrawler.run(queueDomains, nThreads);
  }

}
