package com.successpoint.wingo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseOfficaialAcounts {
    @SerializedName("code")
    @Expose
    int code;
    @SerializedName("count")
    @Expose
    int count;
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("content")
    @Expose
    List<OfficaialAcounts> content;


    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public int getCount() {
        return count;
    }

    public List<OfficaialAcounts> getContent() {
        return content;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setContent(List<OfficaialAcounts> content) {
        this.content = content;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
