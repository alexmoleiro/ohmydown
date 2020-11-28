package com.alexmoleiro.healthchecker.scraper;

import com.alexmoleiro.healthchecker.core.WebStatusRequest;
import com.alexmoleiro.healthchecker.infrastructure.SiteCheckerResponse;
import com.alexmoleiro.healthchecker.infrastructure.WebStatusRequestDto;
import com.alexmoleiro.healthchecker.service.SiteChecker;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLHandshakeException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.URISyntaxException;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static java.net.http.HttpClient.Redirect.ALWAYS;
import static java.net.http.HttpClient.newBuilder;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;
import static java.util.stream.Collectors.toList;

public class ScraperTest {
  // @Test
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
        lines(of("/Users/alejandro.moleiro/Idea/sitechecker/domains-english.md")).collect(toList());

    final AtomicInteger index = new AtomicInteger(-1); // https://mimoYmima.com"
    int cursor = index.get();

    final SiteChecker siteChecker =
        new SiteChecker(newBuilder().followRedirects(ALWAYS).build(), Duration.ofSeconds(5));

    final int nThreads = 10;
    ExecutorService executor = Executors.newFixedThreadPool(nThreads);

    IntStream.rangeClosed(1, nThreads)
        .forEach(x -> executor.execute(() -> navegar(domains, index, cursor, siteChecker)));

    Thread.sleep(Duration.ofMinutes(2).toMillis());
  }

  private void navegar(
      List<String> domains, AtomicInteger index, int cursor, SiteChecker siteChecker) {

    while (1 == 1) {
      String domain = null;
      // final String name = Thread.currentThread().getName();
      // System.out.println(name);
      try {
        cursor = index.addAndGet(1) % domains.size();
        domain = domains.get(cursor);
        final SiteCheckerResponse result =
            siteChecker.check(new WebStatusRequest(new WebStatusRequestDto(domain)));
        System.out.println(
            cursor
                + "\t\t"
                + result.getDelay()
                + "\t\t\t\t"
                + result.getUrl()
                + "\t\t"
                + result.getStatus());
      } catch (ConnectException | URISyntaxException | SSLHandshakeException e) {
        System.out.println("varios " + domain);
      } catch (HttpTimeoutException e) {
        System.out.println("Timeout https://" + domain);
      } catch (IOException | InterruptedException e) {
        System.out.println("Freak " + domain);
      }
    }
  }
}
