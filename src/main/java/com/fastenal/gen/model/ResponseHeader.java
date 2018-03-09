
package com.fastenal.gen.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseHeader {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("svcTime")
    @Expose
    private Integer svcTime;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSvcTime() {
        return svcTime;
    }

    public void setSvcTime(Integer svcTime) {
        this.svcTime = svcTime;
    }

}
