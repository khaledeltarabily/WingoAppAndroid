package com.successpoint.wingo.view.mainactivity.fragments.Profile.AllActivitiesUserList;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.successpoint.wingo.model.TopFansModel;

public interface UserListDataView extends MvpView {
    void publishTopFans(TopFansModel response);
}