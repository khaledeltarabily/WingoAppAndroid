package com.successpoint.wingo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OfficaialAcounts {
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("level")
    @Expose
    int level;
    @SerializedName("image")
    @Expose
    String image;

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
