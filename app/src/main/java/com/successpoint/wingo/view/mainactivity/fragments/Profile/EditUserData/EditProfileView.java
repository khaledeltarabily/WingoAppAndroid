package com.successpoint.wingo.view.mainactivity.fragments.Profile.EditUserData;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface EditProfileView extends MvpView {
    void DataEditedCorrectly(boolean done);
    void ImageUploadedCorrectly(boolean done);
}