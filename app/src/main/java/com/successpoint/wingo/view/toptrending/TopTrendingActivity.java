package com.successpoint.wingo.view.toptrending;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.TopTrendingModel;
import com.successpoint.wingo.view.ServiceOfiicaialAcounts.OfficaialAcountsActivity;
import com.successpoint.wingo.view.mainactivity.MainActivity;
import com.successpoint.wingo.view.toptrending.adapter.TopTrendingAdapter;

import java.util.ArrayList;
import java.util.List;

public class TopTrendingActivity extends MvpActivity<TopTrendingView, TopTrendingPresenter> implements TopTrendingView {

    private RecyclerView rvTrending;
    private Button btDone;
    List<TopTrendingModel.Content> global_list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toptrending);
        global_list = new ArrayList<>();
        rvTrending = findViewById(R.id.rvTrending);
        btDone = findViewById(R.id.btDone);
        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Done","onClick Done");
                String list_users = "";
                for (int i=0;i<global_list.size();i++){
                    if (global_list.get(i).isSelected()) {
                        if (list_users.equals(""))
                            list_users = String.valueOf(global_list.get(i).getUserId());
                        else
                            list_users += "|" + String.valueOf(global_list.get(i).getUserId());
                    }
                }
                if (list_users.equals(""))
                    startActivity(new Intent(TopTrendingActivity.this, MainActivity.class));
                else
                    presenter.SetUsersToFollow(list_users);
            }

        });
        presenter.getTopTrending();
    }

    @NonNull
    @Override
    public TopTrendingPresenter createPresenter() {
        return new TopTrendingPresenter(this, this);
    }

    public void finish(View view) {
        finish();
    }

    public void skip(View view) {
        startActivity(new Intent(TopTrendingActivity.this, OfficaialAcountsActivity.class));
    }

    @Override
    public void publishData(List<TopTrendingModel.Content> content) {
        global_list = content;
        TopTrendingAdapter adapter = new TopTrendingAdapter(this, content, new TopTrendingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TopTrendingModel.Content item) {

            }
        });
        rvTrending.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3, RecyclerView.VERTICAL,false);
        rvTrending.setLayoutManager(gridLayoutManager);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void DoneExecuted(boolean done) {
        startActivity(new Intent(TopTrendingActivity.this, OfficaialAcountsActivity.class));
    }
}