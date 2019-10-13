package com.successpoint.wingo.view.mainactivity.fragments.explore;

import android.content.Context;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.view.mainactivity.fragments.popular.PopularView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ExplorePresenter extends MvpBasePresenter<ExploreView> {
    Context context;
    ExploreView mView;
    ApiRequest apiRequest;

    public ExplorePresenter(Context context, ExploreView mView) {
        this.context = context;
        this.mView = mView;
        apiRequest = ApiRequest.getInstance(context);

    }
    public void getHotLiveList(HashMap<String, String> params) {
        apiRequest.createPostRequest(Constants.Popular,  params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getInt("code") == 1){
                    mView.publishMainData(jsonObject);
                }
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }
}
