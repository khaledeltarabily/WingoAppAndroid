package com.successpoint.wingo.view.ServiceOfiicaialAcounts;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.successpoint.wingo.model.OfficaialAcounts;
import com.successpoint.wingo.model.ResponseOfficaialAcounts;
import com.successpoint.wingo.model.TopTrendingModel;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;

import org.json.JSONException;

import java.util.HashMap;

public class OfficailAcountsPresenter extends MvpBasePresenter<OfficaialView> {
    Context context;
    OfficaialView mView;
    ApiRequest apiRequest;

    public OfficailAcountsPresenter(Context context, OfficaialView mView) {
        this.context = context;
        this.mView = mView;
        apiRequest = ApiRequest.getInstance(context);
    }

    public void getOfficaialAcounts() {
        HashMap<String, String> params = new HashMap<>();
        params.put("api_token", Constants.api_token);
        apiRequest.createPostRequest(Constants.officialaccounts, ResponseOfficaialAcounts.class, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<ResponseOfficaialAcounts>() {
            @Override
            public void onSuccess(ResponseOfficaialAcounts response) throws JSONException {
                Log.e("Data","EEEEERORno");
                if (response.getCode() == 1)
                    mView.publishData(response.getContent());
            }

            @Override
            public void onFail(ANError error) throws JSONException {
                Log.e("Data","EEEEEROR");
            }
        });
    }

}
