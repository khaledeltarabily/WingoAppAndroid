package com.successpoint.wingo.view.mainactivity.fragments.Profile;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.successpoint.wingo.model.TopFansModel;
import com.successpoint.wingo.model.UserModelObject;

import org.json.JSONObject;

public interface ProfileView extends MvpView {
    void DataRetrieved(UserModelObject user_object, boolean isFail);

    void SocialConnected(boolean Status, String type);

}