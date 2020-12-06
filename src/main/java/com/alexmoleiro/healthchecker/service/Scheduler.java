package com.alexmoleiro.healthchecker.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.jsoup.Jsoup.connect;

@Service
@EnableScheduling
public class Scheduler {

  private final HealthCheckerCrawler healthCheckerCrawler;
  private List<String> domains;
  public static final String URL =
      "https://raw.githubusercontent.com/alexmoleiro/sitechecker/master/sites/domains-english.md";

  public Scheduler(HealthCheckerCrawler healthCheckerCrawler) throws IOException {
    this.healthCheckerCrawler = healthCheckerCrawler;
    this.domains =
        stream(connect(URL).get().body().html().split(" ")).sequential().collect(toList());
  }

  @Scheduled(cron = "${cron.expression}")
  public void crawlerJob() {
    healthCheckerCrawler.run(domains);
  }
}
