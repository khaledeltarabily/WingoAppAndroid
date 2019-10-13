package com.successpoint.wingo.view.mainactivity.fragments.Profile.AllActivitiesUserList;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.TopFansModel;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.AllActivitiesUserList.adapter.UserActivitiesAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShowUserActivities extends MvpActivity<UserListDataView, UserListDataPresenter> implements UserListDataView {
    private ImageView back;
    private TextView tvFull;
    private RecyclerView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topfans);

        back = findViewById(R.id.back);
        tvFull = findViewById(R.id.tvFull);
        list = findViewById(R.id.rvFansList);

        Bundle bundle= getIntent().getExtras();
        if (bundle!=null) {
            if (bundle.getString("user_id")==null)
                presenter.getActivityByText(bundle.getString("type"));
            else
                presenter.getActivityByTextByUser_id(bundle.getString("type"),bundle.getString("user_id"));
        }

        back.setOnClickListener(view -> finish());
    }

    @NonNull
    @Override
    public UserListDataPresenter createPresenter() {
        return new UserListDataPresenter(this, this);
    }

    @Override
    public void publishTopFans(TopFansModel response) {
//        int total = 0;
//        for (int i = 0; i < response.getData().size(); i++) {
//            total = total+response.getData().get(i).getAmount();
//        }
//        tvFull.setText(total+"");
        UserActivitiesAdapter adapter = new UserActivitiesAdapter(this, response.getData(), item -> {

        });
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();
    }
}