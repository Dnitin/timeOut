package com.fastenal.gen.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Request {

    private String SelectedDate;

    private String empid;

    @JsonGetter("SelectedDate")
    public String getSelectedDate() {
        return SelectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        SelectedDate = selectedDate;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }
}
