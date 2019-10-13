package com.successpoint.wingo.view.settings;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.successpoint.wingo.App;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.UserModelObject;

import androidx.annotation.NonNull;

public class privacyPage extends MvpActivity<SettingsView, SettingsPresenter> implements SettingsView {

    SettingsPresenter presenter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_settings);
        presenter = new SettingsPresenter(this,this);
        Switch last_seen = findViewById(R.id.last_seen);
        Switch location = findViewById(R.id.location);
        Switch nearby = findViewById(R.id.nearby);
        last_seen.setChecked(App.userModelObject_of_Project.getHide_last_seen());
        nearby.setChecked(App.userModelObject_of_Project.getHide_nearby());
        location.setChecked(App.userModelObject_of_Project.getHide_location());
        last_seen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                App.userModelObject_of_Project.setHide_last_seen(b);
                getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("user_data",UserModelObjectToString(App.userModelObject_of_Project)).apply();

                presenter.ChangeSettingSwitch(App.userModelObject_of_Project.getUser_token(),"last_seen");
            }
        });
        nearby.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                App.userModelObject_of_Project.setHide_nearby(b);
                getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("user_data",UserModelObjectToString(App.userModelObject_of_Project)).apply();

                presenter.ChangeSettingSwitch(App.userModelObject_of_Project.getUser_token(),"nearby");
            }
        });
        location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                App.userModelObject_of_Project.setHide_location(b);
                getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("user_data",UserModelObjectToString(App.userModelObject_of_Project)).apply();

                presenter.ChangeSettingSwitch(App.userModelObject_of_Project.getUser_token(),"location");
            }
        });
    }

    @NonNull
    @Override
    public SettingsPresenter createPresenter() {
        return new SettingsPresenter(this,this);
    }

    @Override
    public void DataRetrieved(boolean Done) {

    }
    public String UserModelObjectToString(UserModelObject user){
        String result = "{";
        result += " \"name\": \"" + user.getName()+"\"";
        result += " , \"level\":" + user.getLevel();
        result += " , \"user_id\":\"" + user.getUser_id()+"\"";
        result += " , \"exp\":" + user.getExp();
        result += " , \"vip\":" + user.getVip();
        result += " , \"send\":" + user.getSend();
//            result += " , \"facebook_token\":\"" + user.getFacebook_token()+"\"";
//            result += " , \"twitter_token\"\":" + user.getTwitter_token()+"\"";
//            result += " , \"google_token\":" + user.getGoogle_token()+"\"";
        result += " , \"birth_date\":\"" + user.getBirth_date()+"\"";
        result += " , \"age\":\"" + user.getAge()+"\"";
        result += " , \"bio\": \"" + user.getBio()+"\"";

        result += " , \"verfied\":" + user.isVerfied()+"\"";
        result += " , \"feathers\":" + user.getFeathers();
        result += " , \"diamonds\": " + user.getDiamonds();
        result += " , \"wings\":" + user.getWings();
        result += " , \"friends\": " + user.getFriends();
        result += " , \"fans\":" + user.getFans();
        result += " , \"following\":" + user.getFollowing();
        result += " , \"country\": \"" + user.getCountry()+"\"";
        result += " , \"position\":" + user.getPosition();
        result += " , \"total_recieve\":" + user.getTotal_recieve();
        result += " , \"picture\": \"" + user.getPicture()+"\"";
        result += " , \"total_send\": " + user.getTotal_send();
        result += " , \"image_count\": " + user.getImage_count();
        result += " , \"register_date\": \"" + user.getRegister_date()+"\"";
        result += " , \"elite_id\": \"" + user.getElite_id()+"\"";
        result += " , \"gender\": \"" + user.getGender()+"\"";
        result += " , \"turn_off_live_notifications\": " + user.getTurn_off_live_notifications();
        result += " , \"turn_off_message_notifications\": " + user.getTurn_off_message_notifications();
        result += " , \"hide_last_seen\": " + user.getHide_last_seen();
        result += " , \"hide_location\": " + user.getHide_location();
        result += " , \"hide_location\": " + user.getHide_location();
        result += ", \"images\": [";
        for (int i =0 ; i<user.getImages().size();i++){
            if (i!=0)
                result += " , ";
            result += "\"" + user.getImages().get(i)+"\"";
        }
        result += "]";

        result +=" }";


        return result;
    }

}