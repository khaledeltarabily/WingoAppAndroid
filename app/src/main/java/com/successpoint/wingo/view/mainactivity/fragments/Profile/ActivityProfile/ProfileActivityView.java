package com.successpoint.wingo.view.mainactivity.fragments.Profile.ActivityProfile;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.successpoint.wingo.model.MainChats;
import com.successpoint.wingo.model.TopFansModel;
import com.successpoint.wingo.model.UserModelObject;

public interface ProfileActivityView extends MvpView {
    void DataRetrieved(UserModelObject user_object);
    void TopContributesRetrieved(TopFansModel model);
    void retrievedchatData(MainChats data);
}