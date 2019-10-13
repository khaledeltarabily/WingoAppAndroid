package com.successpoint.wingo.view.mainactivity.fragments.Profile.ActivityProfile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.successpoint.wingo.App;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.MainChats;
import com.successpoint.wingo.model.TopFansModel;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.Mainsharedprefs;
import com.successpoint.wingo.view.ChatFragment.MessagesActivity;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.AllActivitiesUserList.ShowUserActivities;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.Circles;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.EditUserData.EditProfileActivity;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.ImagesRecycler;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.ProfilePresenter;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.ProfileView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileActivity extends MvpActivity<ProfileActivityView, ProfileActivityPresenter> implements ProfileActivityView {
    RecyclerView user_images, points;
    TextView chat_container,follow_him;
    ImageView edit, back;
    Circles CirclesAd;
    ImagesRecycler imagesRecyclerAdapter;
    TextView user_name, wingo_id, bio, location, level, foshia_icon, fansCount, followingsCount, wingCount, sendCount;
    UserModelObject object;
    LinearLayoutManager layoutManager2;
    ImageView first_fan,second_fan,third_fan;
    List<TopFansModel.Datum> list_fans;
    LinearLayout sends_container,fans_container,following_container,wings_container;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        list_fans = new ArrayList<>();
        follow_him = findViewById(R.id.follow);
        chat_container = findViewById(R.id.chat_container);
        chat_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Clicked","Done");
                presenter.getAllChatsFirst(object.getUser_id(),object);
            }
        });
        fans_container = findViewById(R.id.fans_container);
        sends_container = findViewById(R.id.send_container);
        wings_container = findViewById(R.id.wings_container);
        following_container = findViewById(R.id.followings_container);

        first_fan = findViewById(R.id.first_image);
        second_fan = findViewById(R.id.second_image);
        third_fan = findViewById(R.id.third_image);

        user_images = findViewById(R.id.images);
        edit = findViewById(R.id.edit);
        back = findViewById(R.id.back);
        user_name = findViewById(R.id.user_name);
        wingo_id = findViewById(R.id.wingo_id);
//        foshia_icon = findViewById(R.id.foshia_icon);
        level = findViewById(R.id.level);
        fansCount = findViewById(R.id.fansCount);
        location = findViewById(R.id.address);
        bio = findViewById(R.id.bio);
        followingsCount = findViewById(R.id.followingsCount);
        wingCount = findViewById(R.id.wingCount);
        sendCount = findViewById(R.id.sendCount);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });
        fans_container.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ShowUserActivities.class);
            intent.putExtra("type","fans");
            if (!object.getUser_id().equals(App.userModelObject_of_Project.getUser_id()))
                intent.putExtra("user_id",object.getUser_id());
            startActivity(intent);
        });
        following_container.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ShowUserActivities.class);
            intent.putExtra("type","following");
            if (!object.getUser_id().equals(App.userModelObject_of_Project.getUser_id()))
                intent.putExtra("user_id",object.getUser_id());

            startActivity(intent);
        });
        sends_container.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ShowUserActivities.class);
            intent.putExtra("type","sends");
            if (!object.getUser_id().equals(App.userModelObject_of_Project.getUser_id()))
                intent.putExtra("user_id",object.getUser_id());

            startActivity(intent);
        });
        wings_container.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ShowUserActivities.class);
            intent.putExtra("type","wings");
            if (!object.getUser_id().equals(App.userModelObject_of_Project.getUser_id()))
                intent.putExtra("user_id",object.getUser_id());
            startActivity(intent);
        });
        user_images.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = layoutManager2.findFirstVisibleItemPosition();
                CirclesAd = new Circles(ProfileActivity.this, firstVisibleItem, object.getImage_count());
                points.setAdapter(CirclesAd);

            }
        });

        points = (RecyclerView) findViewById(R.id.points);
        points.setNestedScrollingEnabled(true);
        points.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        points.setLayoutManager(linearLayoutManager);

        Log.e("Done","HereNowId"+getIntent().getExtras().getString("ID"));
        Log.e("Done","HereNowId"+App.userModelObject_of_Project.getUser_id());
        if (getIntent().getExtras().getString("ID").equals(App.userModelObject_of_Project.getUser_id())) {
            object = App.userModelObject_of_Project;
            presenter.getContributorsByUser_id("");
            Log.e("Done","HereNow");
        }
        else {
            Log.e("Done","Here");
            object = (UserModelObject) getIntent().getExtras().getSerializable("model");
            presenter.getContributorsByUser_id(object.getUser_id());
        }
        follow_him.setVisibility(object.isIf_is_him_follow()?View.GONE:View.VISIBLE);
        follow_him.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiRequest apiRequest = ApiRequest.getInstance(getApplicationContext());

                HashMap<String,String> params = new HashMap<>();
                params.put("api_token", Constants.api_token);
                params.put("user_token", Mainsharedprefs.getToken());
                params.put("user_ids",object.getUser_id());
                apiRequest.createPostRequest(Constants.Follow, params, Priority.MEDIUM, new ApiRequest.ServiceCallback() {
                    @Override
                    public void onSuccess(Object response) throws JSONException {
                        follow_him.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFail(ANError error) throws JSONException {

                    }
                });
            }
        });
        SetupData();
    }

    @Override
    public ProfileActivityPresenter createPresenter() {
        return new ProfileActivityPresenter(this,this);
    }

    @Override
    public void DataRetrieved(UserModelObject user_object) {

        if (user_object!=null){
            object = user_object;
            SetupData();
        }
    }

    @Override
    public void TopContributesRetrieved(TopFansModel response) {
        list_fans = response.getData();
        Glide.with(getApplicationContext()).load(list_fans.get(0).getPicture()).into(first_fan);
        Glide.with(getApplicationContext()).load(list_fans.get(1).getPicture()).into(second_fan);
        Glide.with(getApplicationContext()).load(list_fans.get(2).getPicture()).into(third_fan);
    }

    @Override
    public void retrievedchatData(MainChats data) {
        Intent intent = new Intent(getApplicationContext(),MessagesActivity.class);
        Log.e("chat_id",data.getChat_id()+":chat_id");
        intent.putExtra("chat_id",data.getChat_id());
        Bundle bundle = new Bundle();
        bundle.putSerializable("model",data);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void SetupData(){

        user_name.setText(object.getName());
        wingo_id.setText(object.getElite_id());
        bio.setText(object.getBio());
//        foshia_icon.setText(object.getDiamonds() + "");
        fansCount.setText(object.getFans() + "");
        location.setText(object.getHide_location() + "");
        level.setText(object.getLevel() + "");
        followingsCount.setText(object.getFollowing() + "");
        wingCount.setText(object.getWings() + "");
        sendCount.setText(object.getSend() + "");
        user_images = (RecyclerView) findViewById(R.id.images);
        user_images.setNestedScrollingEnabled(true);
        user_images.setHasFixedSize(true);
         layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        user_images.setLayoutManager(layoutManager2);
        imagesRecyclerAdapter = new ImagesRecycler(this, object.getImages());
        user_images.setAdapter(imagesRecyclerAdapter);

        CirclesAd = new Circles(this, 0, object.getImage_count());
        points.setAdapter(CirclesAd);

    }
}