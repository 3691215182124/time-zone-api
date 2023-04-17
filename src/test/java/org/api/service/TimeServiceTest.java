package org.api.service;

import org.api.dto.InstantTimeResponse;
import org.api.dto.TimeZoneResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TimeServiceTest {
 
    @MockBean
    private RestTemplate worldApiRestTemplate;

    @Value("${world.api.url}")
    private String baseUrl;
    @Autowired
    private TimeService timeService;

    private String[] timezoneArr ;

    @BeforeEach
    private void setUp(){
        timezoneArr =new String[]{ "America/New_York","Europe/Paris",
                "America/Menominee", "America/Los_Angeles"};
    }
    @Test
    public void testValidUSTimezone(){
        String timezone = "America/New_York";

        Mockito.when(worldApiRestTemplate.getForObject(baseUrl  + "/api/timezone/America"
                ,String[].class)).thenReturn(timezoneArr);
        Assertions.assertTrue(timeService.isUsRegion(timezone));
    }

    @Test
    public void testToGetUSTimezone() throws JsonProcessingException {
        String timezone = "America/Los_Angeles";
        InstantTimeResponse instantTimeResponse = getSampleResponse();
        Mockito.when(worldApiRestTemplate.exchange(Mockito.anyString(),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.any(HttpEntity.class),
                        Mockito.eq(InstantTimeResponse.class)))
                .thenReturn(new ResponseEntity<>(instantTimeResponse, HttpStatus.ACCEPTED));
        TimeZoneResponse timeZoneResponse = timeService.getUSTimeZone(timezone);
        TimeZoneResponse timeZoneResponseExpected = new TimeZoneResponse("PDT","2023-04-05T11:56:45.564609-07:00","America/Los_Angeles");
        Assertions.assertEquals(timeZoneResponseExpected, timeZoneResponse);
    }

    private InstantTimeResponse getSampleResponse(){
        File file = new File(
                this.getClass().getClassLoader().getResource("usTimezoneResponse.json").getFile()
        );
        InstantTimeResponse instantTimeResponse;
        ObjectMapper mapper = new ObjectMapper();
        try {
            instantTimeResponse = mapper.readValue(file, InstantTimeResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return instantTimeResponse;
    }
}
