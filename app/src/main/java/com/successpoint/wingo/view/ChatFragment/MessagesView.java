package com.successpoint.wingo.view.ChatFragment;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.successpoint.wingo.model.MainChats;
import com.successpoint.wingo.model.MessageModel;
import com.successpoint.wingo.model.UserModelObject;

import java.util.ArrayList;

public interface MessagesView extends MvpView {
    void GetNewChatAdded(ArrayList<MessageModel> object);
}