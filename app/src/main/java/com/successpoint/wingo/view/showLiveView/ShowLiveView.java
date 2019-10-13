package com.successpoint.wingo.view.showLiveView;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.successpoint.wingo.model.CommentModel;
import com.successpoint.wingo.model.GiftModel;
import com.successpoint.wingo.model.GlobalGiftModel;
import com.successpoint.wingo.model.TopFansModel;
import com.successpoint.wingo.model.UserModelObject;

import java.util.ArrayList;

public interface ShowLiveView extends MvpView {
    void RetrievedTopFans(ArrayList<UserModelObject> data);
    void RetrievedTodayTopFans(ArrayList<UserModelObject> data);
    void RetrievedGuests(ArrayList<UserModelObject> data);
    void RetrievedDeletedGuests(UserModelObject data);
    void RetrievedWaitingGuests(ArrayList<UserModelObject> data);
    void RetrievedDeletedWaitingGuests(UserModelObject data);
    void GetGifts(ArrayList<GiftModel> model);
    void GetClickedUserData(UserModelObject response);
    void GetAllCurrentViewers(ArrayList<UserModelObject> model);
    void GetNewAddedViewer(UserModelObject user);
    void GetDeletedAddedViewer(UserModelObject user);
    void GetLastComments(CommentModel model);

    void RetrieveGift(GiftModel model);
    void RetrieveGlobalGift(GlobalGiftModel model);
    void RetrieveVipUser(UserModelObject model);
    void SelectedGiftPosition(int position);
}