package com.successpoint.wingo.view.mainactivity.fragments.Profile.EditUserData;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.successpoint.wingo.App;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.TopFansModel;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.utils.CommonMethods;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.Mainsharedprefs;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.Circles;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.ImagesRecycler;
import com.successpoint.wingo.view.toptrending.TopTrendingActivity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends MvpActivity<EditProfileView, EditProfilePresenter> implements EditProfileView {

    private Toolbar tbSign;
    private CircleImageView civProfile,civProfile2,civProfile3;
    private EditText etUsername;
    private RadioButton male;
    private RadioButton female;
    private TextView btDone;
    private TextView country,bio;
    private TextView wingo_id;
    private TextView birthdate;
    int index_photo_value_current = 0;
    private Uri photoUri1,photoUri2,photoUri3;
    private boolean publised = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        bio = findViewById(R.id.bio);
        country = findViewById(R.id.country);
        birthdate = findViewById(R.id.birthdate);
        wingo_id = findViewById(R.id.wingo);
        tbSign = findViewById(R.id.tbSign);
        civProfile = findViewById(R.id.civProfile);
        civProfile2 = findViewById(R.id.civProfile2);
        civProfile3 = findViewById(R.id.civProfile3);
        etUsername = findViewById(R.id.etUsername);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        btDone = findViewById(R.id.btDone);

        civProfile.setOnClickListener(view -> {
            index_photo_value_current = 1;
            presenter.selectPhoto();
        });
        civProfile2.setOnClickListener(view -> {
            index_photo_value_current = 2;
            presenter.selectPhoto();
        });
        civProfile3.setOnClickListener(view -> {
            index_photo_value_current = 3;
            presenter.selectPhoto();
        });


        btDone.setOnClickListener(view -> {
            HashMap<String, String> params = new HashMap<>();
            params.put("name", etUsername.getText().toString());
            params.put("api_token", Constants.api_token);
            params.put("user_token", Mainsharedprefs.getToken());
            params.put("country", "eg");
            params.put("bio", bio.getText().toString());
            params.put("gender", male.isChecked() ? "male" : "female");
            params.put("birthdate", birthdate.getText().toString());

            presenter.completeProfile(params);

            if (photoUri1 != null) {
                HashMap<String, File> image = new HashMap<>();
                image.put("image", new File(CommonMethods.getActualPath(photoUri1)));
                presenter.uploadImage(image,1);
            }
            if (photoUri2 != null) {
                HashMap<String, File> image = new HashMap<>();
                image.put("image", new File(CommonMethods.getActualPath(photoUri2)));
                presenter.uploadImage(image,2);
            }
            if (photoUri3 != null) {
                HashMap<String, File> image = new HashMap<>();
                image.put("image", new File(CommonMethods.getActualPath(photoUri3)));
                presenter.uploadImage(image,3);
            }
        });
        etUsername.setText(App.userModelObject_of_Project.getName());
        wingo_id.setText(App.userModelObject_of_Project.getElite_id());
        if (App.userModelObject_of_Project.getGender() !=null || App.userModelObject_of_Project.getGender().equals("male"))
            male.setChecked(true);
        else
            female.setChecked(true);
        birthdate.setText(App.userModelObject_of_Project.getBirth_date());
        bio.setText(App.userModelObject_of_Project.getBio());


    }

    @Override
    public EditProfilePresenter createPresenter() {
        return new EditProfilePresenter(this,this);
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
                if (index_photo_value_current==1) {
                    photoUri1 = data.getData();
                    Glide.with(EditProfileActivity.this)
                            .load(photoUri1)
                            .into(civProfile);
                }
                else if (index_photo_value_current == 2 ){
                    photoUri2 = data.getData();
                    Glide.with(EditProfileActivity.this)
                            .load(photoUri2)
                            .into(civProfile2);
                }
                else {
                    photoUri3 = data.getData();
                    Glide.with(EditProfileActivity.this)
                            .load(photoUri3)
                            .into(civProfile3);
                }
            }
        }
    }


    @Override
    public void DataEditedCorrectly(boolean done) {
        if (!publised) {
            publised = true;
            finish();
        }
    }

    @Override
    public void ImageUploadedCorrectly(boolean done) {

    }
}