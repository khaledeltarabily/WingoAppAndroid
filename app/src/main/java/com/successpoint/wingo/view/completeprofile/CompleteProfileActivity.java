package com.successpoint.wingo.view.completeprofile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.successpoint.wingo.App;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.utils.CommonMethods;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.Mainsharedprefs;
import com.successpoint.wingo.view.toptrending.TopTrendingActivity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompleteProfileActivity extends MvpActivity<CompleteProfileView, CompleteProfilePresenter> implements CompleteProfileView {

    private Toolbar tbSign;
    private CircleImageView civProfile;
    private EditText etUsername;
    private RadioButton male;
    private RadioButton female;
    private EditText etHomeAddress;
    private EditText etDay;
    private EditText etMonth;
    private EditText etYear;
    private Button btDone;
    private Uri photoUri;
    private boolean publised = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completeprofile);

        tbSign = findViewById(R.id.tbSign);
        civProfile = findViewById(R.id.civProfile);
        etUsername = findViewById(R.id.etUsername);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        etHomeAddress = findViewById(R.id.etHomeAddress);
        etDay = findViewById(R.id.etDay);
        etMonth = findViewById(R.id.etMonth);
        etYear = findViewById(R.id.etYear);
        btDone = findViewById(R.id.btDone);

        civProfile.setOnClickListener(view -> {
            presenter.selectPhoto();
        });

        etDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0 && Integer.valueOf(charSequence.toString()) > 0 && Integer.valueOf(charSequence.toString()) <= 31 && charSequence.length() == 2) {
                    etMonth.requestFocus();
                } else if (Integer.valueOf(charSequence.toString()) > 31) {
                    etDay.setText(String.valueOf(31));
                    etMonth.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0 && Integer.valueOf(charSequence.toString()) > 0 && Integer.valueOf(charSequence.toString()) <= 12 && charSequence.length() == 2) {
                    etMonth.requestFocus();
                } else if (Integer.valueOf(charSequence.toString()) > 12) {
                    etMonth.setText(String.valueOf(12));
                    etMonth.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0 && Integer.valueOf(charSequence.toString()) > 1960 && Integer.valueOf(charSequence.toString()) <= new GregorianCalendar().get(Calendar.YEAR) && charSequence.length() == 4) {
                    etYear.requestFocus();
                } else if (Integer.valueOf(charSequence.toString()) > new GregorianCalendar().get(Calendar.YEAR)) {
                    etYear.setText(String.valueOf(new GregorianCalendar().get(Calendar.YEAR)));
                    etMonth.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btDone.setOnClickListener(view -> {
            Log.e("Done","CLICKED");
            HashMap<String, String> params = new HashMap<>();
            params.put("name", etUsername.getText().toString());
            params.put("phone", getIntent().getExtras().getString("phone"));
            params.put("password", getIntent().getExtras().getString("password"));
            params.put("api_token", Constants.api_token);
            params.put("country", "eg");
            params.put("birthdate", etYear.getText().toString() + "/" + etMonth.getText().toString() + "/" + etDay.getText().toString());

            if (photoUri != null) {
                HashMap<String, File> image = new HashMap<>();
                image.put("image", new File(CommonMethods.getActualPath(photoUri)));
                presenter.completeProfile(params,image);
            }



        });
    }

    @NonNull
    @Override
    public CompleteProfilePresenter createPresenter() {
        return new CompleteProfilePresenter(this, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == presenter.Image_Gallery_Request) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.selectPhoto();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == presenter.Image_Gallery_Request) {
            if (resultCode == RESULT_OK) {
                photoUri = data.getData();
                Glide.with(CompleteProfileActivity.this)
                        .load(photoUri)
                        .into(civProfile);
            }
        }
    }

    @Override
    public void publishUpdateDone(UserModelObject user_object) {
        user_object.setUser_token(Mainsharedprefs.getToken());
        App.userModelObject_of_Project = user_object;
        getSharedPreferences("wingo",MODE_PRIVATE).edit().putString("user_data",user_object.toString()).apply();
        startActivity(new Intent(this, TopTrendingActivity.class));
        finish();
    }
}