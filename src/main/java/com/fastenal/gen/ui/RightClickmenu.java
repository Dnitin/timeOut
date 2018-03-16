package com.fastenal.gen.ui;

import com.fastenal.gen.model.EmployeeList;
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
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Repository
public class RightClickmenu {

    private static final Logger LOG = Logger.getLogger(RightClickmenu.class.getName());

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
        LOG.info("RightClickmenu :: setEmpId() : Start");
        refreshDates();
        String systemId = System.getProperty("user.name");
        String employee;
        List<EmployeeList> employeeLists = timeOutObject.obtainEmployeeInfo(systemId);
        if(employeeLists.size()<=0 || employeeLists.size()>1) {
            promptAndSetEmployeeInfo();
        }
        else {
            employee = employeeLists.get(0).getEmpId().replaceFirst("^0+(?!$)", "");
            requestSwipe.setEmpid(employee);
            requestLeave.setEmpid(employee);
            employeeName = employeeLists.get(0).getFirstName().split(" ")[0];
        }
        LOG.info("RightClickmenu :: setEmpId() : End");
    }

    @Scheduled(cron = "1 0 0 ? * *")
    public void refreshDates()
    {
        LOG.info("RightClickmenu :: refreshDates() : Start");
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        requestSwipe.setSelectedDate(dateFormat.format(date).toString());
        requestLeave.setCurrdate(dateFormat2.format(date).toString());
        LOG.info("RightClickmenu :: refreshDates() : End");
    }

    public void promptAndSetEmployeeInfo() {
        LOG.info("RightClickmenu :: promptAndSetEmployeeInfo() : Start");
        String employee = JOptionPane.showInputDialog("Enter Employee Id");
        List<EmployeeList> employeeLists;
        if (!employee.isEmpty()) {
            requestSwipe.setEmpid(employee);
            requestLeave.setEmpid(employee);
            employeeLists = timeOutObject.obtainEmployeeInfo(employee);
            employeeName = (employeeLists.size() > 1) ? "I Am Confused?" :
                    employeeLists.get(0).getFirstName().split(" ")[0];
        }
        LOG.info("RightClickmenu :: promptAndSetEmployeeInfo() : End");
    }

    public void renderRightClickMenu() {
        LOG.info("RightClickmenu :: renderRightClickMenu() : Start");
        SystemTray systemTray = SystemTray.getSystemTray();

        MenuItem employeeId = new MenuItem("Employee ID");
        employeeId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                promptAndSetEmployeeInfo();
            }
        });
        trayPopupMenu.add(employeeId);

        MenuItem findEmp = new MenuItem("Search Employee");
        findEmp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeName = JOptionPane.showInputDialog("Enter Employee Name");
                List<EmployeeList> employeeLists = timeOutObject.obtainEmployeeInfo(employeeName);
                String employeeData = "" ;
                for(EmployeeList employeeList : employeeLists) {
                    employeeData += employeeList.getName()+ " : " + employeeList.getEmpId() + "\n";
                }
                trayIcon.displayMessage("Are you looking for these people?" , employeeData, TrayIcon.MessageType.INFO);
            }
        });
        trayPopupMenu.add(findEmp);

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
            LOG.severe(e.getMessage());
            trayIcon.displayMessage("Oops!","Some Error Occured",TrayIcon.MessageType.ERROR);
        }
        LOG.info("RightClickmenu :: renderRightClickMenu() : End");
    }

    @Scheduled(cron = "0 0 9-21/3 ? * MON-FRI")
    public void trayPopupLeftTime()
    {
        LOG.info("RightClickmenu :: trayPopupLeftTime() : Start");
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

        LOG.info("RightClickmenu :: trayPopupLeftTime() : End");
    }

    public JTable getSwipeList() {
        LOG.info("RightClickmenu :: getSwipeList() : Start");
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
        LOG.info("RightClickmenu :: getSwipeList() :: returning JTable : End");
        return new JTable(data, legends);
    }
}
