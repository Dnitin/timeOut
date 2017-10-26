package com.fastenal.gen.ui;

import com.fastenal.gen.model.RequestLeave;
import com.fastenal.gen.model.RequestSwipe;
import com.fastenal.gen.runtimeComponents.TimeOutRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Repository
public class RightClickmenu {

    @Autowired
    TimeOutRuntime timeOutObject;

    @Autowired
    RequestSwipe requestSwipe;

    @Autowired
    RequestLeave requestLeave;

    @PostConstruct
    public void setEmpId() {
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM");
        Date date = new Date();
        requestSwipe.setSelectedDate(dateFormat.format(date).toString());
        requestLeave.setCurrdate(dateFormat2.format(date).toString() + "/01");
        String employee = JOptionPane.showInputDialog("Enter Employee Id");
        if (!employee.isEmpty()) {
            requestSwipe.setEmpid(employee);
            requestLeave.setEmpid(employee);
        }
    }

    public void renderRightClickMenu() {
        SystemTray systemTray = SystemTray.getSystemTray();
        PopupMenu trayPopupMenu = new PopupMenu();
        Image image = Toolkit.getDefaultToolkit().getImage("src/images/1.ico");

        MenuItem employeeId = new MenuItem("Employee ID");
        employeeId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employee = JOptionPane.showInputDialog("Enter Employee Id");
                if (!employee.isEmpty()) {
                    requestSwipe.setEmpid(employee);
                }
            }
        });
        trayPopupMenu.add(employeeId);

        MenuItem swipeRecords = new MenuItem("List Swipe record");
        swipeRecords.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable swipeTable = getSwipeList();

                JScrollPane jScrollPane = new JScrollPane(swipeTable,
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                jScrollPane.setPreferredSize(new Dimension(400, 150));

                JOptionPane.showMessageDialog(null,
                        jScrollPane, "Swipe Records", JOptionPane.PLAIN_MESSAGE);
            }
        });
        trayPopupMenu.add(swipeRecords);
        trayPopupMenu.addSeparator();

        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        trayPopupMenu.add(exit);

        TrayIcon trayIcons = new TrayIcon(image, "TimeOut", trayPopupMenu);

        trayIcons.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trayIcons.displayMessage("User", "Time Left for day: " +
                        timeOutObject.obtainWeekRecord().toString(), TrayIcon.MessageType.INFO);
            }
        });

        trayIcons.setImageAutoSize(true);
        try {
            systemTray.add(trayIcons);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public JTable getSwipeList() {
        Map<Integer, Map<Date, String>> swipeRecords = timeOutObject.obtainSwipeRecord();
        String[][] data = new String[swipeRecords.size()][3];
        String[] legends = {"Serial #", "Swipe Time", "In/Out"};
        int i = 0;
        for (Map.Entry<Integer, Map<Date, String>> entry : swipeRecords.entrySet()) {
            data[i][0] = String.valueOf(i + 1);
            data[i][1] = entry.getValue().get("swipeTime").toString();
            data[i][2] = entry.getValue().get("swipeInOut").toString();
            i++;
        }
        return new JTable(data, legends);
    }
}
