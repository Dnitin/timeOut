package com.fastenal.gen.ui;

import com.fastenal.gen.model.ESResponse;
import com.fastenal.gen.model.RequestLeave;
import com.fastenal.gen.model.RequestSwipe;
import com.fastenal.gen.runtimeComponents.TimeOutRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    @Autowired
    PopupMenu trayPopupMenu;

    @Autowired
    TrayIcon trayIcon;

    String employeeName;

    @PostConstruct
    public void setEmpId() {
        refreshDates();
        String employee = JOptionPane.showInputDialog("Enter Employee Id");
        if (!employee.isEmpty()) {
            requestSwipe.setEmpid(employee);
            requestLeave.setEmpid(employee);
        }
        employeeName = timeOutObject.obtainEmployeeInfo();
    }

    @Scheduled(cron = "1 0 0 ? * *")
    public void refreshDates()
    {
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        requestSwipe.setSelectedDate(dateFormat.format(date).toString());
        requestLeave.setCurrdate(dateFormat2.format(date).toString());
    }

    public void renderRightClickMenu() {
        SystemTray systemTray = SystemTray.getSystemTray();

        MenuItem employeeId = new MenuItem("Employee ID");
        employeeId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setEmpId();
            }
        });
        trayPopupMenu.add(employeeId);

        /*MenuItem moveBy = new MenuItem("Move By");
        moveBy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String time = JOptionPane.showInputDialog(trayPopupMenu,"Time to move by");
            }
        });
        trayPopupMenu.add(moveBy);*/

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

        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trayPopupLeftTime();
            }
        });

        trayIcon.setImageAutoSize(true);
        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0 9-21/3 ? * MON-FRI")
    public void trayPopupLeftTime()
    {
        Date d1 = new Date();
        Calendar cl1 = Calendar. getInstance();
        Calendar cl2 = Calendar. getInstance();
        cl1.setTime(d1);
        cl2.setTime(d1);
        long time = timeOutObject.calculateRemainingMillis();
        long diffSec = time / 1000;
        long min = diffSec / 60;
        long weekRemTime = timeOutObject.calculateWeeklyRemainingTime() + min;

        int weekHourToMove = Math.toIntExact(weekRemTime/60);
        int weekMinuteToMove = Math.toIntExact(weekRemTime%60);

        int dayHourToMove = Math.toIntExact(min/60);
        int dayMinuteToMove = Math.toIntExact(min%60);

        cl1.add(Calendar.HOUR,weekHourToMove);
        cl1.add(Calendar.MINUTE, weekMinuteToMove+1);

        cl2.add(Calendar.HOUR, dayHourToMove);
        cl2.add(Calendar.MINUTE,dayMinuteToMove+1);

        trayIcon.displayMessage("Hi, " + employeeName,
                     "Time Left (Week) -> " + weekHourToMove + " hours " + weekMinuteToMove + " minutes\n"
                        + "Time Left (Day)  -> " + dayHourToMove + " hours " + dayMinuteToMove + " minutes\n"
                        + "Leave After\n"
                        + "Weekly Average   -> " + cl1.getTime().toString() + "\n"
                        + "Daily Average    -> " + cl2.getTime().toString(),
                TrayIcon.MessageType.INFO);
    }

    public JTable getSwipeList() {
        Map<Integer, Map<String, String>> swipeRecords = timeOutObject.obtainSwipeRecord();
        String[][] data = new String[swipeRecords.size()][3];
        String[] legends = {"Serial #", "Swipe Time", "In/Out"};
        int i = 0;
        for (Map.Entry<Integer, Map<String, String>> entry : swipeRecords.entrySet()) {
            data[i][0] = String.valueOf(i + 1);
            data[i][1] = entry.getValue().get("swipeTime");
            data[i][2] = entry.getValue().get("swipeInOut");
            i++;
        }
        return new JTable(data, legends);
    }
}
