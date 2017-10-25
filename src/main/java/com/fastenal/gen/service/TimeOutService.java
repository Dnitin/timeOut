package com.fastenal.gen.service;

import com.fastenal.gen.runtimeComponents.TimeOutRuntime;
import com.fastenal.gen.ui.RightClickmenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.*;

@Service
public class TimeOutService {

    RightClickmenu rightClickmenu = new RightClickmenu();

    @Autowired
    TimeOutRuntime timeOutObject;
    public void tellTime()
    {
        Map<Integer, Map<Date, String>> swipeRecords = timeOutObject.obtainSwipeRecord();
        rightClickmenu.renderRightClickMenu();
        System.out.println(swipeRecords);
    }

}
