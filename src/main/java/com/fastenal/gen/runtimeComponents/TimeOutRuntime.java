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
import java.text.ParseException;
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

    public Map<String, String> obtainWeekRecord() {
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
        Map<String, Map<String, String>> weekRecords = new HashMap<>();

        try {
            preContainer = objectMapper.readValue(postResponse.getBody(), new TypeReference<HashMap>() {
            });
            weekRecords = objectMapper.readValue(preContainer.get("d"), new TypeReference<HashMap>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("just");
            return null;
        }
        return weekRecords.get("WeeklyHours");
    }

    public Map<Integer, Map<String, String>> obtainSwipeRecord() {
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
        Map<String, Map<Integer, Map<String, String>>> swipeRecords = new HashMap<>();

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

    public long timeInMinutes(String hours){
        String[] hour = hours.split(":");
        return Long.valueOf(hour[0])*60+Long.valueOf(hour[1]);
    }

    public long calculateWeeklyRemainingTime() {
        Map<String, String> weeklyhours = obtainWeekRecord();
        Long value = 0L;
        for (String key : weeklyhours.keySet()) {
            String[] avgTotal = weeklyhours.get(key).split(";");
            Long weekAvg = timeInMinutes(avgTotal[0]);
            Long weekTotal = timeInMinutes(avgTotal[1]);
            Long dayNum = weekTotal/weekAvg;
            value = dayNum*8*60 - weekTotal;
        }
        return value;
    }

    public long calculateRemainingMillis() {
        Map<Integer, Map<String, String>> swipeRecord = obtainSwipeRecord();
        long millis = 0;
        long totalMillis = 28800000;
        String prevVal = "darbage";
        String currVal;
        Date currDate = new Date();
        Date prevDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        for (Map.Entry<Integer, Map<String, String>> entry : swipeRecord.entrySet()) {
            currVal = entry.getValue().get("swipeInOut");
            try {
                currDate = formatter.parse(entry.getValue().get("swipeTime"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (prevVal.equalsIgnoreCase("In") && currVal.equalsIgnoreCase("Out")) {
                millis += currDate.getTime() - prevDate.getTime();
                prevVal = "garbage";
            } else {
                prevVal = currVal;
                prevDate = currDate;
            }
        }

        //System.out.println(millis);
        if (prevVal.equalsIgnoreCase("In")) {
            String todayDate = formatter.format(new Date());
            try {
                currDate = formatter.parse(todayDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            millis += currDate.getTime() - prevDate.getTime();
        }
        //System.out.println(totalMillis - millis);
        return totalMillis - millis;

    }
}
