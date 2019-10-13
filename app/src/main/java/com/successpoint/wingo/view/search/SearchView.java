package com.successpoint.wingo.view.search;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.successpoint.wingo.model.FollowingModel;

public interface SearchView extends MvpView {
    void publishFollowing(FollowingModel response);
}