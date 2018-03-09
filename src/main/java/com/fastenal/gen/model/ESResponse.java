package com.fastenal.gen.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ESResponse {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("responseHeader")
    @Expose
    private ResponseHeader responseHeader;
    @SerializedName("responseBody")
    @Expose
    private ResponseBody responseBody;
    @SerializedName("resultType")
    @Expose
    private String resultType;
    @SerializedName("requestBody")
    @Expose
    private RequestBody requestBody;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public ResponseBody getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

}
