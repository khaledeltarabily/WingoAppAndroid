package com.successpoint.wingo.view.createpassword;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface CreatePasswordView extends MvpView {
    void publishResponse(String response);
}