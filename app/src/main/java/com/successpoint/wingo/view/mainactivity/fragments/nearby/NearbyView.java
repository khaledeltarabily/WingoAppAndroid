package com.successpoint.wingo.view.mainactivity.fragments.nearby;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.successpoint.wingo.model.NearbyModel;

import java.util.List;

public interface NearbyView extends MvpView {
    void publishNearby(List<NearbyModel.Content> content);
}