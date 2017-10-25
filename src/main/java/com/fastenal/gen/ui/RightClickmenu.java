package com.fastenal.gen.ui;

import com.fastenal.gen.model.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RightClickmenu {

    @Autowired
    private SystemTray systemTray;

    @Autowired
    private Request request;


    public void renderRightClickMenu() {
        PopupMenu trayPopupMenu = new PopupMenu();
        Image image = Toolkit.getDefaultToolkit().getImage("src/images/1.ico");

        MenuItem employeeId = new MenuItem("Employee ID");
        employeeId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employee = JOptionPane.showInputDialog("Enter Employee Id");
                if (!employee.isEmpty()) {
                    request.setEmpid(employee);
                }
            }
        });
        trayPopupMenu.add(employeeId);

        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        trayPopupMenu.add(exit);

        MenuItem swipeRecords = new MenuItem("List Swipe record");
        swipeRecords.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getSwipeList();
            }
        });
        trayPopupMenu.add(swipeRecords);
        TrayIcon trayIcons = new TrayIcon(image, "TimeOut App", trayPopupMenu);

        trayIcons.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trayIcons.displayMessage("User", "Time Left for day: ", TrayIcon.MessageType.INFO);
            }
        });

        trayIcons.setImageAutoSize(true);
        try {
            systemTray.add(trayIcons);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void getSwipeList() {

    }

}
