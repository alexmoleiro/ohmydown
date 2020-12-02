package com.alexmoleiro.healthchecker.scraper;

import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.infrastructure.SiteCheckerResponse;
import com.alexmoleiro.healthchecker.infrastructure.WebStatusRequestDto;
import com.alexmoleiro.healthchecker.service.HttpChecker;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLHandshakeException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.URISyntaxException;
import java.net.http.HttpTimeoutException;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;
import static java.net.http.HttpClient.Redirect.ALWAYS;
import static java.net.http.HttpClient.newBuilder;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;
import static java.time.Duration.ofMinutes;
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

  @Test
  void shouldUpdateAtomicInteger() throws IOException, URISyntaxException, InterruptedException {
    final List<String> domains =
        lines(of("/Users/alejandro.moleiro/Idea/sitechecker/sites/domains-english.md")).collect(toList());

    final AtomicInteger index = new AtomicInteger(-1);

    final int timeout = 25;
    final HttpChecker httpChecker =
        new HttpChecker(newBuilder().followRedirects(ALWAYS).build(), ofSeconds(timeout));

    final int nThreads = 40;
    ExecutorService executor = newFixedThreadPool(nThreads);

    //rangeClosed(1, nThreads).forEach(x -> executor.execute(() -> navegar(domains, index, siteChecker)));

    rangeClosed(1, nThreads).forEach(x->runAsync(() -> navegar(domains, index, httpChecker), executor));
    sleep(ofMinutes(3).toMillis());
  }

  private void navegar(List<String> domains, AtomicInteger index, HttpChecker httpChecker) {

    while (1 == 1) {
      String domain = null;
      try {
        int cursor = index.addAndGet(1) % domains.size();
        domain = domains.get(cursor);
        final SiteCheckerResponse result =
            httpChecker.check(new WebStatusRequest(new WebStatusRequestDto(domain)));
        System.out.println(result);
      } catch (ConnectException e) {
        System.out.println("Connection error " + domain + " " + e.getMessage());
      } catch (SSLHandshakeException e) {
        System.out.println("SSL exception " + domain);
      } catch (HttpTimeoutException e) {
        System.out.println("Timeout https://" + domain);
      } catch (IOException e) {
        System.out.println("IOException " + domain + " " + e.getMessage());
      } catch (InterruptedException e) {
        System.out.println("Interrupted " + domain);
      }
    }
  }
}
