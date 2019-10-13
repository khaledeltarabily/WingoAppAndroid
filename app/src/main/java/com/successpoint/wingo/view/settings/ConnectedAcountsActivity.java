package com.successpoint.wingo.view.settings;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.successpoint.wingo.App;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.SocialLoginModel;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.AllActivitiesUserList.ShowUserActivities;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import retrofit2.Call;

public class ConnectedAcountsActivity extends AppCompatActivity {
    private View view;
    ImageView google, facebook, twitter, phone;
    public static int APP_REQUEST_CODE = 99;
    CallbackManager callbackManager;
    String phoneNumberString;

    public void phoneLogin() {
        final Intent intent = new Intent(ConnectedAcountsActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    private int GOOGLE_CODE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_chat_inside);


//            google = view.findViewById(R.id.google);
//            twitter = view.findViewById(R.id.twitter);
            facebook = view.findViewById(R.id.face);
            phone = view.findViewById(R.id.phone);
            LoginButton loginButton = view.findViewById(R.id.login_button);

            callbackManager = setUpFacebook(loginButton);


            loginButton.setReadPermissions(Arrays.asList("user_birthday", "user_gender", "user_birthday", "email", "public_profile"));
            facebook.setOnClickListener(view -> loginButton.performClick());

            phone.setOnClickListener(view -> {
                phoneLogin();
            });

            google.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .requestProfile()
                            .requestScopes(new Scope(Scopes.PLUS_ME))
                            .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                            .build();
                    GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(ConnectedAcountsActivity.this, gso);
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, GOOGLE_CODE);
                }
            });

            TwitterConfig config = new TwitterConfig.Builder(ConnectedAcountsActivity.this)
                    .logger(new DefaultLogger(Log.DEBUG))
                    .twitterAuthConfig(new TwitterAuthConfig("WBTHP2sJgVDg1xm8Dfe2YgpaK", "AvRhrbMnb68jsJAIAZkSH8dLBzrQLgl2e0sfPAp5HN8jwVRtNa"))
                    .debug(true)
                    .build();
            Twitter.initialize(config);
            TwitterLoginButton mLoginButton = view.findViewById(R.id.login_button_tw);
            mLoginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    Log.e("Twitter", "TwitterSession");
                    TwitterSession twitterSession = result.data;
                    getTwitterData(twitterSession);
                }

                @Override
                public void failure(TwitterException exception) {
                    Log.e("Twitter", "Failed to authenticate user " + exception.getMessage());

                }
            });


            twitter.setOnClickListener(v -> mLoginButton.performClick());


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.e("Done", "MOBILE");
        if (requestCode == APP_REQUEST_CODE) {
            // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null) {
            } else if (loginResult.wasCancelled()) {
            } else {
                if (loginResult.getAccessToken() != null) {
                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {
                            Log.e("Done", "herer22");
                            PhoneNumber phoneNumber = account.getPhoneNumber();
                            phoneNumberString = phoneNumber.toString();
                            AuthorizedPhone(phoneNumberString,"phone");
                        }

                        @Override
                        public void onError(final AccountKitError error) {
                            Log.e("Done", error.toString());
                        }
                    });


                } else {
                    Log.e("Done", "herer22");
                }
            }
        }
        Log.e("Google", "ReturnedGoogleNot");
        if (requestCode == GOOGLE_CODE) {
            Log.e("Google", "ReturnedGo");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthorizedSocial(account.getIdToken(),"google");
                } catch (ApiException e) {
                Log.e("Google", e.toString());
            }
        }
    }

    private void getTwitterData(final TwitterSession twitterSession) {
        TwitterApiClient twitterApiClient = new TwitterApiClient(twitterSession);
        final Call<User> getUserCall = twitterApiClient.getAccountService().verifyCredentials(true, false, true);
        getUserCall.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                AuthorizedSocial(twitterSession.getAuthToken().token,"twitter");
            }

            @Override
            public void failure(TwitterException exception) {
                Log.e("Twitter", "Failed to get user data " + exception.getMessage());
            }
        });
    }

    public void AuthorizedSocial(String token, String type_to_send) {
        ApiRequest apiRequest = ApiRequest.getInstance(ConnectedAcountsActivity.this);
        HashMap<String, String> params = new HashMap<>();
        params.put(type_to_send + "_token", token);
        params.put("api_token", Constants.api_token);
        apiRequest.createPostRequest(Constants.AuthorizedSocial(type_to_send), SocialLoginModel.class, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<SocialLoginModel>() {
            @Override
            public void onSuccess(SocialLoginModel response) throws JSONException {
                Toast.makeText(ConnectedAcountsActivity.this,"Done",Toast.LENGTH_LONG);
            }

            @Override
            public void onFail(ANError error) throws JSONException {
            }
        });
    }
    public void AuthorizedPhone(String phone, String type_to_send) {
        ApiRequest apiRequest = ApiRequest.getInstance(ConnectedAcountsActivity.this);
        HashMap<String, String> params = new HashMap<>();
        params.put(type_to_send , phone);
        params.put("api_token", Constants.api_token);
        apiRequest.createPostRequest(Constants.AuthorizedSocial(type_to_send), SocialLoginModel.class, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<SocialLoginModel>() {
            @Override
            public void onSuccess(SocialLoginModel response) throws JSONException {
                Toast.makeText(ConnectedAcountsActivity.this,"Done",Toast.LENGTH_LONG);
            }

            @Override
            public void onFail(ANError error) throws JSONException {
            }
        });
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
    private void callGraphApi(LoginResult loginResult){
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                (json_object, response) -> {
                    Log.e("DataFacebook" , response.toString());
                    AuthorizedSocial(loginResult.getAccessToken().getToken(),"facebook");

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "birthday,name,email,picture,gender");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
//        signupWithFacebook(loginResult.getAccessToken());

    }
}
