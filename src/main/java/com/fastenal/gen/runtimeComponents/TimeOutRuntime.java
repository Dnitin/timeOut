package com.fastenal.gen.runtimeComponents;

import com.fastenal.gen.model.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class TimeOutRuntime {

    @Autowired
    private Request request;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void setEmployeeId() {
        request.setEmpid("112419");
        request.setSelectedDate("10-23-2017");
    }

    public Map<Integer, Map<Date, String>> obtainSwipeRecord() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = null;
        try {
            entity = new HttpEntity<>(objectMapper.writeValueAsString(request), headers);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ResponseEntity<String> postResponse = restTemplate.exchange(request.getUrl(), HttpMethod.POST, entity, String.class);
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
