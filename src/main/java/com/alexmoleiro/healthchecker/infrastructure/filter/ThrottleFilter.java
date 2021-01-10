package com.alexmoleiro.healthchecker.infrastructure.filter;

import io.github.bucket4j.Bucket;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.github.bucket4j.Bandwidth.classic;
import static io.github.bucket4j.Bucket4j.builder;
import static io.github.bucket4j.Refill.intervally;
import static java.time.Duration.ofDays;
import static java.time.Duration.ofMinutes;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

public class ThrottleFilter implements Filter {

  private static final int ONE = 1;
  private final int dailyTokens;
  private final int hourlyTokens;
  private Map<String, Bucket> minuteBucket = new HashMap<>();
  private Map<String, Bucket> dayBucket = new HashMap<>();

  public ThrottleFilter(int dailyTokens, int hourlyTokens) {

    this.dailyTokens = dailyTokens;
    this.hourlyTokens = hourlyTokens;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    final String remoteAddr = httpRequest.getRemoteAddr();

    minuteBucket.computeIfAbsent(
        remoteAddr,
        k ->
            builder()
                .addLimit(classic(hourlyTokens, intervally(hourlyTokens, ofMinutes(ONE))))
                .build());
    dayBucket.computeIfAbsent(
        remoteAddr,
        k ->
            builder()
                .addLimit(classic(dailyTokens, intervally(dailyTokens, ofDays(ONE))))
                .build());

    final Bucket mBucket = minuteBucket.get(remoteAddr);
    final Bucket dBucket = dayBucket.get(remoteAddr);

    if (mBucket.tryConsume(ONE) && dBucket.tryConsume(ONE)) {
      chain.doFilter(request, response);
    } else {
      HttpServletResponse httpResponse = (HttpServletResponse) response;
      httpResponse.setContentType("text/plain");
      httpResponse.setStatus(TOO_MANY_REQUESTS.value());
      httpResponse.getWriter().append("Too many requests");
    }
  }
}
