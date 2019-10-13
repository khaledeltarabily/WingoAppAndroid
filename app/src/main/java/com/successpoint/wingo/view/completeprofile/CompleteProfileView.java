package com.successpoint.wingo.view.completeprofile;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.successpoint.wingo.model.UserModelObject;

public interface CompleteProfileView extends MvpView {
    void publishUpdateDone(UserModelObject userModelObject);
}