package com.fastenal.gen.service;

import com.fastenal.gen.ui.RightClickMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;


@Service
public class TimeOutService {

    private static final Logger LOG = Logger.getLogger(TimeOutService.class.getName());

    @Autowired
    RightClickMenu rightClickMenu;

    public void tellTime()
    {
        LOG.info("TimeOutService :: tellTime() : Start");
        rightClickMenu.renderRightClickMenu();
        LOG.info("TimeOutService :: tellTime() : End");
    }

}
