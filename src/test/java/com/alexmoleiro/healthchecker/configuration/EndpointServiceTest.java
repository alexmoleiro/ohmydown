package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.service.EndpointService;
import com.alexmoleiro.healthchecker.service.HealthCheckerCrawler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest
class EndpointServiceTest {

    private static final int ONCE = 1;
    private static final int TIMEOUT = 5_000;

    @MockBean
    HealthCheckerCrawler healthCheckerCrawler;

    @Autowired
    EndpointService endpointService;

    @Test
    void shouldCallRun() {
        verify(healthCheckerCrawler, timeout(TIMEOUT).atLeast(ONCE)).run(anySet());
    }

}