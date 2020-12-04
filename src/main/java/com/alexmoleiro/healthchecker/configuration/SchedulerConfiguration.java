package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.service.CheckStatusCrawler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;
import static java.util.stream.Collectors.toList;

@Configuration
@EnableScheduling
public class SchedulerConfiguration {

  public static final String PATH_TO_FILE =
      "/Users/alejandro.moleiro/Idea/sitechecker/sites/domains-english.md";
  private final CheckStatusCrawler checkStatusCrawler;

  public SchedulerConfiguration(CheckStatusCrawler checkStatusCrawler) {
    this.checkStatusCrawler = checkStatusCrawler;
  }

  @Scheduled(cron = "${cron.expression}")
  public void crawlerJob() throws IOException {
    final List<String> domains = lines(of(PATH_TO_FILE)).collect(toList());
    ConcurrentLinkedDeque<String> queueDomains = new ConcurrentLinkedDeque<>(domains);
    checkStatusCrawler.run(queueDomains);
  }
}
