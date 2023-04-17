package org.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class InstantTimeResponse {
    private String abbreviation;
    private String client_ip;
    private String datetime;
    private int day_of_week;
    private int day_of_year;
    private boolean dst;
    private String dst_from;
    private int dst_offset;
    private String dst_until;
    private int raw_offset;
    private String timezone;
    private int unixtime;
    private String utc_datetime;
    private String utc_offset;
    private int week_number;

}
