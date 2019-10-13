package com.successpoint.wingo.view.mainactivity.fragments.popular;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.json.JSONObject;

public interface PopularView extends MvpView {
    void publishMainData(JSONObject result);
}