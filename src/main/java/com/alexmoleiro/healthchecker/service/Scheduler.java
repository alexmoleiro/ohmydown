package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.DomainsRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableScheduling
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
