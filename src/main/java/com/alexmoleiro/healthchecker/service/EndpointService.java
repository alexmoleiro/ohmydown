package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.healthCheck.DomainsRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.EndpointRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import org.springframework.scheduling.annotation.Scheduled;


public class EndpointService {

    private final HealthCheckerCrawler healthCheckerCrawler;
    private final EndpointRepository endpointRepository;
    private final DomainsRepository domainsRepository;

    public EndpointService(
            HealthCheckerCrawler healthCheckerCrawler,
            DomainsRepository domainsRepository,
            EndpointRepository endpointRepository) {
        this.endpointRepository = endpointRepository;
        this.healthCheckerCrawler = healthCheckerCrawler;
        this.domainsRepository = domainsRepository;

        this.domainsToEndpoints();
    }

    private void domainsToEndpoints() {
        this.domainsRepository.getDomains()
                .forEach(domain -> endpointRepository.add(new Endpoint(new HttpUrl(domain))));
    }

    @Scheduled(cron = "${cron.expression}")
    public void crawlerJob() {
        healthCheckerCrawler.run(endpointRepository.getAll());
    }

}
