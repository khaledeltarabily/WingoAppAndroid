package com.successpoint.wingo.view.mainactivity.fragments.Profile.AllActivitiesUserList;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.successpoint.wingo.model.TopFansModel;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.Mainsharedprefs;

import org.json.JSONException;

import java.util.HashMap;

public class UserListDataPresenter extends MvpBasePresenter<UserListDataView> {
    Context context;
    UserListDataView mView;
    ApiRequest apiRequest;

    public UserListDataPresenter(Context context, UserListDataView mView) {
        this.context = context;
        this.mView = mView;
        apiRequest = ApiRequest.getInstance(context);
    }

    public void getActivityByText(String type) {
        HashMap<String,String> params = new HashMap<>();
        params.put("api_token", Constants.api_token);
        params.put("user_token", Mainsharedprefs.getToken());
        apiRequest.createPostRequest(Constants.ActivitiesUrl(type), TopFansModel.class, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<TopFansModel>() {
            @Override
            public void onSuccess(TopFansModel response) throws JSONException {
                mView.publishTopFans(response);
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }
    public void getActivityByTextByUser_id(String type,String User_id) {
        HashMap<String,String> params = new HashMap<>();
        params.put("api_token", Constants.api_token);
        params.put("user_token", Mainsharedprefs.getToken());
        Log.e("GGGG",User_id+"uSERid");
        params.put("user_id", User_id);
        apiRequest.createPostRequest(Constants.ActivitiesUrl(type), TopFansModel.class, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<TopFansModel>() {
            @Override
            public void onSuccess(TopFansModel response) throws JSONException {
                mView.publishTopFans(response);
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }
}
