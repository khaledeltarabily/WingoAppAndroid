package com.successpoint.wingo.view.ChatFragment;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.successpoint.wingo.model.MainChats;
import com.successpoint.wingo.model.TopFansModel;
import com.successpoint.wingo.model.UserModelObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface ChatView extends MvpView {
    void GetAllChatsDoneFirst(ArrayList<MainChats> object);
}