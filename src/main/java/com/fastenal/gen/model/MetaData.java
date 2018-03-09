
package com.fastenal.gen.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaData {

    @SerializedName("numFound")
    @Expose
    private Integer numFound;
    @SerializedName("aggregations")
    @Expose
    private List<Aggregation> aggregations = null;

    public Integer getNumFound() {
        return numFound;
    }

    public void setNumFound(Integer numFound) {
        this.numFound = numFound;
    }

    public List<Aggregation> getAggregations() {
        return aggregations;
    }

    public void setAggregations(List<Aggregation> aggregations) {
        this.aggregations = aggregations;
    }

}
