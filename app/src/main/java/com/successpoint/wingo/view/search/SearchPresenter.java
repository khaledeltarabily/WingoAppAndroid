package com.successpoint.wingo.view.search;

import android.content.Context;
import android.view.View;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.successpoint.wingo.model.FollowingModel;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.Mainsharedprefs;

import org.json.JSONException;

import java.util.HashMap;

public class SearchPresenter extends MvpBasePresenter<SearchView> {
    Context context;
    SearchView mView;
    ApiRequest apiRequest;

    public SearchPresenter(Context context, SearchView mView) {
        this.context = context;
        this.mView = mView;
        apiRequest = ApiRequest.getInstance(context);
    }

    public void getFollows() {
        HashMap<String,String> params = new HashMap<>();
        params.put("api_token",Constants.api_token);
        params.put("user_token", Mainsharedprefs.getToken());
        apiRequest.createPostRequest(Constants.Following, FollowingModel.class, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<FollowingModel>() {
            @Override
            public void onSuccess(FollowingModel response) throws JSONException {
                mView.publishFollowing(response);
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }
    public void setFollowers(String user_id) {
        HashMap<String,String> params = new HashMap<>();
        params.put("api_token",Constants.api_token);
        params.put("user_token", Mainsharedprefs.getToken());
        params.put("user_id",user_id);
        apiRequest.createPostRequest(Constants.Follow, params, Priority.MEDIUM, new ApiRequest.ServiceCallback() {
            @Override
            public void onSuccess(Object response) throws JSONException {
//                tvFollowing.setVisibility(View.VISIBLE);
//                ivFollow.setVisibility(View.GONE);
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }
}
