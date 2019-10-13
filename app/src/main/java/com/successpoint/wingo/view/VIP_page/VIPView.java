package com.successpoint.wingo.view.VIP_page;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.successpoint.wingo.model.TopFansModel;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.model.VipModel;

public interface VIPView extends MvpView {
    void BuyDone(boolean Done);
    void DataRetrieved(VipModel data);
}