package com.fastenal.gen.service;

import com.fastenal.gen.model.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TimeOutService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    Map<String, Map<Integer, Map<Date, String>>> swipeRecords = new HashMap<>();


    public void obtainRecordFromApi() {

    }


    public void tellTime()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.println("Begin /POST request!");
        String postUrl = "https://itportaltimer.azurewebsites.net/attendancecalendar/pages/Attendancecalendar.aspx/getDailyPunchRecords";
        Request request = new Request();
        request.setEmpid("112419");
        request.setSelectedDate("09-29-2017");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = null;
        try {
            entity = new HttpEntity<>(objectMapper.writeValueAsString(request), headers);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ResponseEntity<String> postResponse = restTemplate.exchange(postUrl, HttpMethod.POST, entity, String.class);
        Map<String,String> garbageContainer ;
        Map<String,Map<Integer,Map<Date,String>>> swipeRecords = new HashMap<>() ;
        DateFormat sdf = new SimpleDateFormat("hh:mm aaa");
        objectMapper.setDateFormat(sdf);

        try {
            garbageContainer = objectMapper.readValue(postResponse.getBody(), new TypeReference<HashMap>(){});
            swipeRecords = objectMapper.readValue(garbageContainer.get("d"), new TypeReference<HashMap>(){});
            System.out.println(swipeRecords);
        } catch (IOException e) {
            e.printStackTrace();
        }

       for(Map.Entry<Integer,Map<Date,String>> entry : swipeRecords.get("SwipeRecord").entrySet())
        {
            if(entry.getValue().get("swipeInOut") == "In")
            System.out.println(entry.getKey()+"<><>"+entry.getValue().get("swipeTime") + "<><>" + entry.getValue().get("swipeInOut"));
        }
    }

}
