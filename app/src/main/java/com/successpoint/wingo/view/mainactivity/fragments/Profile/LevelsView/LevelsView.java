package com.successpoint.wingo.view.mainactivity.fragments.Profile.LevelsView;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.successpoint.wingo.App;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.TopFansModel;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.Mainsharedprefs;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.ProfilePresenter;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.ProfileView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

import static com.successpoint.wingo.utils.CommonMethods.LogMe;

public class LevelsView extends AppCompatActivity {
    TextView Level,currentExp,expNext;
    ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylevel);
        progressBar = findViewById(R.id.progress_bar);
        currentExp = findViewById(R.id.currentExp);
        Level = findViewById(R.id.level);
        expNext = findViewById(R.id.expNext);
        GetValues();
    }


    public void GetValues() {
        ApiRequest apiRequest = ApiRequest.getInstance(this);

        HashMap<String, String> params = new HashMap<>();
        params.put("user_token", Mainsharedprefs.getToken());
        params.put("api_token", Constants.api_token);
        apiRequest.createPostRequest(Constants.LevelData, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                LogMe(response);
                JSONObject result = new JSONObject(response);
                if (result.getInt("code")==0){
                    Toast.makeText(LevelsView.this,"Failed", Toast.LENGTH_LONG).show();
                }
                else {
                    progressBar.setProgress(result.getInt("percentage"));
                    currentExp.setText(result.getInt("current_exp")+"");
                    Level.setText(result.getInt("level")+"");
                    expNext.setText(result.getInt("next_level")+"");
                }

            }
            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }
}