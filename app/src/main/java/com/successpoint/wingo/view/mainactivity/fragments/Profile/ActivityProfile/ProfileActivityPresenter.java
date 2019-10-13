package com.successpoint.wingo.view.mainactivity.fragments.Profile.ActivityProfile;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.successpoint.wingo.AllUrls;
import com.successpoint.wingo.App;
import com.successpoint.wingo.model.MainChats;
import com.successpoint.wingo.model.TopFansModel;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.UtilsFirebase.DataFromFirebaseOnAdded;
import com.successpoint.wingo.utils.UtilsFirebase.DataFromFirebaseOnGet;
import com.successpoint.wingo.utils.UtilsFirebase.getDataFromServer;
import com.successpoint.wingo.utils.UtilsFirebase.setDataToServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import static com.successpoint.wingo.utils.CommonMethods.LogMe;

    public class ProfileActivityPresenter extends MvpBasePresenter<ProfileActivityView> {
    ApiRequest apiRequest;
    Context context;
    ProfileActivityView mView;
    public ProfileActivityPresenter(Context context, ProfileActivityView mView) {
        this.context = context;
        apiRequest = ApiRequest.getInstance(context);
        this.mView = mView;
    }
    public void getContributorsByUser_id(String user_id ) {
        HashMap<String,String> params = new HashMap<>();
        params.put("api_token", Constants.api_token);
        params.put("user_token", App.userModelObject_of_Project.getUser_token());
        if (!user_id.equals(""))
            params.put("user_id", user_id);
        apiRequest.createPostRequest(Constants.Contributions, TopFansModel.class, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<TopFansModel>() {
            @Override
            public void onSuccess(TopFansModel response) throws JSONException {
                mView.TopContributesRetrieved(response);
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }
        public void getAllChatsFirst(String another_user_id,UserModelObject user_data) {
            ArrayList<MainChats> list_arr = new ArrayList<>();
            Log.e("Clicked","User_id"+another_user_id);
            Log.e("Clicked","User_id"+App.userModelObject_of_Project.getUser_id());
            getDataFromServer data = new getDataFromServer(context, AllUrls.GetUserChatsUrl);
            try {
                data.Data(new DataFromFirebaseOnGet() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshots) {
                        Log.e("Clicked","retrieved");

                        DataSnapshot dataSnapshot = dataSnapshots.child("chats");
                        String chat_id_is_be = "";
                        if (dataSnapshot.hasChild(App.userModelObject_of_Project.getUser_id()+"Chat"+another_user_id)){
                            chat_id_is_be = App.userModelObject_of_Project.getUser_id()+"Chat"+another_user_id ;
                            Log.e("Clicked","Here"+chat_id_is_be);
                        }
                        else if (dataSnapshot.hasChild(another_user_id +"Chat"+ App.userModelObject_of_Project.getUser_id())){
                            chat_id_is_be = another_user_id +"Chat"+ App.userModelObject_of_Project.getUser_id();
                            Log.e("Clicked","Here"+chat_id_is_be);
                        }
                        if (chat_id_is_be.equals("")) {
                            Log.e("Clicked","Here");
                            CreateNewChat(another_user_id,user_data);
                            return;
                        }
                        else {
                            Log.e("Clicked","HereNot");
                            getDataFromServer data_inside = new getDataFromServer(context, AllUrls.GetChatsUrlById(chat_id_is_be));
                            try {
                                data_inside.Data(new DataFromFirebaseOnGet() {
                                    @Override
                                    public void onSuccess(DataSnapshot dss) {
                                        if (dss.getValue() != null) {
                                            Log.e("DONE", dss.getValue().toString());
                                            String user_id = dss.child("sender_id").getValue().toString();
                                            if (user_id.equals(App.userModelObject_of_Project.getUser_id()))
                                                user_id = dss.child("receiver_id").getValue().toString();
                                            getDataFromServer data_inside = new getDataFromServer(context, AllUrls.GetUserDataUrl(user_id));
                                            try {
                                                String finalUser_id = user_id;
                                                data_inside.Data(new DataFromFirebaseOnGet() {
                                                    @Override
                                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                                        MainChats item = new MainChats(dataSnapshot, user_data);
                                                        Log.e("CCCCCX", finalUser_id +"Chat"+ App.userModelObject_of_Project.getUser_id());
                                                        Log.e("CCCCCX",item.getChat_id()+"Null");
                                                        mView.retrievedchatData(item);
                                                    }

                                                    @Override
                                                    public void onCancel(DatabaseError databaseError) {

                                                    }
                                                });
                                            } catch (Exception e) {

                                            }
                                        } else {

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


        public void CreateNewChat(String user_id,UserModelObject user_data) {
            MainChats model = new MainChats();
            model.setSender_Id(App.userModelObject_of_Project.getUser_id());
            model.setRecevier_Id(user_id);
            Log.e("Clicked","HereHH");
            Log.e("Clicked",App.userModelObject_of_Project.getUser_id() + "ID");
            Log.e("Clicked","ID");
            setDataToServer data = new setDataToServer(context, AllUrls.GetChatsUrlById(user_id+"Chat"+ App.userModelObject_of_Project.getUser_id()),model);
            try {
                data.Data(new DataFromFirebaseOnAdded() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("Clicked","Doneeeee");
                        setDataToServer data = new setDataToServer(context, AllUrls.GetUserChatsUrl(user_id)+"/"+user_id+"Chat"+ App.userModelObject_of_Project.getUser_id()
                                ,user_id+"Chat"+ App.userModelObject_of_Project.getUser_id());
                        try {
                            data.Data(new DataFromFirebaseOnAdded() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    setDataToServer data = new setDataToServer(context, AllUrls.GetUserChatsUrlMine+user_id+"Chat"+ App.userModelObject_of_Project.getUser_id()
                                            ,user_id+"Chat"+ App.userModelObject_of_Project.getUser_id());
                                    try {
                                        data.Data(new DataFromFirebaseOnAdded() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                model.setUser_data(user_data);
                                                model.setChat_id(user_id+"Chat"+ App.userModelObject_of_Project.getUser_id());
                                                Log.e("CCCCC",user_id+"Chat"+ App.userModelObject_of_Project.getUser_id());
                                                Log.e("CCCCC",model.getChat_id()+"Null");
                                                mView.retrievedchatData(model);

                                            }

                                            @Override
                                            public void onFailure(Exception e) {

                                            }
                                        });
                                    } catch (Exception e) {
                                        Log.e("Clicked","HereHH"+e.toString());

                                    }
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Log.e("Clicked","HereHH"+e.toString());

                                }
                            });
                        } catch (Exception e) {
                            Log.e("Clicked","HereHH"+e.toString());
                        }

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e("Clicked","HereHH"+e.toString());
                    }
                });
            } catch (Exception e) {
                Log.e("Clicked","HereHH"+e.toString());

            }

        }

    }