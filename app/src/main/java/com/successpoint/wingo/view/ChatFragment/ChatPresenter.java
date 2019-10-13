package com.successpoint.wingo.view.ChatFragment;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.successpoint.wingo.AllUrls;
import com.successpoint.wingo.App;
import com.successpoint.wingo.model.MainChats;
import com.successpoint.wingo.model.MessageModel;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.utils.ApiRequest;

import java.util.ArrayList;

import com.successpoint.wingo.utils.UtilsFirebase.DataFromFirebaseOnGet;
import com.successpoint.wingo.utils.UtilsFirebase.getDataFromServer;

public class ChatPresenter extends MvpBasePresenter<ChatView> {
    ApiRequest apiRequest;
    Context context;
    ChatView mView;
    public ChatPresenter(Context context, ChatView mView) {
        this.context = context;
        apiRequest = ApiRequest.getInstance(context);
        this.mView = mView;
    }
    public void getAllChatsFirst() {
        ArrayList<MainChats> list_arr = new ArrayList<>();
        getDataFromServer data = new getDataFromServer(context, AllUrls.GetUserChatsUrlMine);
        try {
            data.Data(new DataFromFirebaseOnGet() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.e("DONE","Data"+ds.getKey());
                        getDataFromServer data_inside = new getDataFromServer(context, AllUrls.GetChatsUrlById(ds.getKey()));
                        try {
                            data_inside.Data(new DataFromFirebaseOnGet() {
                                @Override
                                public void onSuccess(DataSnapshot dss) {
                                    if (dss.getValue() == null)
                                        return;
                                        Log.e("DONE",dss.getValue().toString());
                                        Log.e("DONE",App.userModelObject_of_Project.getUser_id()+"ID");
                                        String user_id = dss.child("sender_Id").getValue().toString();
                                        if (user_id.equals(App.userModelObject_of_Project.getUser_id())) {
                                            Log.e("EQUALLL","recevier_Id"+dss.child("recevier_Id").getValue().toString());
                                            user_id = dss.child("recevier_Id").getValue().toString();
                                        }
                                        else {
                                            Log.e("EQUALLL","sender_Id");
                                        }
                                        getDataFromServer data_inside = new getDataFromServer(context, AllUrls.GetUserDataUrl(user_id));
                                        try {
                                            data_inside.Data(new DataFromFirebaseOnGet() {
                                                @Override
                                                public void onSuccess(DataSnapshot dataSnapshot) {
                                                    if (!dss.hasChild("last_message")){
                                                        MainChats item = new MainChats(dss,dataSnapshot,dataSnapshot);
                                                        list_arr.add(item);
                                                        mView.GetAllChatsDoneFirst(list_arr);
                                                        return;
                                                    }
                                                    getDataFromServer data_inside = new getDataFromServer(context, AllUrls.GetMessageUrlFromMessageId(dss.child("last_message").getValue().toString()));
                                                    try {
                                                        data_inside.Data(new DataFromFirebaseOnGet() {
                                                            @Override
                                                            public void onSuccess(DataSnapshot dataSnapshot_last_message) {
                                                                MainChats item = new MainChats(dss,dataSnapshot_last_message,dataSnapshot);
                                                                list_arr.add(item);
                                                                mView.GetAllChatsDoneFirst(list_arr);
                                                            }

                                                            @Override
                                                            public void onCancel(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    } catch (Exception e) {

                                                    }

                                                }

                                                @Override
                                                public void onCancel(DatabaseError databaseError) {

                                                }
                                            });
                                        } catch (Exception e) {

                                        }

                                }

                                @Override
                                public void onCancel(DatabaseError databaseError) {

                                }
                            });
                        } catch (Exception e) {

                        }
                    }

                }

                @Override
                public void onCancel(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }

    }

    public void GetChatMessages() {
        ArrayList<MessageModel> list_arr = new ArrayList<>();
        getDataFromServer data = new getDataFromServer(context, AllUrls.GetUserChatsUrl);
        try {
            data.Data(new DataFromFirebaseOnGet() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        getDataFromServer data_inside = new getDataFromServer(context, AllUrls.GetChatsUrlById(ds.getValue().toString()));
                        try {
                            data_inside.Data(new DataFromFirebaseOnGet() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                                        MessageModel item = dss.getValue(MessageModel.class);
//                                        item.setChat_id(dss.getKey());
//                                        list_arr.add(item);
//                                        mView.Get(list_arr);
                                    }

                                }

                                @Override
                                public void onCancel(DatabaseError databaseError) {

                                }
                            });
                        } catch (Exception e) {

                        }
                    }

                }

                @Override
                public void onCancel(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }

    }
}
