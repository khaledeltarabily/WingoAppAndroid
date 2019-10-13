package com.successpoint.wingo;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.utils.Mainsharedprefs;
import com.successpoint.wingo.view.mainactivity.MainActivity;
import com.successpoint.wingo.view.mainsign.MainSignActivity;
import com.successpoint.wingo.view.showLiveView.GoLiveView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {
    ImageView imageView;
    ImageView imageView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if(Mainsharedprefs.getToken().equals("NaN"))
                startActivity(new Intent(SplashActivity.this, MainSignActivity.class));
            else {
                try {
                    Log.e("Datalogged",getSharedPreferences("wingo",MODE_PRIVATE).getString("user_data","")+"Shared");
                    UserModelObject data = new UserModelObject(new JSONObject(getSharedPreferences("wingo",MODE_PRIVATE).getString("user_data","")));
                    data.setUser_token(Mainsharedprefs.getToken());
                    App.userModelObject_of_Project = data ;
                    Log.e("Data",App.userModelObject_of_Project.getUser_id()+"User_id");
                    startActivity(new Intent(SplashActivity.this, GoLiveView.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                    startActivity(new Intent(SplashActivity.this, MainSignActivity.class));
                }

            }
            finish();
        }, 3000);
    }
}
