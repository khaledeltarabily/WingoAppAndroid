package com.successpoint.wingo.model;

import com.google.firebase.database.DataSnapshot;

public class CommentModel
{
    public String comment_text;
    public String image_user;
    public String user_id;
    public String comment_type;
    public String user_name;
    public String user_level;
    public boolean isVip;
    public boolean isFly;

    public CommentModel(DataSnapshot dataSnapshot){
        comment_text = dataSnapshot.child("text").getValue().toString();
        user_id = dataSnapshot.child("user_id").getValue().toString();
        user_level = "0";
        user_name = dataSnapshot.child("name").getValue().toString();
        isFly = Boolean.parseBoolean(dataSnapshot.child("isFly").getValue(String.class));
        image_user = dataSnapshot.child("image").getValue().toString();

    }

    public String getImage_user() {
        return image_user;
    }

    public void setImage_user(String image_user) {
        this.image_user = image_user;
    }

    public boolean isFly() {
        return isFly;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public void setFly(boolean fly) {
        isFly = fly;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment_text() {
        return comment_text;
    }

    public String getComment_type() {
        return comment_type;
    }


    public String getUser_level() {
        return user_level;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public void setComment_type(String comment_type) {
        this.comment_type = comment_type;
    }


    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

}