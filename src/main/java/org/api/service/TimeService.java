package org.api.service;

import org.api.dto.InstantTimeResponse;
import org.api.dto.TimeZoneResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class TimeService {
    private final String timeApiURL;
    @Autowired
    @Qualifier("timeApiTemplate")
    private final RestTemplate timeApiTemplate;

    private List<String> timezoneList;

    public TimeService(@Value("${world.api.url}") String timeApiURL, RestTemplate timeApiTemplate) {

        this.timeApiTemplate = timeApiTemplate;
        this.timeApiURL = timeApiURL;
        timezoneList = null;
    }

    public boolean isUsRegion(final String timezone) {
        List<String> timezoneList = getRegionsList();
        return timezoneList.stream()
                .filter(timezoneArg -> timezone.equals(timezoneArg))
                .count() > 0;
    }

    public TimeZoneResponse getUSTimeZone(String timezone) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        String url = timeApiURL + "/api/timezone/" + timezone;

        ResponseEntity<InstantTimeResponse> apiResponse = timeApiTemplate
                .exchange(url, HttpMethod.GET,
                        requestEntity, InstantTimeResponse.class);

        return mapToTimeZone(apiResponse.getBody());
    }

    private TimeZoneResponse mapToTimeZone(InstantTimeResponse instantTimeResponse){
        return new TimeZoneResponse(instantTimeResponse.getAbbreviation(),instantTimeResponse.getDatetime(),instantTimeResponse.getTimezone());
    }

    public List<String> getRegionsList() {
        if (timezoneList == null) {
            timezoneList = Arrays.asList(timeApiTemplate
                    .getForObject(timeApiURL + "/api/timezone/America", String[].class));
        }
        return timezoneList;
    }


}
