package com.successpoint.wingo.view.mainsign;

import com.facebook.AccessToken;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.successpoint.wingo.model.SocialLoginModel;
import com.successpoint.wingo.model.UserModelObject;

import org.json.JSONObject;

public interface MainSignView extends MvpView {
    void publishFacebook(JSONObject loginResult,AccessToken accestoken);
    void publishSocial(SocialLoginModel response);
    void RegisterSocial(boolean DoneOrNot,String token);
    void DataRetrieved(UserModelObject user_object);
    void PhoneRetrieved(SocialLoginModel DoneOrNot,String user_tokenx);
}