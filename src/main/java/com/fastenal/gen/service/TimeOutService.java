package com.fastenal.gen.service;

import com.fastenal.gen.model.D;
import com.fastenal.gen.model.Request;
import com.fastenal.gen.model.SwipeRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.StringEscapeUtils;
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
        //String postResponse = "{\"d\":{\"SwipeRecord\":{\"1\":{\"swipeTime\":\"12:58 PM\",\"swipeInOut\":\"In\"},\"2\":{\"swipeTime\":\"01:34 PM\",\"swipeInOut\":\"Out\"},\"3\":{\"swipeTime\":\"01:34 PM\",\"swipeInOut\":\"Out\"},\"4\":{\"swipeTime\":\"01:42 PM\",\"swipeInOut\":\"In\"},\"5\":{\"swipeTime\":\"04:29 PM\",\"swipeInOut\":\"Out\"},\"6\":{\"swipeTime\":\"04:30 PM\",\"swipeInOut\":\"In\"},\"7\":{\"swipeTime\":\"06:51 PM\",\"swipeInOut\":\"Out\"},\"8\":{\"swipeTime\":\"06:52 PM\",\"swipeInOut\":\"In\"},\"9\":{\"swipeTime\":\"10:28 PM\",\"swipeInOut\":\"Out\"},\"10\":{\"swipeTime\":\"10:29 PM\",\"swipeInOut\":\"In\"},\"11\":{\"swipeTime\":\"11:04 PM\",\"swipeInOut\":\"Out\"},\"12\":{\"swipeTime\":\"11:09 PM\",\"swipeInOut\":\"In\"},\"13\":{\"swipeTime\":\"11:37 PM\",\"swipeInOut\":\"Out\"}}}}";
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
