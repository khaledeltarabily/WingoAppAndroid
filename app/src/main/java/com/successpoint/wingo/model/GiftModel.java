package com.successpoint.wingo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.successpoint.wingo.App;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GiftModel
{
    public String GiftName;
    public String UserName;
    public int Diamonds;
    public String image_url;
    public String GiftId;
    public int GiftCount;

    public GiftModel(DataSnapshot dataSnapshot){
       image_url = dataSnapshot.child("gift_image").getValue(String.class);
       GiftName = dataSnapshot.child("GiftName").getValue(String.class);
       GiftId = dataSnapshot.child("gift_id").getValue(String.class);
       GiftCount = Integer.parseInt(dataSnapshot.child("gift_count").getValue(String.class));
       UserName = dataSnapshot.child("senderName").getValue(String.class);
       image_url = dataSnapshot.child("sender_image").getValue(String.class);
    }
    public GiftModel(JSONObject json){
        try {
            GiftName = json.getString("name");
            Diamonds = json.getInt("diamonds");
            image_url = json.getString("icon");
            GiftId = json.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getGiftCount() {
        return GiftCount;
    }

    public void setGiftCount(int giftCount) {
        GiftCount = giftCount;
    }

    public String getGiftName() {
        return GiftName;
    }

    public void setGiftName(String giftName) {
        GiftName = giftName;
    }

    public int getDiamonds() {
        return Diamonds;
    }

    public void setDiamonds(int diamonds) {
        Diamonds = diamonds;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getGiftId() {
        return GiftId;
    }

    public void setGiftId(String giftId) {
        GiftId = giftId;
    }
}