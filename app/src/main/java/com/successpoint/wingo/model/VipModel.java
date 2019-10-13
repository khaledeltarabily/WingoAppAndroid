package com.successpoint.wingo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VipModel
{

    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("vip_price")
    @Expose
    private List<Integer> vip_price = null;
    @SerializedName("vip_renew")
    @Expose
    private List<Integer> vip_renew = null;
    @SerializedName("data")
    @Expose
    private vip_data_model data = null;


    public List<Integer> getVip_renew() {
        return vip_renew;
    }

    public void setVip_renew(List<Integer> vip_renew) {
        this.vip_renew = vip_renew;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public vip_data_model getData() {
        return data;
    }

    public List<Integer> getVip_price() {
        return vip_price;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(vip_data_model data) {
        this.data = data;
    }

    public void setVip_price(List<Integer> vip_price) {
        this.vip_price = vip_price;
    }
}