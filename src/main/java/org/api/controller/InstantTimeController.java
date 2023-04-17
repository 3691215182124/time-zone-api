package org.api.controller;

import org.api.dto.TimeZoneResponse;
import org.api.service.TimeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping
public class InstantTimeController {
    @Autowired
    private TimeService timeService;

    @RequestMapping(path = "/time" , method = RequestMethod.GET)
    public ResponseEntity<TimeZoneResponse> getUSTime(@RequestParam String timezone) throws JsonProcessingException {
        if(timeService.isUsRegion(timezone)){
            TimeZoneResponse timeZoneResponse = timeService.getUSTimeZone(timezone);
            return new ResponseEntity(timeZoneResponse, HttpStatus.OK);
        }
        throw new RuntimeException("Not US Timezone");
    }
    @RequestMapping(path = "/time/**" , method = RequestMethod.GET)
    public ResponseEntity<TimeZoneResponse> getUSTimePath(HttpServletRequest request) throws JsonProcessingException {
        String requestURL = request.getRequestURL().toString();
        String timezone = requestURL.split("/time/")[1];
        if(timeService.isUsRegion(timezone)){
            TimeZoneResponse timeZoneResponse = timeService.getUSTimeZone(timezone);
            return new ResponseEntity(timeZoneResponse, HttpStatus.OK);
        }
        return new ResponseEntity(null,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleNoSuchElementFoundException(
            RuntimeException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }

}
