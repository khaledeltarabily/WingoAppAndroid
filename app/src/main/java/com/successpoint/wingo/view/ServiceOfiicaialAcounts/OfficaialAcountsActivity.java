package com.successpoint.wingo.view.ServiceOfiicaialAcounts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.OfficaialAcounts;
import com.successpoint.wingo.model.TopTrendingModel;
import com.successpoint.wingo.view.mainactivity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OfficaialAcountsActivity extends MvpActivity<OfficaialView, OfficailAcountsPresenter> implements OfficaialView {

    private RecyclerView rvTrending;
    private Button btDone;
    List<OfficaialAcounts> global_list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toptrending);
        global_list = new ArrayList<>();
        rvTrending = findViewById(R.id.rvTrending);
        btDone = findViewById(R.id.btDone);
        btDone.setOnClickListener(view -> startActivity(new Intent(OfficaialAcountsActivity.this, MainActivity.class)));
        presenter.getOfficaialAcounts();
    }

    @NonNull
    @Override
    public OfficailAcountsPresenter createPresenter() {
        return new OfficailAcountsPresenter(this, this);
    }

    public void finish(View view) {
        finish();
    }

    public void skip(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void publishData(List<OfficaialAcounts> content) {
        global_list = content;
        OfficailAdapter adapter = new OfficailAdapter(this, content);         
        rvTrending.setAdapter(adapter);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        rvTrending.setLayoutManager(gridLayoutManager);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void DoneExecuted(boolean done) {
        startActivity(new Intent(OfficaialAcountsActivity.this, MainActivity.class));
    }
}