package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.service.HealthCheckerCrawler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.jsoup.Jsoup.connect;

@Configuration
@EnableScheduling
public class SchedulerConfiguration {

  private final HealthCheckerCrawler healthCheckerCrawler;
  private List<String> domains;
  public static final String URL =
      "https://raw.githubusercontent.com/alexmoleiro/sitechecker/master/sites/domains-english.md";

  public SchedulerConfiguration(HealthCheckerCrawler healthCheckerCrawler) throws IOException {
    this.healthCheckerCrawler = healthCheckerCrawler;
    this.domains =
        stream(connect(URL).get().body().html().split(" ")).sequential().collect(toList());
  }

  @Scheduled(cron = "${cron.expression}")
  public void crawlerJob() {
    healthCheckerCrawler.run(new ConcurrentLinkedDeque<>(domains));
  }
}
