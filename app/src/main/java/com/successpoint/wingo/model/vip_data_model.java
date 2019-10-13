package com.successpoint.wingo.model;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class vip_data_model
{

    @SerializedName("vip")
    @Expose
    private int vip;
    @SerializedName("renew_date")
    @Expose
    private String renew_date;
    @SerializedName("renewable")
    @Expose
    private boolean renewable;
    @SerializedName("days_remaining")
    @Expose
    private String days_remaining = null;

    public int getVip() {
        return vip;
    }

    public String getDays_remaining() {
        return days_remaining;
    }

    public String getRenew_date() {
        return renew_date;
    }

    public void setDays_remaining(String days_remaining) {
        this.days_remaining = days_remaining;
    }

    public void setRenew_date(String renew_date) {
        this.renew_date = renew_date;
    }

    public void setRenewable(boolean renewable) {
        this.renewable = renewable;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public boolean isRenewable() {
        return renewable;
    }
}