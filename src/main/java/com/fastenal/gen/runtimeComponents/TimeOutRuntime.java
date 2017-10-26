package com.fastenal.gen.runtimeComponents;

import com.fastenal.gen.model.RequestLeave;
import com.fastenal.gen.model.RequestSwipe;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class TimeOutRuntime {

    @Autowired
    private RequestLeave requestLeave;

    @Autowired
    private RequestSwipe requestSwipe;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, Map<Date, String>> obtainWeekRecord() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = null;
        try {
            entity = new HttpEntity<>(objectMapper.writeValueAsString(requestLeave), headers);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ResponseEntity<String> postResponse = restTemplate.exchange(requestLeave.getUrl(), HttpMethod.POST, entity, String.class);
        Map<String, String> preContainer;
        Map<String, Map<String, Map<Date, String>>> weekRecords = new HashMap<>();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(sdf);

        try {
            preContainer = objectMapper.readValue(postResponse.getBody(), new TypeReference<HashMap>() {
            });
            weekRecords = objectMapper.readValue(preContainer.get("d"), new TypeReference<HashMap>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return weekRecords.get("WeeklyHours");
    }

    public Map<Integer, Map<Date, String>> obtainSwipeRecord() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = null;
        try {
            entity = new HttpEntity<>(objectMapper.writeValueAsString(requestSwipe), headers);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ResponseEntity<String> postResponse = restTemplate.exchange(requestSwipe.getUrl(), HttpMethod.POST, entity, String.class);
        Map<String, String> garbageContainer;
        Map<String, Map<Integer, Map<Date, String>>> swipeRecords = new HashMap<>();
        DateFormat sdf = new SimpleDateFormat("hh:mm aaa");
        objectMapper.setDateFormat(sdf);

        try {
            garbageContainer = objectMapper.readValue(postResponse.getBody(), new TypeReference<HashMap>() {
            });
            swipeRecords = objectMapper.readValue(garbageContainer.get("d"), new TypeReference<HashMap>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return swipeRecords.get("SwipeRecord");
    }
}
