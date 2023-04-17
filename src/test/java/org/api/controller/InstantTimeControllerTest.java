package org.api.controller;

import org.api.dto.TimeZoneResponse;
import org.api.service.TimeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class InstantTimeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimeService timeService;

    @BeforeEach
    private void setUp(){
    }
    @Test
    public void getValidUSTime() throws Exception {
        try {
            String region = "America/New_York";
            Mockito.when(timeService.isUsRegion(region)).thenReturn(true);
            TimeZoneResponse timeZoneResponse = new TimeZoneResponse("EST","2023-04-05T11:56:45.564609-07:00",region);
            Mockito.when(timeService.getUSTimeZone(region)).thenReturn(timeZoneResponse);
            mockMvc.perform(MockMvcRequestBuilders.get("/time")
                            .param("timezone", region))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.abbreviation",Matchers.is("EST")))
                    .andExpect(jsonPath("$.datetime",Matchers.is("2023-04-05T11:56:45.564609-07:00")))
                    .andExpect(jsonPath("$.timezone",Matchers.is("America/New_York")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void invalidTimezone() throws Exception {
        String timezone = "Europe/Paris";
        Mockito.when(timeService.isUsRegion(timezone)).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.get("/time")
                        .param("timezone", timezone))
                .andExpect(status().is5xxServerError());
    }
}
