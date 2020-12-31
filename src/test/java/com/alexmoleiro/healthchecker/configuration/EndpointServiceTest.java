package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import com.alexmoleiro.healthchecker.service.HealthCheckerCrawler;
import com.alexmoleiro.healthchecker.service.EndpointService;
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
class EndpointServiceTest {

    private static final int ONCE = 1;
    private static final int TIMEOUT = 5_000;
    private String URL_STRING = "http://www.added.com";

    @MockBean
    HealthCheckerCrawler healthCheckerCrawler;

    @Autowired
    EndpointService endpointService;

    @Captor
    ArgumentCaptor<Set<Endpoint>> endpointsCaptor;

    @Test
    void shouldCallRun() {
        verify(healthCheckerCrawler, timeout(TIMEOUT).atLeast(ONCE)).run(anySet());
    }

    @Test
    void shouldAddEndpointToTheList() {

        endpointService.add(new Endpoint(new HttpUrl(URL_STRING)));
        endpointService.add(new Endpoint(new HttpUrl(URL_STRING)));

        verify(healthCheckerCrawler, timeout(TIMEOUT).atLeast(ONCE)).run(endpointsCaptor.capture());

        assertThat(endpointsCaptor.getValue()
                .stream().filter(endpoint -> endpoint.getUrl().toString().equals(URL_STRING)).findFirst().get()
                .getUrl().toString())
                .isEqualTo(URL_STRING);

        assertThat(endpointsCaptor.getValue()
                .stream().filter(x -> x.getUrl().toString().equals(URL_STRING))
                .collect(Collectors.toList()).size())
                .isEqualTo(1);

    }
}