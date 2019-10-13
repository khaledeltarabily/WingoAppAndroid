package com.successpoint.wingo.view.searchresult;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.successpoint.wingo.model.SearchResultModel;

public interface SearchResultView extends MvpView {
    void publishResult(SearchResultModel response);
}