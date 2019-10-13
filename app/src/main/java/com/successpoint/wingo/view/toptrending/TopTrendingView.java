package com.successpoint.wingo.view.toptrending;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.successpoint.wingo.model.TopTrendingModel;

import java.util.List;

public interface TopTrendingView extends MvpView {
    void publishData(List<TopTrendingModel.Content> content);
    void DoneExecuted(boolean done);
}