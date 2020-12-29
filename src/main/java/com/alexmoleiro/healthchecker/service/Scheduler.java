package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.healthCheck.DomainsRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Set;

import static java.util.stream.Collectors.toSet;


public class Scheduler {

  private final HealthCheckerCrawler healthCheckerCrawler;
  private Set<Endpoint> endpoints;

  public Scheduler(HealthCheckerCrawler healthCheckerCrawler, DomainsRepository domainsRepository) {
    this.healthCheckerCrawler = healthCheckerCrawler;
    endpoints = domainsRepository.getDomains()
            .stream()
            .map(domain -> new Endpoint(new HttpUrl(domain)))
            .collect(toSet());
  }

  @Scheduled(cron = "${cron.expression}")
  public void crawlerJob() {
    healthCheckerCrawler.run(endpoints);
  }

  public void add(Endpoint endpoint) {
      endpoints.add(endpoint);
  }
}
