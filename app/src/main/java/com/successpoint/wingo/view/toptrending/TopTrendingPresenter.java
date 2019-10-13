package com.successpoint.wingo.view.toptrending;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.successpoint.wingo.model.TopTrendingModel;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.Mainsharedprefs;

import org.json.JSONException;

import java.util.HashMap;

public class TopTrendingPresenter extends MvpBasePresenter<TopTrendingView> {
    Context context;
    TopTrendingView mView;
    ApiRequest apiRequest;

    public TopTrendingPresenter(Context context, TopTrendingView mView) {
        this.context = context;
        this.mView = mView;
        apiRequest = ApiRequest.getInstance(context);
    }

    public void getTopTrending() {
        HashMap<String, String> params = new HashMap<>();
        params.put("api_token", Constants.api_token);
        apiRequest.createPostRequest(Constants.TopTrending, TopTrendingModel.class, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<TopTrendingModel>() {
            @Override
            public void onSuccess(TopTrendingModel response) throws JSONException {
                if (response.getCode() == 1)
                    mView.publishData(response.getContent());
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }

    public void SetUsersToFollow(String user_id) {
        HashMap<String,String> params = new HashMap<>();
        params.put("api_token",Constants.api_token);
        params.put("user_token", Mainsharedprefs.getToken());
        Log.e("user_id", Mainsharedprefs.getToken()+"user_id");
        params.put("user_ids",user_id);
        apiRequest.createPostRequest(Constants.Follow, params, Priority.MEDIUM, new ApiRequest.ServiceCallback() {
            @Override
            public void onSuccess(Object response) throws JSONException {
                mView.DoneExecuted(true);
            }

            @Override
            public void onFail(ANError error) throws JSONException {


            }
        });
    }
}
