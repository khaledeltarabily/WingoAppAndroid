package com.successpoint.wingo.model;

import com.google.firebase.database.DataSnapshot;
import com.successpoint.wingo.App;

import java.io.Serializable;

public class MainChats implements Serializable {
    UserModelObject user_data , receiver_data;
    String chat_id;
    MessageModel last_Message;
    String sender_Id,recevier_Id;

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public MessageModel getLast_Message() {
        return last_Message;
    }

    public void setLast_Message(MessageModel last_Message) {
        this.last_Message = last_Message;
    }

    public UserModelObject getUser_data() {
        return user_data;
    }

    public void setUser_data(UserModelObject user_data) {
        this.user_data = user_data;
    }

    public UserModelObject getReceiver_data() {
        return receiver_data;
    }

    public void setReceiver_data(UserModelObject receiver_data) {
        this.receiver_data = receiver_data;
    }

    public String getRecevier_Id() {
        return recevier_Id;
    }

    public String getSender_Id() {
        return sender_Id;
    }

    public void setRecevier_Id(String recevier_Id) {
        this.recevier_Id = recevier_Id;
    }

    public void setSender_Id(String sender_Id) {
        this.sender_Id = sender_Id;
    }

    public MainChats(){}
    public MainChats(DataSnapshot dataSnapshot, DataSnapshot message_model, DataSnapshot user_model){
        sender_Id = dataSnapshot.child("sender_Id").getValue().toString();
        recevier_Id = dataSnapshot.child("recevier_Id").getValue().toString();
        if (user_model==message_model){
            MessageModel model = new MessageModel();
            model.setMessage_time("now");
            model.setMessage_text("");
            last_Message = model;
        }
        else
        last_Message = new MessageModel(message_model);
        if (recevier_Id.equals(App.userModelObject_of_Project.getUser_id())) {
            user_data = App.userModelObject_of_Project;
            receiver_data = new UserModelObject(user_model);
        }
        else {
            receiver_data = App.userModelObject_of_Project;
            user_data = new UserModelObject(user_model);
        }
        chat_id = dataSnapshot.getKey();

    }
    public MainChats(DataSnapshot dataSnapshot, UserModelObject user_model){
        sender_Id = dataSnapshot.child("sender_Id").getValue().toString();
        recevier_Id = dataSnapshot.child("recevier_Id").getValue().toString();
        if (recevier_Id.equals(App.userModelObject_of_Project.getUser_id())) {
            receiver_data = App.userModelObject_of_Project;
            user_data = user_model;
        }
        else {
            user_data = App.userModelObject_of_Project;
            receiver_data = user_model;
        }
        chat_id = dataSnapshot.getKey();

    }
}
