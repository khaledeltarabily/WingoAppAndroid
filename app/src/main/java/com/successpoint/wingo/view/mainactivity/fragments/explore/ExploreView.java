package com.successpoint.wingo.view.mainactivity.fragments.explore;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.json.JSONObject;

public interface ExploreView extends MvpView {
    void publishMainData(JSONObject jsonObject);
}