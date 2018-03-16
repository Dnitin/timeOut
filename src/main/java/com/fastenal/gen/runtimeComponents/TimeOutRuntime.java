package com.fastenal.gen.runtimeComponents;

import com.fastenal.gen.model.ESResponse;
import com.fastenal.gen.model.EmployeeList;
import com.fastenal.gen.model.RequestLeave;
import com.fastenal.gen.model.RequestSwipe;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Repository
public class TimeOutRuntime {

    private static final Logger LOG = Logger.getLogger(TimeOutRuntime.class.getName());

    @Autowired
    private RequestLeave requestLeave;

    @Autowired
    private RequestSwipe requestSwipe;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public List<EmployeeList> obtainEmployeeInfo(String employeeInfo) {
        LOG.info("TimeOutRuntime :: obtainEmployeeInfo() : Start");
        Gson gson = new Gson();
        String url = "https://esearch.stg.fastenal.com/v1/employee/employeeList.json";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = null;
        entity = new HttpEntity<>("{\"query\" :  \"" + employeeInfo + "\"}"  , headers);
        ResponseEntity<String> postResponse = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        Map<String, Map<String, String>> empInfo = new HashMap<>();
        List<String> employeeList = new ArrayList<String>();
        ESResponse response = gson.fromJson(postResponse.getBody() , ESResponse.class);

        LOG.info("TimeOutRuntime :: obtainEmployeeInfo() returning employeeList : End");

        return response.getResponseBody().getData().getEmployeeList();
    }

    public Map<String, String> obtainWeekRecord() {
        LOG.info("TimeOutRuntime :: obtainWeekRecord() : Start");
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
            preContainer = objectMapper.readValue(postResponse.getBody(), new TypeReference<HashMap>() {});
            weekRecords = objectMapper.readValue(preContainer.get("d"), new TypeReference<HashMap>() {});
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("just");
            return null;
        }
        LOG.info("TimeOutRuntime :: obtainWeekRecord() :: returning WeeklyHours Map : End");
        return weekRecords.get("WeeklyHours");
    }

    public Map<Integer, Map<String, String>> obtainSwipeRecord() {
        LOG.info("TimeOutRuntime :: obtainSwipeRecord() : Start");
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
            garbageContainer =
                    objectMapper.readValue(postResponse.getBody(), new TypeReference<HashMap>() {
            });
            swipeRecords = objectMapper.readValue(garbageContainer.get("d"), new TypeReference<HashMap>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        LOG.info("TimeOutRuntime :: obtainSwipeRecord() :: returning SwipeRecord Map : End");
        return swipeRecords.get("SwipeRecord");
    }

    private long timeInMinutes(String hours){
        LOG.info("TimeOutRuntime :: timeInMinutes() : Start");
        String[] hour = hours.split(":");
        return Long.valueOf(hour[0])*60+Long.valueOf(hour[1]);
    }

    public long calculateWeeklyRemainingTime() {
        LOG.info("TimeOutRuntime :: calculateWeeklyRemainingTime() : Start");
        Map<String, String> weeklyhours = obtainWeekRecord();
        Long value = 0L;
        Long dayNum = 0L;
      
        for (String key : weeklyhours.keySet()) {
            String[] avgTotal = weeklyhours.get(key).split(";");
            Long weekAvg = timeInMinutes(avgTotal[0]);
            Long weekTotal = timeInMinutes(avgTotal[1]);
            if(weekAvg!=0)
                dayNum = weekTotal/weekAvg;
            value = dayNum*8*60 - weekTotal;
        }
        LOG.info("TimeOutRuntime :: calculateWeeklyRemainingTime() :: returning value : End");
        return value;
    }

    public long calculateRemainingMillis() {
        LOG.info("TimeOutRuntime :: calculateRemainingMillis() : Start");
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

        if (prevVal.equalsIgnoreCase("In")) {
            String todayDate = formatter.format(new Date());
            try {
                currDate = formatter.parse(todayDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            millis += currDate.getTime() - prevDate.getTime();
        }
        LOG.info("TimeOutRuntime :: calculateRemainingMillis() :: returning totalMillis - millis : End");
        return totalMillis - millis;
    }
}
