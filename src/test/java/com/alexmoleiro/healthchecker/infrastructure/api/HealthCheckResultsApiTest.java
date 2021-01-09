package com.alexmoleiro.healthchecker.infrastructure.api;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.EndpointRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.alexmoleiro.healthchecker.core.healthCheck.EndpointType.LANDING;
import static java.time.LocalDateTime.of;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class HealthCheckResultsApiTest {

    public static final String URL_STRING = "https://www.a.com";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private HealthCheckRepository healthCheckRepository;

    @Autowired
    private EndpointRepository endpointRepository;

    private static final LocalDateTime FIRST = of(2020, 12, 8, 23, 20);
    private static final LocalDateTime SECOND = of(2020, 12, 8, 23, 25);

    @Test
    void shouldReturnHistoricalValues() throws Exception {

        healthCheckRepository.deleteAll();
        Endpoint endpointA = new Endpoint(new HttpUrl("www.a.com"));

        endpointRepository.add(endpointA);

        healthCheckRepository.add(
                endpointA,
                new HealthCheckResponse(new HttpUrl(URL_STRING), OK.value(), FIRST.minusHours(1), FIRST)
        );

        healthCheckRepository.add(
                endpointA,
                new HealthCheckResponse(new HttpUrl(URL_STRING), INTERNAL_SERVER_ERROR.value(), SECOND.minusHours(1), SECOND)
        );

        this.mockMvc.perform(get("/historical/" + endpointA.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        {"url":"https://www.a.com","delay":3600000,"status":200,"time":"2020-12-08T23:20"},
                        {"url":"https://www.a.com","delay":3600000,"status":500,"time":"2020-12-08T23:25"}
                        ]"""));
    }

    @Test
    void shouldReturnEmptyListWhenNoEndpointIsFound() throws Exception {

        healthCheckRepository.deleteAll();

        String noneExistingEndpoint = UUID.randomUUID().toString();

        this.mockMvc.perform(get("/historical/" + noneExistingEndpoint))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        []"""));
    }


    @Test
    void shouldReturnLandingListSites() throws Exception {
        healthCheckRepository.deleteAll();

        HttpUrl httpUrlZ = new HttpUrl("https://www.z.com");
        Endpoint endpointZ = new Endpoint(httpUrlZ, LANDING);
        healthCheckRepository.add(endpointZ, new HealthCheckResponse(httpUrlZ, OK.value(), FIRST, SECOND));

        HttpUrl httpUrlX = new HttpUrl("https://www.x.com");
        Endpoint endpointX = new Endpoint(httpUrlX, LANDING);
        healthCheckRepository.add(endpointX, new HealthCheckResponse(httpUrlX, INTERNAL_SERVER_ERROR.value(), FIRST, SECOND
        ));

        HttpUrl httpUrlY = new HttpUrl("https://www.y.com");
        Endpoint endpointY = new Endpoint(httpUrlY);
        healthCheckRepository.add(endpointY, new HealthCheckResponse(httpUrlY, OK.value(), FIRST, SECOND));

        this.mockMvc.perform(post("/landing-list"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"responses":[
                        {"url":"https://www.z.com","delay":300000,"status":200,"id":"%s","group":"%s","average":300000,"uptime":100},
                        {"url":"https://www.x.com","delay":300000,"status":500,"id":"%s","group":"%s","average":300000,"uptime":0}
                        ],
                        "numUrls":2}""".formatted(
                                endpointZ.getId(), endpointZ.getGroup(),
                        endpointX.getId(), endpointX.getGroup() )));
    }
}