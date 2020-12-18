package com.alexmoleiro.healthchecker.infrastructure.repositories;

import com.alexmoleiro.healthchecker.core.healthCheck.DomainsRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.jsoup.Jsoup.connect;

public class DomainsRemote implements DomainsRepository {

  public static final String URL =
      "https://raw.githubusercontent.com/alexmoleiro/sitechecker/master/src/main/resources/sites/domains-english.md";

  @Override
  public List<String> getDomains() {
    List<String> list = new ArrayList<>();
    try {
       list = stream(connect(URL).get().body().html().split(" ")).sequential().collect(toList());

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return list;
  }
}
