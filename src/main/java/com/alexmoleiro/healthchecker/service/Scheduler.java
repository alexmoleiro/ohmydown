package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.healthCheck.DomainsRepository;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;


public class Scheduler {

  private final HealthCheckerCrawler healthCheckerCrawler;
  private List<String> domains;

  public Scheduler(HealthCheckerCrawler healthCheckerCrawler, DomainsRepository domainsRepository) {
    this.healthCheckerCrawler = healthCheckerCrawler;
    this.domains = domainsRepository.getDomains();
  }

  @Scheduled(cron = "${cron.expression}")
  public void crawlerJob() {
    healthCheckerCrawler.run(domains);
  }
}
