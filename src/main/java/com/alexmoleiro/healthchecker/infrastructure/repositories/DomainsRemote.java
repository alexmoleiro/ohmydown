package com.alexmoleiro.healthchecker.infrastructure.repositories;

import com.alexmoleiro.healthchecker.core.DomainsRepository;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.jsoup.Jsoup.connect;

public class DomainsRemote implements DomainsRepository {

  public static final String URL =
      "https://raw.githubusercontent.com/alexmoleiro/sitechecker/master/sites/domains-english.md";

  @Override
  public List<String> getDomains() {
    List<String> list = null;
    try {
      list = stream(connect(URL).get().body().html().split(" ")).sequential().collect(toList());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return list;
  }
}
