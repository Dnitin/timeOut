
package com.fastenal.gen.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AggregationValue {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("id")
    @Expose
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
