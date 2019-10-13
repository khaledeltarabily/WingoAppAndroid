package com.successpoint.wingo.view.mainsign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import retrofit2.Call;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.successpoint.wingo.App;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.SocialLoginModel;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.utils.Mainsharedprefs;
import com.successpoint.wingo.view.createpassword.CreatePasswordActivity;
import com.successpoint.wingo.view.toptrending.TopTrendingActivity;
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
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Arrays;

public class MainSignActivity extends MvpActivity<MainSignView, MainSignPresenter> implements MainSignView {
    public String user_token="";
    public static int APP_REQUEST_CODE = 99;
    TwitterLoginButton mLoginButton;
    CallbackManager callbackManager;
    String phoneNumberString;
    public void phoneLogin() {
        final Intent intent = new Intent(this, AccountKitActivity.class);
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
    private Button btNext;
    private LoginButton loginButton;
    private ImageView ivFb, ivGo, ivIn, ivTw;
    private static final String CALLBACK_URL = "twittersdk://callback";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("WBTHP2sJgVDg1xm8Dfe2YgpaK", "AvRhrbMnb68jsJAIAZkSH8dLBzrQLgl2e0sfPAp5HN8jwVRtNa"))
                .debug(true)

                .build();
        Twitter.initialize(config);
        setContentView(R.layout.activity_mainsign);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        btNext = findViewById(R.id.btNext);
        loginButton = findViewById(R.id.login_button);
        ivFb = findViewById(R.id.ivFb);
        ivGo = findViewById(R.id.ivGo);
        ivIn = findViewById(R.id.ivIn);
        ivTw = findViewById(R.id.ivTw);

        callbackManager = presenter.setUpFacebook(loginButton);


        loginButton.setReadPermissions(Arrays.asList( "user_birthday" , "user_gender","user_birthday","email", "public_profile"));
        ivFb.setOnClickListener(view -> loginButton.performClick());

        btNext.setOnClickListener(view -> {
            phoneLogin();
        });
//964994869093-qj3iepm48ub5mpno67bgio7vbk34v0h4.apps.googleusercontent.com
        //                        .requestIdToken("964994869093-l7ckr3vfbi1l6r8mjjr85h5qpm34l0el.apps.googleusercontent.com")
        ivGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("964994869093-dds2l2c7a123iftpjg8nk6lvtp39oh8b.apps.googleusercontent.com")
                        .requestEmail()
                        .requestProfile()
                        .requestScopes(new Scope(Scopes.PLUS_ME))
                        .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                        .build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(MainSignActivity.this, gso);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, GOOGLE_CODE);
            }
        });


         mLoginButton =  findViewById(R.id.login_button_tw);
        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.e("Twitter","TwitterSession");
                TwitterSession twitterSession = result.data;
                getTwitterData(twitterSession);
            }
            @Override
            public void failure(TwitterException exception) {
                Log.e("Twitter", "Failed to authenticate user " + exception.getMessage());

            }
        });


        ivTw.setOnClickListener(v -> mLoginButton.performClick());

