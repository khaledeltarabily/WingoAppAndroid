package com.successpoint.wingo.model;

import com.google.firebase.database.DataSnapshot;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserModelObject  implements Serializable {
    public UserModelObject(DataSnapshot dataSnapshot){
        name = dataSnapshot.child("name").getValue().toString();
        user_id = dataSnapshot.getKey();
        images.add(dataSnapshot.child("image").getValue().toString());
    }
    public UserModelObject(){}
    public UserModelObject(JSONObject object){
        try {
            name = object.getString("name");
            level = object.getInt("level");
            user_id = object.getString("user_id");
            exp = object.getInt("exp");
            vip = object.getInt("vip");
            send = object.getInt("send");
            facebook_token = object.getString("facebook_token");
            twitter_token = object.getString("twitter_token");
            google_token = object.getString("google_token");
            birth_date = object.getString("birth_date");
            age = object.getString("age");
            bio = object.getString("bio");


            verfied = object.getBoolean("verfied");
            feathers = object.getInt("feathers");
            diamonds = object.getInt("diamonds");
            wings = object.getInt("wings");
            friends = object.getInt("friends");
            fans = object.getInt("fans");
            following = object.getInt("following");
            country = object.getString("country");
            position = object.getInt("position");
            country = object.getString("country");
            total_recieve = object.getInt("total_recieve");
            picture = object.getString("picture");
            total_send = object.getInt("total_send");
            image_count = object.getInt("image_count");
            qr = object.getString("qr");
            frame = object.getInt("frame");
            for (int i =0 ; i<object.getJSONArray("images").length();i++) {
                images.add(object.getJSONArray("images").get(i).toString());
            }
            register_date = object.getString("register_date");
            elite_id = object.getString("elite_id");
            gender = object.getString("gender");

            hide_location = object.getBoolean("hide_location");
            hide_last_seen = object.getBoolean("hide_last_seen");
            hide_nearby = object.getBoolean("hide_nearby");
            turn_off_live_notifications = object.getBoolean("turn_off_live_notifications");
            turn_off_message_notifications = object.getBoolean("turn_off_message_notifications");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String user_token;

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    private boolean if_is_him_follow;

    public boolean isIf_is_him_follow() {
        return if_is_him_follow;
    }

    public void setIf_is_him_follow(boolean if_is_him_follow) {
        this.if_is_him_follow = if_is_him_follow;
    }

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("level")
    @Expose
    private int level;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("exp")
    @Expose
    private int exp;
    @SerializedName("vip")
    @Expose
    private int vip;
    @SerializedName("send")
    @Expose
    private int send;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("facebook_token")
    @Expose
    private String facebook_token;
    @SerializedName("twitter_token")
    @Expose
    private String twitter_token;
    @SerializedName("google_token")
    @Expose
    private String google_token ;
    @SerializedName("birth_date")
    @Expose
    private String birth_date;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("verfied")
    @Expose
    private boolean verfied;
    @SerializedName("feathers")
    @Expose
    private int feathers;
    @SerializedName("diamonds")
    @Expose
    private int diamonds;
    @SerializedName("wings")
    @Expose
    private int wings;
    @SerializedName("friends")
    @Expose
    private int friends;
    @SerializedName("fans")
    @Expose
    private int fans;
    @SerializedName("following")
    @Expose
    private int following;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("position")
    @Expose
    private int position;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("total_recieve")
    @Expose
    private int total_recieve;
    @SerializedName("total_send")
    @Expose
    private int total_send;
    @SerializedName("qr")
    @Expose
    private String qr;
    @SerializedName("frame")
    @Expose
    private int frame;
    @SerializedName("image_count")
    @Expose
    private int image_count;
    @SerializedName("register_date")
    @Expose
    private String register_date;
    @SerializedName("images")
    @Expose
    private List<String> images = new ArrayList<>();
    @SerializedName("elite_id")
    @Expose
    private String elite_id;
    @SerializedName("gender")
    @Expose
    private String gender;


    //   check for  settings
    @SerializedName("hide_location")
    @Expose
    private Boolean hide_location;
    @SerializedName("hide_last_seen")
    @Expose
    private Boolean hide_last_seen;
    @SerializedName("hide_nearby")
    @Expose
    private Boolean hide_nearby;
    @SerializedName("turn_off_live_notifications")
    @Expose
    private Boolean turn_off_live_notifications;
    @SerializedName("turn_off_message_notifications")
    @Expose
    private Boolean turn_off_message_notifications=true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int getSend() {
        return send;
    }

    public void setSend(int send) {
        this.send = send;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFacebook_token() {
        return facebook_token;
    }

    public void setFacebook_token(String facebook_token) {
        this.facebook_token = facebook_token;
    }

    public String getTwitter_token() {
        return twitter_token;
    }

    public void setTwitter_token(String twitter_token) {
        this.twitter_token = twitter_token;
    }

    public String getGoogle_token() {
        return google_token;
    }

    public void setGoogle_token(String google_token) {
        this.google_token = google_token;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isVerfied() {
        return verfied;
    }

    public void setVerfied(boolean verfied) {
        this.verfied = verfied;
    }

    public int getFeathers() {
        return feathers;
    }

    public void setFeathers(int feathers) {
        this.feathers = feathers;
    }

    public int getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(int diamonds) {
        this.diamonds = diamonds;
    }

    public int getWings() {
        return wings;
    }

    public void setWings(int wings) {
        this.wings = wings;
    }

    public int getFriends() {
        return friends;
    }

    public void setFriends(int friends) {
        this.friends = friends;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getTotal_recieve() {
        return total_recieve;
    }

    public void setTotal_recieve(int total_recieve) {
        this.total_recieve = total_recieve;
    }

    public int getTotal_send() {
        return total_send;
    }

    public void setTotal_send(int total_send) {
        this.total_send = total_send;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public int getImage_count() {
        return image_count;
    }

    public void setImage_count(int image_count) {
        this.image_count = image_count;
    }

    public String getRegister_date() {
        return register_date;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getElite_id() {
        return elite_id;
    }

    public void setElite_id(String elite_id) {
        this.elite_id = elite_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getHide_location() {
        return hide_location;
    }

    public void setHide_location(Boolean hide_location) {
        this.hide_location = hide_location;
    }

    public Boolean getHide_last_seen() {
        return hide_last_seen;
    }

    public void setHide_last_seen(Boolean hide_last_seen) {
        this.hide_last_seen = hide_last_seen;
    }

    public Boolean getHide_nearby() {
        return hide_nearby;
    }

    public void setHide_nearby(Boolean hide_nearby) {
        this.hide_nearby = hide_nearby;
    }

    public Boolean getTurn_off_live_notifications() {
        return turn_off_live_notifications;
    }

    public void setTurn_off_live_notifications(Boolean turn_off_live_notifications) {
        this.turn_off_live_notifications = turn_off_live_notifications;
    }

    public Boolean getTurn_off_message_notifications() {
        return turn_off_message_notifications;
    }

    public void setTurn_off_message_notifications(Boolean turn_off_message_notifications) {
        this.turn_off_message_notifications = turn_off_message_notifications;
    }




}






