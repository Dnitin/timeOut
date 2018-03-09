
package com.fastenal.gen.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseBody {

    @SerializedName("metaData")
    @Expose
    private MetaData metaData;
    @SerializedName("data")
    @Expose
    private Data data;

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
