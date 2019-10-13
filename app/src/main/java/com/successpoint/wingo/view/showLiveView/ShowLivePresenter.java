package com.successpoint.wingo.view.showLiveView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.successpoint.wingo.AllUrls;
import com.successpoint.wingo.App;
import com.successpoint.wingo.model.CommentModel;
import com.successpoint.wingo.model.FollowingModel;
import com.successpoint.wingo.model.GiftModel;
import com.successpoint.wingo.model.GlobalGiftModel;
import com.successpoint.wingo.model.MessageModel;
import com.successpoint.wingo.model.TopFansModel;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.Mainsharedprefs;
import com.successpoint.wingo.utils.UtilsFirebase.DataFromFirebaseOnAdded;
import com.successpoint.wingo.utils.UtilsFirebase.DataFromFirebaseOnGet;
import com.successpoint.wingo.utils.UtilsFirebase.setDataToServer;
import com.successpoint.wingo.utils.UtilsFirebase.getDataFromServer;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.ActivityProfile.ProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.successpoint.wingo.utils.CommonMethods.LogMe;

public class ShowLivePresenter extends MvpBasePresenter<ShowLiveView> {
    Context context;
    ShowLiveView mView;
    ApiRequest apiRequest;

    public ShowLivePresenter(Context context, ShowLiveView mView) {
        this.context = context;
        this.mView = mView;
        apiRequest = ApiRequest.getInstance(context);
    }

