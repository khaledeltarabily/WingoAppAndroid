package com.successpoint.wingo.view.mainactivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.navigation.NavigationView;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.successpoint.wingo.R;
import com.successpoint.wingo.view.mainactivity.fragments.LeaderBoard.LeaderBoardActivity;
import com.successpoint.wingo.view.search.SearchActivity;

public class MainActivity extends MvpActivity<MainActivityView, MainPresenter> implements NavigationView.OnNavigationItemSelectedListener, MainActivityView {
    private DrawerLayout drawer;
    private FrameLayout content;
    private Toolbar tbMain;
    private ImageView ivTrophy;
    private LinearLayout lNames;
    private TextView tvPopular;
    private TextView tvNearby, ivExplore;
    private TextView tvPk;
    private ImageView dotPopular;
    private ImageView dotNearby;
    private ImageView dotPk;
    private ImageView ivSearch;
    private ImageView ivNotification;
    private Fragment navHostFragment;
    private RelativeLayout lTabContainer;
    private ImageView home;
    private ImageView explore;
    private ImageView chat;
    private ImageView profile;
    private ImageView fly;
    private ImageView ivLive;
    private NavigationView navView;
    float scaleFactor = 6f;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainactivity);

        drawer = findViewById(R.id.drawer_layout);
        content = findViewById(R.id.content);
        lTabContainer = findViewById(R.id.lTabContainer);
        tbMain = findViewById(R.id.tbMain);
        ivTrophy = findViewById(R.id.ivTrophy);
        ivTrophy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LeaderBoardActivity.class));
            }
        });
        lNames = findViewById(R.id.lNames);
        tvPopular = findViewById(R.id.tvPopular);
        tvNearby = findViewById(R.id.tvNearby);
        ivExplore = findViewById(R.id.ivExplore);
        tvPk = findViewById(R.id.tvPk);
        dotPopular = findViewById(R.id.dotPopular);
        dotNearby = findViewById(R.id.dotNearby);
        dotPk = findViewById(R.id.dotPk);
        ivSearch = findViewById(R.id.ivSearch);
        ivNotification = findViewById(R.id.ivNotification);
        home = findViewById(R.id.home);

        explore = findViewById(R.id.explore);
        chat = findViewById(R.id.chat);

        profile = findViewById(R.id.profile);
        fly = findViewById(R.id.fly);
        ivLive = findViewById(R.id.ivLive);
        navView = findViewById(R.id.nav_view);

        ivSearch.setOnClickListener(view -> startActivity(new Intent(this, SearchActivity.class)));
        drawer.setScrimColor(Color.TRANSPARENT);
        drawer.setDrawerElevation(0f);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(-slideX);
                content.setScaleX(1 - (slideOffset / scaleFactor));
                content.setScaleY(1 - (slideOffset / scaleFactor));

                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    ivNotification.setImageResource(R.drawable.notification);
                } else {
                    ivNotification.setImageResource(R.drawable.notification1);
                }
            }
        };
        drawer.addDrawerListener(actionBarDrawerToggle);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = NavHostFragment.findNavController(navHostFragment);
        }
        ivNotification.setOnClickListener(view -> {
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else {
                drawer.openDrawer(GravityCompat.END);
            }
        });

        tvPopular.setOnClickListener(view -> {
            tvPopular.setTextColor(getResources().getColor(R.color.loginEnd));
            tvNearby.setTextColor(getResources().getColor(R.color.appColorBlack));
            dotPopular.setImageResource(R.drawable.dot);
            dotNearby.setImageDrawable(null);

            navController.navigate(R.id.popularFragment);
        });

        tvNearby.setOnClickListener(view -> {
            tvNearby.setTextColor(getResources().getColor(R.color.loginEnd));
            tvPopular.setTextColor(getResources().getColor(R.color.appColorBlack));
            dotNearby.setImageResource(R.drawable.dot);
            dotPopular.setImageDrawable(null);

            navController.navigate(R.id.nearbyFragment);
        });

        explore.setOnClickListener(view -> {
            changeBottomIcons(2);
            navController.navigate(R.id.exploreFragment);
        });

        home.setOnClickListener(view -> {
            changeBottomIcons(1);
            navController.navigate(R.id.popularFragment);
        });

        profile.setOnClickListener(view -> {
            changeBottomIcons(4);
            navController.navigate(R.id.profileFragment);
        });

        chat.setOnClickListener(view -> {
            changeBottomIcons(3);
            navController.navigate(R.id.chatFragment);
        });

    }

    private void changeBottomIcons(int i) {
        Log.e("Done","Here");
        explore.setImageResource(R.drawable.explore);
        home.setImageResource(R.drawable.home);
        profile.setImageResource(R.drawable.profile);
        chat.setImageResource(R.drawable.messages);

        switch (i) {
            case 1:
                home.setImageResource(R.drawable.home1);
                ivExplore.setVisibility(View.GONE);
                ivTrophy.setVisibility(View.VISIBLE);
                lTabContainer.setVisibility(View.VISIBLE);
                break;
            case 2:
                explore.setImageResource(R.drawable.explore1);
                ivExplore.setVisibility(View.VISIBLE);
                ivTrophy.setVisibility(View.GONE);
                lTabContainer.setVisibility(View.GONE);
                break;
            case 3:
                chat.setImageResource(R.drawable.messages1);
                ivExplore.setVisibility(View.GONE);
                ivTrophy.setVisibility(View.GONE);
                lTabContainer.setVisibility(View.GONE);
                break;
            case 4:
                profile.setImageResource(R.drawable.profile_one);
                ivExplore.setVisibility(View.GONE);
                ivTrophy.setVisibility(View.GONE);
                lTabContainer.setVisibility(View.GONE);
                break;
        }
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);

        return true;
    }
}