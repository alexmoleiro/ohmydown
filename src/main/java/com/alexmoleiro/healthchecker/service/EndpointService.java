package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.healthCheck.DomainsRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;


public class EndpointService {

  private final HealthCheckerCrawler healthCheckerCrawler;
  private Map<UUID,Endpoint> endpoints;

  public EndpointService(HealthCheckerCrawler healthCheckerCrawler, DomainsRepository domainsRepository) {
    this.healthCheckerCrawler = healthCheckerCrawler;
    endpoints = domainsRepository.getDomains()
            .stream()
            .map(domain -> new Endpoint(new HttpUrl(domain)))
            .collect(toMap(s->UUID.fromString(s.getId()), s->s));
  }

  @Scheduled(cron = "${cron.expression}")
  public void crawlerJob() {
    healthCheckerCrawler.run(endpoints.values().stream().collect(toSet()));
  }

  public void add(Endpoint endpoint) {
      endpoints.put(UUID.randomUUID(), endpoint);
  }
}
