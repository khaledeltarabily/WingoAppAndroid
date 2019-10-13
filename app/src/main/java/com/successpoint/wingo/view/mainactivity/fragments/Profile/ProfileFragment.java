package com.successpoint.wingo.view.mainactivity.fragments.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.login.widget.LoginButton;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.hbb20.CountryCodePicker;
import com.successpoint.wingo.App;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.view.VIP_page.VIPActivity;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.ActivityProfile.ProfileActivity;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.AllActivitiesUserList.ShowUserActivities;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.EditUserData.EditProfileActivity;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.LevelsView.LevelsView;
import com.successpoint.wingo.view.search.SearchActivity;
import com.successpoint.wingo.view.settings.SettingsActivity;
import com.successpoint.wingo.view.toptrending.TopTrendingActivity;
import com.successpoint.wingo.view.wallet.WalletActivity;

import androidx.annotation.NonNull;

public class ProfileFragment extends MvpFragment<ProfileView, ProfilePresenter> implements ProfileView {
    private View view;
    
    private ImageView user_image;
    private ImageView search,friend;
    private TextView user_name,user_id,wingo_id,gawhra,foshia_icon,fansCount,followingCount,friendsCount;
    private ImageView Wallet,VIP,Store,Level,Baggage,Task,Settings,Feedback;
    private ImageView Barcode;
    private UserModelObject object;
    private ImageView facebook,twitter,instgram,google;
    LinearLayout friends_container,fans_container,following_container;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private CountryCodePicker spCC;
    private EditText etPhone;
    private Button btNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {

            view = inflater.inflate(R.layout.fragment_profile, container, false);
            presenter = new ProfilePresenter(getContext(),this);
            fans_container = view.findViewById(R.id.fans_container);
            friends_container = view.findViewById(R.id.friends_container);
            following_container = view.findViewById(R.id.following_container);
            user_image = view.findViewById(R.id.user_image);

            friend = view.findViewById(R.id.friend);
            user_name = view.findViewById(R.id.user_name);
            user_id = view.findViewById(R.id.user_id);
            wingo_id = view.findViewById(R.id.wingo_id);
//            foshia_icon = view.findViewById(R.id.foshia_icon);
            gawhra = view.findViewById(R.id.level);
            fansCount = view.findViewById(R.id.fansCount);
            followingCount = view.findViewById(R.id.followingCount);
            friendsCount = view.findViewById(R.id.friendsCount);
            Wallet = view.findViewById(R.id.Wallet);
            friendsCount = view.findViewById(R.id.friendsCount);
            VIP = view.findViewById(R.id.VIP);
            Store = view.findViewById(R.id.Store);
            Level = view.findViewById(R.id.Level);
            Baggage = view.findViewById(R.id.Baggage);
            Task = view.findViewById(R.id.Task);
            Settings = view.findViewById(R.id.Settings);
            Feedback = view.findViewById(R.id.Feedback);
            search = view.findViewById(R.id.search);
//            Barcode = view.findViewById(R.id.Barcode);
            search.setOnClickListener(v -> {
                    startActivity(new Intent(getContext(), SearchActivity.class));
            });
//            search.setOnClickListener(v ->
//            {
//                Intent intent = new Intent(getContext(), ProfileActivity.class);
//                intent.putExtra("ID",object.getUser_id());
//                startActivity(intent);
//            });

            Settings.setOnClickListener(v -> startActivity(new Intent(getContext(), SettingsActivity.class)));
            Level.setOnClickListener(v -> startActivity(new Intent(getContext(), LevelsView.class)));
            Wallet.setOnClickListener(v -> startActivity(new Intent(getContext(), WalletActivity.class)));

            fans_container.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), ShowUserActivities.class);
                intent.putExtra("type","fans");
                startActivity(intent);
            });
            following_container.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), ShowUserActivities.class);
                intent.putExtra("type","following");
                startActivity(intent);
            });
            friends_container.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), ShowUserActivities.class);
                intent.putExtra("type","friends");
                startActivity(intent);
            });
            friend.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), Profile.class);
                intent.putExtra("ID",object.getUser_id());
                startActivity(intent);
            });
            object = App.userModelObject_of_Project;
            object  = App.userModelObject_of_Project ;
            presenter.GetUserDataFromUserToken(App.userModelObject_of_Project.getUser_token());
            SetViewsHere();
        }
        return view;
    }

    @NonNull
    @Override
    public ProfilePresenter createPresenter() {
        return new ProfilePresenter(getContext(),this);
    }


    @Override
    public void DataRetrieved(UserModelObject user_object, boolean isFail) {
        Log.e("Done","DataRetrieved");
        App.userModelObject_of_Project = user_object;
        object = App.userModelObject_of_Project;
        SetViewsHere();
    }

    @Override
    public void SocialConnected(boolean Status, String type) {

    }
    private void SetViewsHere(){
        Log.e("Done","SetViewsHere");
        if (object.getImages().size()>0)
            Glide.with(getContext()).load(object.getImages().get(0)).into(user_image);
        user_name.setText(object.getName());
        user_id.setText(object.getUser_id());
        wingo_id.setText(object.getElite_id());
        gawhra.setText(object.getDiamonds()+"");
//        foshia_icon.setText(object.getWings()+"");
        fansCount.setText(object.getFans()+"");
        followingCount.setText(object.getFollowing()+"");
        friendsCount.setText(object.getFriends()+"");
        if (object.getVip() == 0) {
            VIP.setAlpha(0.5f);
            Store.setAlpha(0.5f);
            Baggage.setAlpha(0.5f);
        }
        else {
            VIP.setAlpha(1f);
            Store.setAlpha(1f);
            Baggage.setAlpha(1f);
        }
        VIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), VIPActivity.class));
            }
        });
    }
}