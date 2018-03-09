
package com.fastenal.gen.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Aggregation {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("attributeName")
    @Expose
    private String attributeName;
    @SerializedName("aggregationValues")
    @Expose
    private List<AggregationValue> aggregationValues = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public List<AggregationValue> getAggregationValues() {
        return aggregationValues;
    }

    public void setAggregationValues(List<AggregationValue> aggregationValues) {
        this.aggregationValues = aggregationValues;
    }

}
