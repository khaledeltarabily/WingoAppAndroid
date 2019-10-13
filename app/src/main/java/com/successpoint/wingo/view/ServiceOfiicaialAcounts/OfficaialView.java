package com.successpoint.wingo.view.ServiceOfiicaialAcounts;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.successpoint.wingo.model.OfficaialAcounts;
import com.successpoint.wingo.model.TopTrendingModel;

import java.util.List;

public interface OfficaialView extends MvpView {
    void publishData(List<OfficaialAcounts> content);
    void DoneExecuted(boolean done);
}