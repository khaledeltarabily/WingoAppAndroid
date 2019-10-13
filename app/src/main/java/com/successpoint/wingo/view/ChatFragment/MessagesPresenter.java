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

import com.successpoint.wingo.utils.UtilsFirebase.DataFromFirebaseOnAdded;
import com.successpoint.wingo.utils.UtilsFirebase.DataFromFirebaseOnGet;
import com.successpoint.wingo.utils.UtilsFirebase.getDataFromServer;
import com.successpoint.wingo.utils.UtilsFirebase.setDataToServer;
public class MessagesPresenter extends MvpBasePresenter<MessagesView> {
    ApiRequest apiRequest;
    Context context;
    MessagesView mView;
    public MessagesPresenter(Context context, MessagesView mView) {
        this.context = context;
        apiRequest = ApiRequest.getInstance(context);
        this.mView = mView;
    }

    public void GetChatMessages(String chat_id) {
        ArrayList<MessageModel> list_arr = new ArrayList<>();
        Log.e("chat_id",chat_id+":chat_id");
        getDataFromServer data = new getDataFromServer(context, AllUrls.GetChatAllMessagesUrlById(chat_id));
        try {
            data.Data(new DataFromFirebaseOnGet() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshott) {
                    final int[] total_be = {0};
                    for (DataSnapshot ds : dataSnapshott.getChildren()) {
                        Log.e("RESData",ds.toString()+"Data");
                        getDataFromServer data_inside = new getDataFromServer(context, AllUrls.GetMessageUrlFromMessageId(ds.getKey()));
                        try {
                            data_inside.Data(new DataFromFirebaseOnGet() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    Log.e("RESData",dataSnapshot.toString()+"Data");
                                    MessageModel item = new MessageModel(dataSnapshot);
                                    list_arr.add(item);
                                    total_be[0]++;
                                    if (total_be[0] ==dataSnapshott.getChildrenCount()) {
                                        Log.e("TTTOTAL",list_arr.size()+"Size");
                                        mView.GetNewChatAdded(list_arr);
                                        GetNewMessageAdded(chat_id);
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
                    Log.e("RESData",databaseError.toString()+"Data");

                }
            });
        } catch (Exception e) {
            Log.e("RESData",e.toString()+"Data");

        }

    }
    public void GetNewMessageAdded(String chat_id) {
        Log.e("chat_id",chat_id+":chat_id");
        getDataFromServer data = new getDataFromServer(context, AllUrls.GetChatAllMessagesUrlById(chat_id));
        try {
            data.DataChildLastOne(new DataFromFirebaseOnGet() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    getDataFromServer data_inside = new getDataFromServer(context, AllUrls.GetMessageUrlFromMessageId(dataSnapshot.getKey()));
                    try {
                        data_inside.Data(new DataFromFirebaseOnGet() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                ArrayList<MessageModel> list_arr = new ArrayList<>();
                                Log.e("RESData",dataSnapshot.toString()+"Data");
                                MessageModel item = new MessageModel(dataSnapshot);
                                list_arr.add(item);
                                mView.GetNewChatAdded(list_arr);

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
                    Log.e("RESData",databaseError.toString()+"Data");

                }
            });
        } catch (Exception e) {
            Log.e("RESData",e.toString()+"Data");

        }

    }
}
