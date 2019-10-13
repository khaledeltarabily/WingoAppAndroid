package com.successpoint.wingo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.successpoint.wingo.App;

public class Mainsharedprefs {
    public static void saveToken(String token){
        SharedPreferences prefs = App.getAppContext().getSharedPreferences("Token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Token",token);
        editor.apply();
        editor.commit();
    }

    public static String getToken(){
        return App.getAppContext().getSharedPreferences("Token", Context.MODE_PRIVATE).getString("Token","NaN");
    }
}
