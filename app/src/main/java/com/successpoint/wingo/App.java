package com.successpoint.wingo;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.successpoint.wingo.model.GiftModel;
import com.successpoint.wingo.model.UserModelObject;

import java.util.ArrayList;

public class App extends Application {
    static Context appContext;

    public static Context getAppContext() {
        return appContext;
    }
    static public UserModelObject userModelObject_of_Project = new UserModelObject();
    static public ArrayList<GiftModel> AllGiftsData = new ArrayList<>();
    static public int  position_is_be = 0;

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        App.appContext = getApplicationContext();
    }
}
