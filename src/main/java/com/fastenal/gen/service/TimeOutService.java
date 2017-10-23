package com.fastenal.gen.service;

import com.fastenal.gen.runtimeComponents.TimeOutRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TimeOutService {

    @Autowired
    TimeOutRuntime timeOutObject;
    public void tellTime()
    {
        Map<Integer, Map<Date, String>> swipeRecords = timeOutObject.obtainSwipeRecord();
        System.out.println(swipeRecords);
    }

}
