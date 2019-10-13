package com.successpoint.wingo.view.createpassword;

import android.content.Context;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;

import org.json.JSONException;

import java.util.HashMap;

public class CreatePasswordPresenter extends MvpBasePresenter<CreatePasswordView> {
    Context context;
    CreatePasswordView mView;
    ApiRequest apiRequest;

    public CreatePasswordPresenter(Context context, CreatePasswordView mView) {
        this.context = context;
        this.mView = mView;
        apiRequest = ApiRequest.getInstance(context);
    }

    public void signup(HashMap<String, String> params) {
        apiRequest.createPostRequest(Constants.PhoneLogin, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                mView.publishResponse(response);
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }
}
