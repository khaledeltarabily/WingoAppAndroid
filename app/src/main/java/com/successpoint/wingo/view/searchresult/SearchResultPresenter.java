package com.successpoint.wingo.view.searchresult;

import android.content.Context;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.successpoint.wingo.model.SearchResultModel;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.Mainsharedprefs;

import org.json.JSONException;

import java.util.HashMap;

public class SearchResultPresenter extends MvpBasePresenter<SearchResultView> {
    Context context;
    SearchResultView mView;
    ApiRequest apiRequest;

    public SearchResultPresenter(Context context, SearchResultView mView) {
        this.context = context;
        this.mView = mView;
        apiRequest = ApiRequest.getInstance(context);
    }

    public void startSearch(String quary) {
        HashMap<String,String> params = new HashMap<>();
        params.put("api_token", Constants.api_token);
        params.put("user_token", Mainsharedprefs.getToken());
        params.put("query", quary);
        apiRequest.createPostRequest(Constants.Search, SearchResultModel.class, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<SearchResultModel>() {
            @Override
            public void onSuccess(SearchResultModel response) throws JSONException {
                mView.publishResult(response);
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }
}
