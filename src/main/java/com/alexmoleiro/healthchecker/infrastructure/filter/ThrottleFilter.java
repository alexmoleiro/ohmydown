package com.alexmoleiro.healthchecker.infrastructure.filter;

import io.github.bucket4j.Bucket;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

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

@Component
public class ThrottleFilter implements Filter {

  public static final int DAILY_TOKENS = 2000;
  public static final int HOURLY_TOKENS = 120;
  public static final int ONE = 1;
  private Map<String, Bucket> minuteBucket = new HashMap<>();
  private Map<String, Bucket> dayBucket = new HashMap<>();

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    final String remoteAddr = httpRequest.getRemoteAddr();

    minuteBucket.computeIfAbsent(
        remoteAddr,
        k ->
            builder()
                .addLimit(classic(HOURLY_TOKENS, intervally(HOURLY_TOKENS, ofMinutes(ONE))))
                .build());
    dayBucket.computeIfAbsent(
        remoteAddr,
        k ->
            builder()
                .addLimit(classic(DAILY_TOKENS, intervally(DAILY_TOKENS, ofDays(ONE))))
                .build());

    final Bucket mBucket = minuteBucket.get(remoteAddr);
    final Bucket dBucket = dayBucket.get(remoteAddr);

    if (mBucket.tryConsume(ONE) && dBucket.tryConsume(ONE)) {
      chain.doFilter(request, response);
    } else {
      HttpServletResponse httpResponse = (HttpServletResponse) response;
      httpResponse.setContentType("text/plain");
      httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
      httpResponse.getWriter().append("Too many requests");
    }
  }
}
