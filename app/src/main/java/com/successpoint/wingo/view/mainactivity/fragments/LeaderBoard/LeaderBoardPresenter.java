package com.successpoint.wingo.view.mainactivity.fragments.LeaderBoard;

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

public class LeaderBoardPresenter extends MvpBasePresenter<LeaderBoardView> {
    Context context;
    LeaderBoardView mView;
    ApiRequest apiRequest;

    public LeaderBoardPresenter(Context context, LeaderBoardView mView) {
        this.context = context;
        this.mView = mView;
        apiRequest = ApiRequest.getInstance(context);
    }
    public void GetLeaderBoard(String type,String time) {
        HashMap<String,String> params = new HashMap<>();
        params.put("api_token", Constants.api_token);
        params.put("user_token", Mainsharedprefs.getToken());
        params.put("time", time);
        apiRequest.createPostRequest(Constants.LeaderBoard+type, TopFansModel.class, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<TopFansModel>() {
            @Override
            public void onSuccess(TopFansModel response) throws JSONException {
                Log.e("FFFF",response.toString());
                mView.publishTopFans(response,type,time);
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }


}
