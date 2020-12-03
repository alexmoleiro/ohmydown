package com.alexmoleiro.healthchecker.scraper;

import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.infrastructure.SiteCheckerResponse;
import com.alexmoleiro.healthchecker.infrastructure.WebStatusRequestDto;
import com.alexmoleiro.healthchecker.service.HttpChecker;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Disabled;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;

import static java.net.http.HttpClient.Redirect.ALWAYS;
import static java.net.http.HttpClient.newBuilder;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;
import static java.time.Duration.ofSeconds;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;

public class ScraperTest {

  @Disabled
  void shouldDownload() throws IOException {

    FileWriter fileWriter = new FileWriter("domains-alabrent.txt");
    PrintWriter printWriter = new PrintWriter(fileWriter);

    final HashSet<String> domains = new HashSet<>();
    //
    String[][] paginas = {
      {"https://alabrent.com/directorio/diseno-grafico/?page=", "54"},
      {"https://alabrent.com/directorio/agencias-de-publicidad/?page=", "11"},
      {"https://alabrent.com/directorio/impresion-serigrafica-y-tampografia/?page=", "78"},
      {"https://alabrent.com/directorio/edicion/?page=", "128"}
    };

    for (int j = 0; j < paginas.length; j++) {
      for (int i = 1; i < Integer.valueOf(paginas[j][1]); i++) {
        Document doc = Jsoup.connect(paginas[j][0] + i).get();
        final Elements inline = doc.getElementsByClass("entry-meta clearfix").select("a");
        inline.stream().forEach(x -> domains.add(x.attr("href")));
      }

      domains.stream().forEach(x -> printWriter.println(x));
      printWriter.close();
    }
  }

  @Disabled
  void shouldTestOneThousand() throws IOException, InterruptedException {
    final List<String> domains =
        lines(of("/Users/alejandro.moleiro/Idea/sitechecker/sites/domains-english.md"))
            .collect(toList());

    final int timeout = 25;
    final int nThreads = 40;
    final ConcurrentLinkedDeque<String> domainsQueue = new ConcurrentLinkedDeque<>(domains);
    ExecutorService executor = newFixedThreadPool(nThreads);
    final var httpChecker =
        new HttpChecker(newBuilder().followRedirects(ALWAYS).build(), ofSeconds(timeout));

    rangeClosed(1, nThreads)
        .forEach(x -> runAsync(() -> checkDomain(httpChecker, domainsQueue), executor));

    Thread.sleep(90_000);
  }

  private void checkDomain(HttpChecker httpChecker, ConcurrentLinkedDeque<String> domainsQueue) {
    while (domainsQueue.peek() != null) {
      final SiteCheckerResponse check =
          httpChecker.check(new WebStatusRequest(new WebStatusRequestDto(domainsQueue.poll())));
      System.out.println(check);
    }
  }
}
