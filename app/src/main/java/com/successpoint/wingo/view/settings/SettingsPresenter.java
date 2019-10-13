package com.successpoint.wingo.view.settings;

import android.content.Context;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.successpoint.wingo.utils.CommonMethods.LogMe;

public class SettingsPresenter extends MvpBasePresenter<SettingsView> {
    ApiRequest apiRequest;
    Context context;
    SettingsView mView;
    public SettingsPresenter(Context context, SettingsView mView) {
        this.context = context;
        apiRequest = ApiRequest.getInstance(context);
        this.mView = mView;
    }

    public void ChangeSettingSwitch(String user_token,String Url_type) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_token", user_token);
        params.put("api_token", Constants.api_token);
        apiRequest.createPostRequest(Constants.SettingsData+Url_type, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                LogMe(response);
                JSONObject result = new JSONObject(response);
                if (result.getInt("code")==1){
                    mView.DataRetrieved(true);
                }
                else {
                    mView.DataRetrieved(false);
                }

            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }

}
