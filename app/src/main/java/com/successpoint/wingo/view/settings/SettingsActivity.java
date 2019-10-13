package com.successpoint.wingo.view.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.successpoint.wingo.R;
import com.successpoint.wingo.utils.Mainsharedprefs;
import com.successpoint.wingo.view.mainsign.MainSignActivity;

public class SettingsActivity extends MvpActivity<SettingsView, SettingsPresenter> implements SettingsView {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findViewById(R.id.logout).setOnClickListener(v -> {
            Mainsharedprefs.saveToken("NaN");
            startActivity(new Intent(SettingsActivity.this,MainSignActivity.class));
            finish();
        });

//        findViewById(R.id.Language).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
//            }
//        });
        findViewById(R.id.privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,privacyPage.class));

            }
        });
        findViewById(R.id.blocklist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.connected_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.notifictions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,NotificationPage.class));
            }
        });
//        findViewById(R.id.aboutus).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        findViewById(R.id.feedback).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        findViewById(R.id.clear_cash).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        findViewById(R.id.version).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

    @NonNull
    @Override
    public SettingsPresenter createPresenter() {
        return new SettingsPresenter(this,this);
    }

    @Override
    public void DataRetrieved(boolean Done) {

    }
}