package com.successpoint.wingo.view.mainactivity.fragments.LeaderBoard;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.successpoint.wingo.model.TopFansModel;

public interface LeaderBoardView extends MvpView {
    void publishTopFans(TopFansModel response,String type,String time);
}