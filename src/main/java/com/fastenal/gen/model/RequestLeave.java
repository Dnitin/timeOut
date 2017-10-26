package com.fastenal.gen.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Value;

public class RequestLeave {
    @JsonIgnore
    @Value("${swipeApi.url1:url}")
    private String url;

    private String Currdate;

    private String empid;

    @JsonGetter("Currdate")
    public String getCurrdate() {
        return Currdate;
    }

    public void setCurrdate(String currdate) {
        Currdate = currdate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }
}
