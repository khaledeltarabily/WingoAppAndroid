package com.successpoint.wingo.view.mainactivity.fragments.Profile;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.successpoint.wingo.model.SocialLoginModel;
import com.successpoint.wingo.model.TopFansModel;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.Mainsharedprefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.successpoint.wingo.utils.CommonMethods.LogMe;

public class ProfilePresenter extends MvpBasePresenter<ProfileView> {
    ApiRequest apiRequest;
    Context context;
    ProfileView mView;
    private CallbackManager callbackManager;
    public ProfilePresenter(Context context, ProfileView mView) {
        this.context = context;
        apiRequest = ApiRequest.getInstance(context);
        this.mView = mView;
    }


    public void GetUserDataFromUserToken(String user_token) {

        HashMap<String, String> params = new HashMap<>();
//        params.put("user_token", "POsBddbjLmR3qazx");
        params.put("user_token", user_token);
        params.put("api_token", Constants.api_token);
        apiRequest.createPostRequest(Constants.UserProfileFromToken, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                Log.e("Done","response" + response);
                LogMe(response);
                JSONObject result = new JSONObject(response);
                if (result.getInt("code")==0){
                    mView.DataRetrieved(null,false);
                }
                else {
                    UserModelObject modelObject = new UserModelObject(result.getJSONObject("content"));
                    mView.DataRetrieved(modelObject,false);
                }
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }

}







