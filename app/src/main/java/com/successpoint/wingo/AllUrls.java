package com.successpoint.wingo;

import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

public class AllUrls {
    public static String GLOBALGIFTSIDS = "ALL_GIFTS/";
    public static String GetUserChatsUrl = "users/"+App.userModelObject_of_Project.getUser_id();
    public static String GetUserChatsUrlMine = "users/"+App.userModelObject_of_Project.getUser_id() + "/chats/";
    public static String GetUserChatsUrl(String user_id) {
        return "users/" + user_id + "/chats/";
    }
    public static String GetUserDataUrl (String user_id){
        return "users/"+user_id;}

    public static String GetChatsUrlById(String chat_id){
        return  "AllMainChats/"+chat_id;
    }

    public static String GetChatAllMessagesUrlById(String chat_id){
        return  "All_Massage_Of_Chat/"+chat_id;
    }
    public static String ListenToChatLastMessage(String chat_id){
        return  "AllMainChats/"+chat_id+"/last_message";
    }

    public static String GetMessageUrlFromMessageId(String message_id){
        return  "All_Messages/" + message_id;
    }



    public static String Listen_To_Viewers(String stream_id){
        return  "liveData/" + stream_id + "/Viewers/" + App.userModelObject_of_Project.getUser_id();
    }
  public static String Listen_To_Guests(String stream_id){
        return  "liveData/" + stream_id + "/accepted_guest/";
    } public static String Listen_To_WaitingGuests(String stream_id){
        return  "liveData/" + stream_id + "/Waiting_guest/" ;
    }


    public static String Listen_To_ViewersAll(String stream_id){
        Log.e("DATATA",stream_id+stream_id);
        return  "liveData/" + stream_id + "/Viewers/";
    }

    public static String Listen_To_Comments(String stream_id){

        return  "liveData/" + stream_id + "/Comments/";
    }
    public static String GiftsSend(String stream_id){

        return  "liveData/" + stream_id + "/Gifts/";
    }

}
