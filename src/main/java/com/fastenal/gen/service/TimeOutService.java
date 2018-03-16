package com.fastenal.gen.service;

import com.fastenal.gen.ui.RightClickmenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;


@Service
public class TimeOutService {

    private static final Logger LOG = Logger.getLogger(TimeOutService.class.getName());

    @Autowired
    RightClickmenu rightClickmenu;

    public void tellTime()
    {
        LOG.info("TimeOutService :: tellTime() : Start");
        rightClickmenu.renderRightClickMenu();
        LOG.info("TimeOutService :: tellTime() : End");
    }

}
