package com.successpoint.wingo.view.mainactivity.fragments.nearby;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.successpoint.wingo.model.NearbyModel;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;

import org.json.JSONException;

import java.util.HashMap;

import static com.successpoint.wingo.utils.CommonMethods.LogMe;

public class NearbyPresenter extends MvpBasePresenter<NearbyView> {
    Context context;
    NearbyView nearbyView;
    ApiRequest apiRequest;

    public NearbyPresenter(Context context, NearbyView nearbyView) {
        this.context = context;
        this.nearbyView = nearbyView;
        apiRequest = ApiRequest.getInstance(context);
    }

    public void getNearby(HashMap<String, String> params) {
        apiRequest.createPostRequest(Constants.Nearby, NearbyModel.class, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<NearbyModel>() {
            @Override
            public void onSuccess(NearbyModel response) throws JSONException {
                if (response.getCode() == 1) {
                    nearbyView.publishNearby(response.getContent());
                }
            }

            @Override
            public void onFail(ANError error) throws JSONException {
                LogMe(error.getResponse().message());
            }
        });
    }
}
