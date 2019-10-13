package com.successpoint.wingo.view.mainactivity.fragments.popular;

import android.content.Context;
import android.widget.Toast;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.successpoint.wingo.model.PopularModel;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.successpoint.wingo.utils.CommonMethods.LogMe;

public class PopularPresenter extends MvpBasePresenter<PopularView> {
    Context context;
    PopularView mView;
    ApiRequest apiRequest;

    public PopularPresenter(Context context, PopularView mView) {
        this.context = context;
        this.mView = mView;
        apiRequest = ApiRequest.getInstance(context);

    }

    public void getPopularList(HashMap<String,String> params) {
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