//        AuthenticationDialog authenticationDialog = new AuthenticationDialog(this, this);
//        authenticationDialog.setCancelable(true);
//        authenticationDialog.show();

        ivIn.setOnClickListener(v -> Toast.makeText(MainSignActivity.this,"Stopped",Toast.LENGTH_LONG).show());


    }

    @NonNull
    @Override
    public MainSignPresenter createPresenter() {
        return new MainSignPresenter(this, this);
    }


    @Override
    public void publishFacebook(JSONObject loginResult,AccessToken accestoken) {
        try {
            getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("type","facebook").apply();
            getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("name",loginResult.get("name").toString()).apply();
//            getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("gender",loginResult.get("gender").toString()).apply();
//            getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("birthdate",loginResult.get("birthdate").toString()).apply();
            getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("id",loginResult.get("id").toString()).apply();
            getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("image_url","http://graph.facebook.com/"+loginResult.get("id").toString()+"/picture?type=large&width=240&height=240").apply();
            Log.e("Data","HereeFace");
            presenter.signupWithFacebook(accestoken);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Data","HereeFace" + e.toString());
        }
    }

    @Override
    public void publishSocial(SocialLoginModel response) {
        Log.e("Data","publishSocial");
        if (response.getCode() == 1) {
            Log.e("Data","publishSocialgetCode");
            if (response.getUserToken()!=null) {
                Mainsharedprefs.saveToken(response.getUserToken());
                user_token = response.getUserToken();
                presenter.GetUserDataFromUserToken(response.getUserToken());
            }
            else {
                Log.e("Data","publishSocialgetCodeNoooxxx");
                SharedPreferences sharedPreferences = getSharedPreferences("wingo",MODE_PRIVATE);
                presenter.CreateNewUserSocial(sharedPreferences.getString("name",""),sharedPreferences.getString("id",""),sharedPreferences.getString("image_url",""),
                    sharedPreferences.getString("birthdate",""),sharedPreferences.getString("gender",""));
            }
        }
        else {
            SharedPreferences sharedPreferences = getSharedPreferences("wingo",MODE_PRIVATE);
            Log.e("Data","publishSocialgetCodeNooo"+sharedPreferences.getString("type","x"));
            if (sharedPreferences.getString("type","").equals("facebook")){
                presenter.CreateNewUserSocial(sharedPreferences.getString("name",""),sharedPreferences.getString("id",""),sharedPreferences.getString("image_url",""),
                    sharedPreferences.getString("birthdate",""),sharedPreferences.getString("gender",""));
            }
            else if (sharedPreferences.getString("type","").equals("google")){
                presenter.CreateNewUserSocialGoogle(sharedPreferences.getString("name",""),sharedPreferences.getString("token",""));
            }
            else {
                presenter.CreateNewUserSocialTwitter(sharedPreferences.getString("name",""),sharedPreferences.getString("token",""));
            }

        }
    }

    @Override
    public void RegisterSocial(boolean DoneOrNot,String token) {
        user_token = token;
        if (DoneOrNot){
            presenter.GetUserDataFromUserToken(user_token);
        }
        else {
            Log.e("Data","RegisterSocial Error");
            Toast.makeText(this,"Error Occur",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode==TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE)
        {
            Log.e("Done","MOBILEwww");
            // Pass the activity result to the login button.
            mLoginButton.onActivityResult(requestCode, resultCode, data);
        }
        Log.e("Done","MOBILE");
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
                            Log.e("DoneV","herer22");
                            PhoneNumber phoneNumber = account.getPhoneNumber();
                             phoneNumberString = phoneNumber.toString();
                             presenter.CheckPhone(phoneNumberString);
                        }

                        @Override
                        public void onError(final AccountKitError error) {
                            Log.e("DoneHH",error.toString());
                        }
                    });



                }
                else
                {
                    Log.e("Done","herer22");
                }
            }
        }
        Log.e("Google","ReturnedGoogleNot");
        if (requestCode == GOOGLE_CODE) {
            Log.e("Google","ReturnedGo");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.e("Data",account.getDisplayName());
                Log.e("Data",account.toString());
                getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("type","google").apply();
                getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("token",account.getIdToken()).apply();
                getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("name",account.getDisplayName()).apply();
                getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("image_url",account.getPhotoUrl().getPath()).apply();
                Log.e("Google","ReturnedGoogle");
                presenter.signupWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.e("Google",e.toString());
            }
        }
    }
    private void getTwitterData(final TwitterSession twitterSession) {
        Log.e("Twitter", "getTwitterData");
        TwitterApiClient twitterApiClient = new TwitterApiClient(twitterSession);
        final Call<User> getUserCall = twitterApiClient.getAccountService().verifyCredentials(true, false, true);
        getUserCall.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                Log.e("Twitter", "getTwitterData Inside");
                getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("type","twitter").apply();
                getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("token", twitterSession.getAuthToken().token).apply();
                getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("id", String.valueOf(result.data.getId())).apply();
                getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("name",result.data.name).apply();
                getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("image_url",result.data.profileImageUrl).apply();
                Log.e("Twitter", twitterSession.getAuthToken().token);
                presenter.signupWithTwitter(twitterSession.getAuthToken().token);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.e("Twitter", "Failed to get user data " + exception.getMessage());
            }
        });
    }

    String instgram_token = "";

    private class RequestInstagramAPI extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("" + instgram_token);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    if (jsonData.has("id")) {
                        getSharedPreferences("wingo", MODE_PRIVATE).edit().putString("type", "instgram").apply();
                        getSharedPreferences("wingo", MODE_PRIVATE).edit().putString("token", instgram_token).apply();
                        getSharedPreferences("wingo", MODE_PRIVATE).edit().putString("id", jsonData.getString("id")).apply();
                        getSharedPreferences("wingo", MODE_PRIVATE).edit().putString("name", jsonData.getString("username")).apply();
                        getSharedPreferences("wingo", MODE_PRIVATE).edit().putString("image_url", jsonData.getString("profile_picture")).apply();
                        presenter.signupWithInstgram(instgram_token);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Login error!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
    public String UserModelObjectToString(UserModelObject user){
            String result = "{";
            result += " \"name\": \"" + user.getName()+"\"";
            result += " , \"level\":" + user.getLevel();
            result += " , \"user_id\":\"" + user.getUser_id()+"\"";
            result += " , \"exp\":" + user.getExp();
            result += " , \"vip\":" + user.getVip();
            result += " , \"send\":" + user.getSend();
//            result += " , \"facebook_token\":\"" + user.getFacebook_token()+"\"";
//            result += " , \"twitter_token\"\":" + user.getTwitter_token()+"\"";
//            result += " , \"google_token\":" + user.getGoogle_token()+"\"";
            result += " , \"birth_date\":\"" + user.getBirth_date()+"\"";
            result += " , \"age\":\"" + user.getAge()+"\"";
            result += " , \"bio\": \"" + user.getBio()+"\"";

        result += " , \"verfied\":" + user.isVerfied()+"\"";
        result += " , \"feathers\":" + user.getFeathers();
        result += " , \"diamonds\": " + user.getDiamonds();
        result += " , \"wings\":" + user.getWings();
        result += " , \"friends\": " + user.getFriends();
        result += " , \"fans\":" + user.getFans();
        result += " , \"following\":" + user.getFollowing();
        result += " , \"country\": \"" + user.getCountry()+"\"";
        result += " , \"position\":" + user.getPosition();
        result += " , \"total_recieve\":" + user.getTotal_recieve();
        result += " , \"picture\": \"" + user.getPicture()+"\"";
        result += " , \"total_send\": " + user.getTotal_send();
        result += " , \"image_count\": " + user.getImage_count();
        result += " , \"register_date\": \"" + user.getRegister_date()+"\"";
        result += " , \"elite_id\": \"" + user.getElite_id()+"\"";
        result += " , \"gender\": \"" + user.getGender()+"\"";
        result += " , \"turn_off_live_notifications\": " + user.getTurn_off_live_notifications();
        result += " , \"turn_off_message_notifications\": " + user.getTurn_off_message_notifications();
        result += " , \"hide_last_seen\": " + user.getHide_last_seen();
        result += " , \"hide_location\": " + user.getHide_location();
        result += " , \"hide_location\": " + user.getHide_location();
        result += ", \"images\": [";
        for (int i =0 ; i<user.getImages().size();i++){
            if (i!=0)
                result += " , ";
            result += "\"" + user.getImages().get(i)+"\"";
        }
        result += "]";

            result +=" }";


            return result;
    }

    @Override
    public void DataRetrieved(UserModelObject user_object) {
        Log.e("Data","DataRetrieved");
        user_object.setUser_token(user_token);
        Mainsharedprefs.saveToken(user_token);
        App.userModelObject_of_Project = user_object;
        getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("user_data",UserModelObjectToString(user_object)).apply();
        Log.e("uuuuuser",UserModelObjectToString(user_object));
        startActivity(new Intent(this, TopTrendingActivity.class));
        finish();
    }

    @Override
    public void PhoneRetrieved(SocialLoginModel response,String user_tokenx) {
        Log.e("Done","PhoneRetrieved");
        if (response.getCode() == 0) {
            Intent intent = new Intent(getApplicationContext(), CreatePasswordActivity.class);
            intent.putExtra("phone", phoneNumberString);
            startActivity(intent);
        }
        else {
            this.user_token = user_tokenx;
            presenter.GetUserDataFromUserToken(response.getUserToken());
        }
    }
}