    public void SetSeenView(String stream_id) {
        Log.e("DATADATA","Hereee"+stream_id);
        HashMap<String,String> params = new HashMap<>();
        params.put("name",App.userModelObject_of_Project.getName());
        if(App.userModelObject_of_Project.getImages().size()>0)
            params.put("image", App.userModelObject_of_Project.getImages().get(0));
        else
            params.put("image", "");
        params.put("Vip", String.valueOf(App.userModelObject_of_Project.getVip()));
        params.put("Bio", App.userModelObject_of_Project.getBio());
        Log.e("DATADATA","Herrrrreee"+stream_id);
        setDataToServer data = new setDataToServer(context, AllUrls.Listen_To_Viewers(stream_id),params);
        try {
            data.Data(new DataFromFirebaseOnAdded() {
                @Override
                public void onSuccess(Void aVoid) {

                }
                @Override
                public void onFailure(Exception e) {

                }
            });
        } catch (Exception e) {

        }
    }
    public void SetSeenToServerView(String stream_id) {
        HashMap<String,String> params = new HashMap<>();
        params.put("api_token",Constants.api_token);
        params.put("user_token", Mainsharedprefs.getToken());
        params.put("stream_id",stream_id);
        apiRequest.createPostRequest(Constants.MakeViewLiveAction, params, Priority.MEDIUM, new ApiRequest.ServiceCallback< String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                Log.e("DATADATA","Hereee"+stream_id);
                SetSeenView(stream_id);
            }

            @Override
            public void onFail(ANError error) throws JSONException {
                Log.e("DATADATA","HereeeFail"+error);
                SetSeenView(stream_id);

            }
        });
    }
    public void SetCloseToServerView(String stream_id) {
        HashMap<String,String> params = new HashMap<>();
        params.put("stream_id",stream_id);
        params.put("api_token",Constants.api_token);
        params.put("user_token", Mainsharedprefs.getToken());
        apiRequest.createPostRequest(Constants.MakeCloseLiveAction, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                SetCloseView(stream_id);
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }
    public void SetCloseView(String stream_id) {
        getDataFromServer data = new getDataFromServer(context, AllUrls.Listen_To_Viewers(stream_id));
        try {
            data.DataRemove(new DataFromFirebaseOnAdded() {
                @Override
                public void onSuccess(Void aVoid) {

                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        } catch (Exception e) {

        }
    }

    public void RetrievedTopFans(String user_id) {
        Log.e("DATATA","START DATA"+user_id);

        HashMap<String,String> params = new HashMap<>();
        params.put("api_token",Constants.api_token);
        params.put("user_token", Mainsharedprefs.getToken());
        params.put("user_id", user_id);
        apiRequest.createPostRequest(Constants.UserFans , params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                Log.e("DATATA","DATAdd"+response);

                JSONObject result = new JSONObject(response);
                ArrayList<UserModelObject> modelObject = new ArrayList<>();
                for (int i =0 ; i<result.getJSONArray("contnt").length() ; i++)
                    modelObject.add(new UserModelObject(result.getJSONArray("contnt").getJSONObject(i)));
                Log.e("DATATA","DATA"+modelObject.size());
                mView.RetrievedTopFans(modelObject);
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }
    public void RetrievedTodayTopFans(String user_id) {
        HashMap<String,String> params = new HashMap<>();
        params.put("api_token",Constants.api_token);
        params.put("user_token", Mainsharedprefs.getToken());
        params.put("user_id", user_id);
        apiRequest.createPostRequest(Constants.TodayTopFans, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                Log.e("DATATA","DATA"+response);
                JSONObject result = new JSONObject(response);
                ArrayList<UserModelObject> modelObject = new ArrayList<>();
                for (int i =0 ; i<result.getJSONArray("data").length() ; i++)
                    modelObject.add(new UserModelObject(result.getJSONArray("data").getJSONObject(i)));
                mView.RetrievedTodayTopFans(modelObject);
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }

    public void JoinAsGuest(String live_id) {
        Log.e("DATATA","DATAOiutside");
        HashMap<String,String> params = new HashMap<>();
        params.put("name",App.userModelObject_of_Project.getName());
        if (App.userModelObject_of_Project.getImages().size()>0)
            params.put("image", App.userModelObject_of_Project.getImages().get(0));
        else
            params.put("image", "");
        params.put("Vip", String.valueOf(App.userModelObject_of_Project.getVip()));
        params.put("Bio", App.userModelObject_of_Project.getBio());
        setDataToServer data = new setDataToServer(context, AllUrls.Listen_To_WaitingGuests(live_id) + "/" +App.userModelObject_of_Project.getUser_id(),params);
        try {
            data.Data(new DataFromFirebaseOnAdded() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("DATATA","DATAInternal");

                }
                @Override
                public void onFailure(Exception e) {
                    Log.e("DATATA","DATAEorror");
                }
            });
        } catch (Exception e) {

        }
    }
    public void RetrievedGuests(String live_id) {
        Log.e("DATATA","GUESTDATA");
        getDataFromServer data = new getDataFromServer(context, AllUrls.Listen_To_Guests(live_id));
        try {
            data.Data(new DataFromFirebaseOnGet() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    Log.e("DATATA","GUESTDATA"+dataSnapshot.toString());
                    ArrayList<UserModelObject> data_list = new ArrayList();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        ////// set data to commentsModel
                        data_list.add(new UserModelObject(data));
                    }
                    mView.RetrievedGuests(data_list);
                }

                @Override
                public void onCancel(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }
    }
    public void RetrievedWaitingGuests(String live_id) {
        Log.e("DATATA","GUESTDATA2");
        getDataFromServer data = new getDataFromServer(context, AllUrls.Listen_To_WaitingGuests(live_id));
        try {
            data.Data(new DataFromFirebaseOnGet() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    Log.e("DATATA","GUESTDATA2"+dataSnapshot.toString());
                    ArrayList<UserModelObject> data_list = new ArrayList();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        ////// set data to commentsModel
                        data_list.add(new UserModelObject(data));
                    }
                    mView.RetrievedWaitingGuests(data_list);
                }

                @Override
                public void onCancel(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }
    }
    public void RetrievedDeletedWaitingGuests(String live_id,UserModelObject user) {
        getDataFromServer data = new getDataFromServer(context, AllUrls.Listen_To_WaitingGuests(live_id));
        try {
            data.DataRemove(new DataFromFirebaseOnAdded() {
                @Override
                public void onSuccess(Void aVoid) {
                    mView.RetrievedDeletedWaitingGuests(user);
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        } catch (Exception e) {

        }
    }
    public void RetrievedDeletedGuests(String live_id,UserModelObject user) {
        getDataFromServer data = new getDataFromServer(context, AllUrls.Listen_To_Guests(live_id));
        try {
            data.DataRemove(new DataFromFirebaseOnAdded() {
                @Override
                public void onSuccess(Void aVoid) {
                    mView.RetrievedDeletedGuests(user);
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        } catch (Exception e) {

        }
    }

    public void GetClickedUserData(String user_id) {
        HashMap<String,String> params = new HashMap<>();
        params.put("api_token",Constants.api_token);
        params.put("user_token", Mainsharedprefs.getToken());
        params.put("user_id", user_id);
        apiRequest.createPostRequest(Constants.UserProfile, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                Log.e("DATATA","DATAHEREReturn"+user_id);
                JSONObject result = new JSONObject(response);
                UserModelObject modelObject = new UserModelObject(result.getJSONObject("content"));
                mView.GetClickedUserData(modelObject);
            }

            @Override
            public void onFail(ANError error) throws JSONException {
                Log.e("DATATA","Error"+user_id);

            }
        });
    }
    public void GetLastComments(String live_id) {
        getDataFromServer data = new getDataFromServer(context, AllUrls.Listen_To_Comments(live_id));
        try {
            data.DataChildLastOne(new DataFromFirebaseOnGet() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    ////// set data to commentsModel
                    mView.GetLastComments(new CommentModel(dataSnapshot));
                }

                @Override
                public void onCancel(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }
    }
    public void SetGiftToFirebase(GiftModel model,String live_id,int count_gift) {
        Log.e("Retrieved_is","DATATAoUTSIDExxxxxx"+live_id);

        HashMap<String,String> params = new HashMap<>();

        Log.e("DATAr",model.getImage_url());
        Log.e("DATAr",model.getGiftName());
        Log.e("DATAr",model.getGiftId());
        Log.e("DATAr",App.userModelObject_of_Project.getName());
        Log.e("DATAr",model.getImage_url());

        params.put("gift_image",model.getImage_url());
        params.put("GiftName",model.getGiftName());
        params.put("gift_id", model.getGiftId());
        params.put("gift_count", String.valueOf(count_gift));
        params.put("senderName", App.userModelObject_of_Project.getName());
        params.put("sender_image",model.getImage_url());
        setDataToServer data = new setDataToServer(context,AllUrls.GiftsSend(live_id) + FirebaseDatabase.getInstance().getReference().push().getKey()
                ,params);
        try {
            data.Data(new DataFromFirebaseOnAdded() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("Retrieved_is","DATATAoInsidexxx");

                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("Retrieved_is","DATATAoInsidexxx"+e.toString());
                }
            });
        } catch (Exception e) {

        }




        HashMap<String,String> paramsss = new HashMap<>();

        params.put("gift_image",model.getImage_url());
        params.put("GiftName",model.getGiftName());
        params.put("gift_id", model.getGiftId());
        params.put("gift_count", String.valueOf(model.getGiftCount()));
        params.put("senderName", App.userModelObject_of_Project.getName());
        params.put("sender_image",model.getImage_url());
        setDataToServer data_is = new setDataToServer(context, AllUrls.GLOBALGIFTSIDS+FirebaseDatabase.getInstance().getReference().push().getKey(),paramsss);
        try {
            data_is.Data(new DataFromFirebaseOnAdded() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("Retrieved_is","DATATAoInsiderr");


                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        } catch (Exception e) {

        }






        HashMap<String,String> param_id = new HashMap<>();
        param_id.put("api_token", Constants.api_token);
        param_id.put("user_token", Mainsharedprefs.getToken());
        param_id.put("gift_id",model.getGiftId());
        param_id.put("amount", String.valueOf(count_gift));
        param_id.put("stream_id",live_id);
        apiRequest.createPostRequest(Constants.SendGift + FirebaseDatabase.getInstance().getReference(), param_id, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                JSONObject result = new JSONObject(response);
                Log.e("Retrieved_is","DATATA");
                if (result.getInt("code") == 1) {



                }

            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });



    }

    public void AddComment(String live_id,String text,boolean isFly) {
        FirebaseDatabase.getInstance().getReference().push().getKey();

        String datas = FirebaseDatabase.getInstance().getReference().push().getKey();
        HashMap<String,String> params_param = new HashMap<>();
        if (App.userModelObject_of_Project.getImages().size() > 0)
            params_param.put("image", App.userModelObject_of_Project.getImages().get(0));
        else
            params_param.put("image", "");

        params_param.put("text",text);
        params_param.put("Vip", String.valueOf(App.userModelObject_of_Project.getVip()));
        params_param.put("user_id", App.userModelObject_of_Project.getUser_id());
        params_param.put("name", App.userModelObject_of_Project.getName());
        params_param.put("level", String.valueOf(App.userModelObject_of_Project.getLevel()));
        setDataToServer data = new setDataToServer(context, AllUrls.Listen_To_Comments(live_id) + datas ,params_param);
        try {
            data.Data(new DataFromFirebaseOnAdded() {
                @Override
                public void onSuccess(Void aVoid) {
                    FirebaseDatabase.getInstance().getReference().child(AllUrls.Listen_To_Comments(live_id)).child(datas).child("isFly")
                            .setValue(isFly);

                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        } catch (Exception e) {

        }

        HashMap<String,String> params = new HashMap<>();
        params.put("api_token", Constants.api_token);
        params.put("user_token", Mainsharedprefs.getToken());
        params.put("user_id",App.userModelObject_of_Project.getUser_id());
        params.put("stream_id",live_id);
        apiRequest.createPostRequest(Constants.DoneFlyComment, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                JSONObject result = new JSONObject(response);
                if (result.getInt("code") == 1) {

                }

            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });


    }
    public void GetAllCurrentViewers(String live_id) {
        Log.e("DATATA","retrieved");
        getDataFromServer data = new getDataFromServer(context, AllUrls.Listen_To_ViewersAll(live_id));
        try {
            data.Data(new DataFromFirebaseOnGet() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    Log.e("DATATA","retrieved"+dataSnapshot.toString());
                    ArrayList<UserModelObject> data = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ////// set data to commentsModel
                        data.add(new UserModelObject(ds));
                    }
                    mView.GetAllCurrentViewers(data);
                    GetDeletedAddedViewer(live_id);
                    GetNewAddedViewer(live_id);
                }

                @Override
                public void onCancel(DatabaseError databaseError) {
                    Log.e("DATATA","retrievedError");
                }
            });
        } catch (Exception e) {

        }
    }
    public void GetNewAddedViewer(String live_id) {
        getDataFromServer data = new getDataFromServer(context, AllUrls.Listen_To_ViewersAll(live_id));
        try {
            data.DataChildLastOne(new DataFromFirebaseOnGet() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    ////// set data to commentsModel
                    mView.GetNewAddedViewer(new UserModelObject((dataSnapshot)));
                }

                @Override
                public void onCancel(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }
    }
    public void GetDeletedAddedViewer(String live_id) {
        getDataFromServer data = new getDataFromServer(context, AllUrls.Listen_To_ViewersAll(live_id));
        try {
            data.DataChildLastOneDeleted(new DataFromFirebaseOnGet() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    ////// set data to commentsModel
                    mView.GetDeletedAddedViewer(new UserModelObject((dataSnapshot)));
                }

                @Override
                public void onCancel(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }
    }
    public void RetrieveGift(String live_id) {
        getDataFromServer data = new getDataFromServer(context, AllUrls.GiftsSend(live_id));
        try {
            data.DataChildLastOne(new DataFromFirebaseOnGet() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    ////// set data to commentsModel
                    mView.RetrieveGift(new GiftModel(dataSnapshot));
                }

                @Override
                public void onCancel(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }
    }
    public void RetrieveGlobalGift(String live_id) {
        ArrayList<String> list_arr = new ArrayList<>();
        getDataFromServer data = new getDataFromServer(context, AllUrls.GLOBALGIFTSIDS);
        try {
            data.DataChildLastOne(new DataFromFirebaseOnGet() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    ////// set data to commentsModel
                    mView.RetrieveGlobalGift(new GlobalGiftModel(dataSnapshot));

                }

                @Override
                public void onCancel(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }
    }
    public void RetrieveGlobalAllGift() {
        Log.e("DATATA","GiftsGet");

        apiRequest.createGetRequest(Constants.AllGifts, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                Log.e("DATATA","response" + response);
                JSONObject result = new JSONObject(response);
                ArrayList<GiftModel> modelObject = new ArrayList<>();
                for (int i =0 ; i<result.getJSONArray("data").length() ; i++)
                    modelObject.add(new GiftModel(result.getJSONArray("data").getJSONObject(i)));
                Log.e("DATATA","DATA"+modelObject.size());
                mView.GetGifts(modelObject);
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }
}
