package com.successpoint.wingo.model;

import com.google.firebase.database.DataSnapshot;

public class GlobalGiftModel
{
    public String GiftText;
    public String OpenAnotherLive;
    public String image_url;
    public String GiftId;
    public int GiftCount;

    public GlobalGiftModel(DataSnapshot ds){

    }

    public String getGiftText() {
        return GiftText;
    }

    public String getOpenAnotherLive() {
        return OpenAnotherLive;
    }

    public void setGiftText(String giftText) {
        GiftText = giftText;
    }

    public void setOpenAnotherLive(String openAnotherLive) {
        OpenAnotherLive = openAnotherLive;
    }

    public int getGiftCount() {
        return GiftCount;
    }

    public void setGiftCount(int giftCount) {
        GiftCount = giftCount;
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