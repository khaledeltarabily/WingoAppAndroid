package com.successpoint.wingo.view.mainactivity.fragments.LeaderBoard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.TopFansModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LeaderBoardActivity extends MvpActivity<LeaderBoardView, LeaderBoardPresenter> implements LeaderBoardView {
    private ImageView back;
    List<TopFansModel.Datum> list_D_HOST, list_W_HOST, list_m_HOST, list_D_supporters,list_W_supporters,list_M_supporters;

    List<TopFansModel.Datum> current_is_top;
    List<TopFansModel.Datum> current_is_bottom;
    TextView first_name,first_count,first_level;
    TextView second_name,second_count,second_level;
    TextView third_name,third_count,third_level;
    ImageView first_image,second_image,third_image;
    CardView viewAll_first;
    CardView viewAll_second;

    TextView supporters_first_name,supporters_first_count,supporters_first_level;
    TextView supporters_second_name,supporters_second_count,supporters_second_level;
    TextView supporters_third_name,supporters_third_count,supporters_third_level;
    ImageView supporters_first_image,supporters_second_image,supporters_third_image;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosts_rank);
        current_is_bottom=new ArrayList<>();
        current_is_top=new ArrayList<>();
        list_D_HOST=new ArrayList<>();
        list_W_HOST=new ArrayList<>();
        list_m_HOST=new ArrayList<>();
        list_D_supporters=new ArrayList<>();
        list_W_supporters=new ArrayList<>();
        list_M_supporters=new ArrayList<>();
        back = findViewById(R.id.back);
        viewAll_first = findViewById(R.id.viewAll_first);
        viewAll_second = findViewById(R.id.viewAll_second);
        first_image = findViewById(R.id.first_image);
        second_image = findViewById(R.id.second_image);
        third_image = findViewById(R.id.third_image);
        first_name = findViewById(R.id.first_name);
        first_count = findViewById(R.id.first_wings);
        first_level = findViewById(R.id.first_level);

        second_name = findViewById(R.id.second_name);
        second_count = findViewById(R.id.second_wings);
        second_level = findViewById(R.id.second_level);

        third_name = findViewById(R.id.third_name);
        third_count = findViewById(R.id.third_wings);
        third_level = findViewById(R.id.third_level);

        supporters_first_image = findViewById(R.id.supporters_first_image);
        supporters_second_image = findViewById(R.id.supporters_second_image);
        supporters_third_image = findViewById(R.id.supporters_third_image);
        supporters_first_name = findViewById(R.id.supporters_first_name);
        supporters_first_count = findViewById(R.id.supporters_first_wings);
        supporters_first_level = findViewById(R.id.supporters_first_level);

        supporters_second_name = findViewById(R.id.supporters_second_name);
        supporters_second_count = findViewById(R.id.supporters_second_wings);
        supporters_second_level = findViewById(R.id.supporters_second_level);

        supporters_third_name = findViewById(R.id.supporters_third_name);
        supporters_third_count = findViewById(R.id.supporters_third_wings);
        supporters_third_level = findViewById(R.id.supporters_third_level);
        
        
//        back.setOnClickListener(view -> finish());
//        viewAll_first.setOnClickListener(view ->{
//            Intent intent = new Intent(LeaderBoardActivity.this, LeaderBoardActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("data", (Serializable) current_is_top);
//            intent.putExtras(bundle);
//            startActivity(intent);
//        });
//        viewAll_second.setOnClickListener(view ->{
//            Intent intent = new Intent(LeaderBoardActivity.this, LeaderBoardActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("data", (Serializable) current_is_bottom);
//            intent.putExtras(bundle);
//            startActivity(intent);
//        });

        TabLayout tabLayout = findViewById(R.id.hosts);
        MakeList(list_D_HOST,"d");
        MakeListSupporters(list_D_supporters,"d");


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0 )
                    MakeList(list_D_HOST,"d");
                else if (tab.getPosition() == 1)
                    MakeList(list_W_HOST,"w");
                else
                    MakeList(list_W_HOST,"m");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        TabLayout tabLayout_supports = findViewById(R.id.supporters);

        tabLayout_supports.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0 )
                    MakeListSupporters(list_D_supporters,"d");
                else if (tab.getPosition() == 1)
                    MakeListSupporters(list_W_supporters,"w");
                else
                    MakeListSupporters(list_M_supporters,"m");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @NonNull
    @Override
    public LeaderBoardPresenter createPresenter() {
        return new LeaderBoardPresenter(this, this);
    }

    @Override
    public void publishTopFans(TopFansModel response,String type,String time) {
        if (type.equals("hosts")){
            if (time.equals("d")){
                list_D_HOST = response.getData();
                MakeList(list_D_HOST,"d");
            }
            else if(time.equals("w")){
                list_W_HOST = response.getData();
                MakeList(list_W_supporters,"w");
            }
            else {
                list_m_HOST = response.getData();
                MakeList(list_W_HOST,"m");
            }
            current_is_top = response.getData();
        }
        else {
            if (time.equals("d")){
                list_D_supporters = response.getData();
                MakeListSupporters(list_D_supporters,"d");

            }
            else if(time.equals("w")){
                list_W_supporters = response.getData();
                MakeListSupporters(list_W_supporters,"w");

            }
            else {
                list_M_supporters = response.getData();
                MakeListSupporters(list_M_supporters,"m");


            }
            current_is_bottom = response.getData();

        }
    }

    void MakeList(List<TopFansModel.Datum> list,String time){
        if (list.size()>0){
            Log.e("FFFFFFF",list.size()+"mm");
            Log.e("FFFFFFF",list.toString());
            first_name.setText(list.get(0).getName());
            first_count.setText(list.get(0).getAmount()+"");
            first_level.setText(list.get(0).getLevel()+"");
            Glide.with(LeaderBoardActivity.this).load(list.get(0).getPicture()).into(first_image);

            second_name.setText(list.get(1).getName());
            second_count.setText(list.get(1).getAmount()+"");
            second_level.setText(list.get(1).getLevel()+"");
            Glide.with(LeaderBoardActivity.this).load(list.get(1).getPicture()).into(second_image);

            third_name.setText(list.get(2).getName());
            third_count.setText(list.get(2).getAmount()+"");
            third_level.setText(list.get(2).getLevel()+"");
            Glide.with(LeaderBoardActivity.this).load(list.get(2).getPicture()).into(third_image);
        }
        else {
            presenter.GetLeaderBoard("hosts",time);
        }
        current_is_top = list;
    }
    void MakeListSupporters(List<TopFansModel.Datum> list,String time){
        if (list.size()>0){
            supporters_first_name.setText(list.get(0).getName());
            supporters_first_count.setText(list.get(0).getAmount()+"");
            supporters_first_level.setText(list.get(0).getLevel()+"");
            Glide.with(LeaderBoardActivity.this).load(list.get(0).getPicture()).into(supporters_first_image);

            supporters_second_name.setText(list.get(1).getName());
            supporters_second_count.setText(list.get(1).getAmount()+"");
            supporters_second_level.setText(list.get(1).getLevel()+"");
            Glide.with(LeaderBoardActivity.this).load(list.get(1).getPicture()).into(supporters_second_image);

            supporters_third_name.setText(list.get(2).getName());
            supporters_third_count.setText(list.get(2).getAmount()+"");
            supporters_third_level.setText(list.get(2).getLevel()+"");
            Glide.with(LeaderBoardActivity.this).load(list.get(2).getPicture()).into(supporters_third_image);
        }
        else {
            presenter.GetLeaderBoard("supporters",time);
        }
        current_is_bottom = list;
    }
}