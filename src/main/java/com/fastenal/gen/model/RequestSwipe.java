package com.fastenal.gen.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Value;

public class RequestSwipe {

    @JsonIgnore
    @Value("${swipeApi.url:url}")
    private String url;

    private String SelectedDate;

    private String empid;

    @JsonGetter("SelectedDate")
    public String getSelectedDate() {
        return SelectedDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
