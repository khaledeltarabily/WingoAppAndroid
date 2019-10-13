package com.successpoint.wingo.view.VIP_page;

import  android.content.Context;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.facebook.CallbackManager;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.successpoint.wingo.model.FollowingModel;
import com.successpoint.wingo.model.VipModel;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.Mainsharedprefs;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class VIPPresenter extends MvpBasePresenter<VIPView> {
    ApiRequest apiRequest;
    Context context;
    VIPView mView;
    private CallbackManager callbackManager;
    public VIPPresenter(Context context, VIPView mView) {
        this.context = context;
        apiRequest = ApiRequest.getInstance(context);
        this.mView = mView;
    }

    public void getVipData() {
        HashMap<String,String> params = new HashMap<>();
        params.put("api_token",Constants.api_token);
        params.put("user_token", Mainsharedprefs.getToken());
        apiRequest.createPostRequest(Constants.Following, VipModel.class, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<VipModel>() {
            @Override
            public void onSuccess(VipModel response) throws JSONException {
                mView.DataRetrieved(response);
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }
    public void BuyData(int value) {
        HashMap<String,String> params = new HashMap<>();
        params.put("api_token",Constants.api_token);
        params.put("user_token", Mainsharedprefs.getToken());
        params.put("vip",value+"");
        apiRequest.createPostRequest(Constants.BuyData, String.class, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                JSONObject result = new JSONObject(response);
                if (result.getInt("code") == 1)
                    mView.BuyDone(true);
                else
                    mView.BuyDone(false);
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }
    public void BuyDataRenew(int value) {
        HashMap<String,String> params = new HashMap<>();
        params.put("api_token",Constants.api_token);
        params.put("user_token", Mainsharedprefs.getToken());
        params.put("vip",value+"");
        apiRequest.createPostRequest(Constants.RenewData, String.class, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                JSONObject result = new JSONObject(response);
                if (result.getInt("code") == 1)
                    mView.BuyDone(true);
                else
                    mView.BuyDone(false);
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }

}