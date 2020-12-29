package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import com.alexmoleiro.healthchecker.service.HealthCheckerCrawler;
import com.alexmoleiro.healthchecker.service.Scheduler;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest
class SchedulerTest {

    public static final int ONCE = 1;
    public static final int TIMEOUT = 5_000;

    @MockBean
    HealthCheckerCrawler healthCheckerCrawler;

    @Autowired
    Scheduler scheduler;

    @Captor
    ArgumentCaptor<Set<Endpoint>> endpointsCaptor;

    @Test
    void shouldCallRun() {
        verify(healthCheckerCrawler, timeout(TIMEOUT).atLeast(ONCE)).run(anySet());
    }

    @Test
    void shouldAddEnpointToTheList() {
        scheduler.add(new Endpoint(new HttpUrl("www.added.com")));
        scheduler.add(new Endpoint(new HttpUrl("www.added.com")));

        verify(healthCheckerCrawler, timeout(TIMEOUT).atLeast(ONCE)).run(endpointsCaptor.capture());

        assertThat(endpointsCaptor.getValue()
                .stream().filter(x -> x.getUrl().toString().equals("http://www.added.com")).findFirst().get()
                .getUrl().toString()).isEqualTo("http://www.added.com");

        assertThat(endpointsCaptor.getValue()
                .stream().filter(x -> x.getUrl().toString().equals("http://www.added.com"))
                .collect(Collectors.toList()).size()).isEqualTo(1);


    }
}