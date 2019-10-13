package com.successpoint.wingo.view.mainsign;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.successpoint.wingo.model.SocialLoginModel;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

import static com.successpoint.wingo.utils.CommonMethods.LogMe;

public class MainSignPresenter extends MvpBasePresenter<MainSignView> {
    Context context;
    MainSignView mainSignView;
    ApiRequest apiRequest;
    private CallbackManager callbackManager;

    public MainSignPresenter(Context context, MainSignView mainSignView) {
        this.context = context;
        this.mainSignView = mainSignView;
        apiRequest = ApiRequest.getInstance(context);
    }


    public CallbackManager setUpFacebook(LoginButton loginButton) {
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("Data","LoginResult");
                callGraphApi(loginResult);

            }

            @Override
            public void onCancel() {
                Log.e("Data","onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("Data","FacebookException");

            }
        });
        return callbackManager;
    }

    public void signupWithFacebook(AccessToken accessToken) {
        Log.e("Data","signupWithFacebook");
        HashMap<String, String> params = new HashMap<>();
        params.put("facebook_id", accessToken.getToken());
        params.put("api_token", Constants.api_token);
        apiRequest.createPostRequest(Constants.FacebookLogin, SocialLoginModel.class, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<SocialLoginModel>() {
            @Override
            public void onSuccess(SocialLoginModel response) throws JSONException {
                mainSignView.publishSocial(response);
            }

            @Override
            public void onFail(ANError error) throws JSONException {
                LogMe(error.toString());
            }
        });
    }
    public void signupWithGoogle(String accessToken) {
        Log.e("Data","signupWithGoogle"+accessToken);
        HashMap<String, String> params = new HashMap<>();
        params.put("google_token", accessToken);
        params.put("api_token", Constants.api_token);
        apiRequest.createPostRequest(Constants.GoogleLogin, SocialLoginModel.class, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<SocialLoginModel>() {
            @Override
            public void onSuccess(SocialLoginModel response) throws JSONException {
                Log.e("Data","Inside");
                mainSignView.publishSocial(response);
            }

            @Override
            public void onFail(ANError error) throws JSONException {
                LogMe(error.toString());
                Log.e("Datax",error.toString());
            }
        });
    }
    public void CheckPhone(String phone) {
        Log.e("Data","NOW");
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("api_token", Constants.api_token);
        apiRequest.createPostRequest(Constants.CheckPhone, SocialLoginModel.class, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<SocialLoginModel>() {
            @Override
            public void onSuccess(SocialLoginModel response) throws JSONException {
                Log.e("Data","Inside");
                mainSignView.PhoneRetrieved(response,response.getUserToken());
            }

            @Override
            public void onFail(ANError error) throws JSONException {
                LogMe(error.toString());
            }
        });
    }
    public void signupWithTwitter(String accessToken) {
        HashMap<String, String> params = new HashMap<>();
        params.put("twitter_token", accessToken);
        params.put("api_token", Constants.api_token);
        apiRequest.createPostRequest(Constants.TwitterLogin, SocialLoginModel.class, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<SocialLoginModel>() {
            @Override
            public void onSuccess(SocialLoginModel response) throws JSONException {
                mainSignView.publishSocial(response);
            }

            @Override
            public void onFail(ANError error) throws JSONException {
                LogMe(error.toString());
            }
        });
    }
    public void signupWithInstgram(String accessToken) {
        HashMap<String, String> params = new HashMap<>();
        params.put("instgram_token", accessToken);
        params.put("api_token", Constants.api_token);
        apiRequest.createPostRequest(Constants.InstgramLogin, SocialLoginModel.class, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<SocialLoginModel>() {
            @Override
            public void onSuccess(SocialLoginModel response) throws JSONException {
                mainSignView.publishSocial(response);
            }

            @Override
            public void onFail(ANError error) throws JSONException {
                LogMe(error.toString());
            }
        });
    }


    private void callGraphApi(LoginResult loginResult){
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                (json_object, response) -> {
                    Log.e("DataFacebook" , response.toString());
                    mainSignView.publishFacebook(json_object,loginResult.getAccessToken());
                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "birthday,name,email,picture,gender");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
//        signupWithFacebook(loginResult.getAccessToken());

    }



    public void CreateNewUserSocial(String name,String facebook_id,String image,String birthdate,String gender) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("facebook_token", facebook_id);
        params.put("birthdate", birthdate);
        params.put("gender", gender);
        params.put("image", image);
        params.put("api_token", Constants.api_token);

        apiRequest.createPostRequest(Constants.FaceBookRegister, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                LogMe(response);
                JSONObject result = new JSONObject(response);
                if (result.getInt("code") == 0 ){
                    //Errorrrrrrrr
                    Log.e("facebook_id",facebook_id+"xx");
                    mainSignView.RegisterSocial(false,"");
                }
                else {
                    mainSignView.RegisterSocial(true,result.getString("user_token"));
                }
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }
    public void CreateNewUserSocialGoogle(String name,String facebook_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("google_token", facebook_id);
        params.put("api_token", Constants.api_token);

        apiRequest.createPostRequest(Constants.RegisterGoogle, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                LogMe(response);
                JSONObject result = new JSONObject(response);
                if (result.getInt("code") == 0 ){
                    //Errorrrrrrrr
                    Log.e("facebook_id",facebook_id+"xx");
                    mainSignView.RegisterSocial(false,"");
                }
                else {
                    mainSignView.RegisterSocial(true,result.getString("user_token"));
                }
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }
    public void CreateNewUserSocialTwitter(String name,String facebook_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("twitter_token", facebook_id);
        params.put("api_token", Constants.api_token);

        apiRequest.createPostRequest(Constants.RegisterTwitter, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                LogMe(response);
                JSONObject result = new JSONObject(response);
                if (result.getInt("code") == 0 ){
                    //Errorrrrrrrr
                    Log.e("facebook_id",facebook_id+"xx");
                    mainSignView.RegisterSocial(false,"");
                }
                else {
                    mainSignView.RegisterSocial(true,result.getString("user_token"));
                }
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }
    public void GetUserDataFromUserToken(String user_token) {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_token", user_token);
        params.put("api_token", Constants.api_token);
        apiRequest.createPostRequest(Constants.UserProfileFromToken, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                Log.e("Done","response" + response);
                LogMe(response);
                JSONObject result = new JSONObject(response);
                UserModelObject modelObject = new UserModelObject(result.getJSONObject("content"));
                mainSignView.DataRetrieved(modelObject);
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }

}
