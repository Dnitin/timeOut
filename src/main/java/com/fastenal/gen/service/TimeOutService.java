package com.fastenal.gen.service;

import com.fastenal.gen.ui.RightClickmenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TimeOutService {

    @Autowired
    RightClickmenu rightClickmenu;

    public void tellTime()
    {
        rightClickmenu.renderRightClickMenu();
    }

}
