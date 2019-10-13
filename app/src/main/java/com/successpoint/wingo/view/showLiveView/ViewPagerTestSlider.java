package com.successpoint.wingo.view.showLiveView;

import android.content.res.Resources;
import android.os.Bundle;

import com.successpoint.wingo.VerticalViewPage;
import com.successpoint.wingo.model.PopularMainModel;
import com.successpoint.wingo.R;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerTestSlider extends AppCompatActivity {


    private List<PopularMainModel> mList;
    VerticalViewPage viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_view);
        Resources res = getResources();
        //Load XML TESTS
        TestContent.LoadTests(res.openRawResource(R.raw.tests));
        // TODO: replace with a real list adapter.
        mList = new ArrayList<>();
        mList = (List<PopularMainModel>) getIntent().getExtras().getSerializable("data");
         viewPager =findViewById(R.id.ViewPager);
        int position = getIntent().getExtras().getInt("position");
        SetupDataToPager(position);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position != 1) {
                    SetupDataToPager(position);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    void SetupDataToPager(int position_new){
        int position = position_new;
        int downPos = position - 1 ;
        int topPos = position + 1 ;
        FragmentImage first =   null;
        if (position != 0 ){
            first =   new FragmentImage();
            Bundle bundle2 = new Bundle();
            bundle2.putString("url",mList.get(downPos).streamerImgUrl);
            first.setArguments(bundle2);
        }
        FragmentImage third =   null;
        if (position != (mList.size() -1 ) ){
            third =   new FragmentImage();
            Bundle bundle2 = new Bundle();
            bundle2.putString("url",mList.get(topPos).streamerImgUrl);
            third.setArguments(bundle2);
        }

        ShowLiveActivity show =   new ShowLiveActivity();
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("data",mList.get(position));
        bundle2.putString("Url",mList.get(position).streamerUrl.get(0));
        show.setArguments(bundle2);

        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager(),first,show,third);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(1);
    }
}



class MyPagerAdapter extends FragmentStatePagerAdapter{
    FragmentImage first;
    ShowLiveActivity show;
    FragmentImage third;
    public MyPagerAdapter(FragmentManager fragmentManager, FragmentImage first , ShowLiveActivity show , FragmentImage third ){
        super(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.first = first;
        this.show = show;
        this.third = third;
    }
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                if (first == null)
                    return show;
                else
                    return first;
            case 1:
                if (first == null)
                    return third;
                else
                    return show;
            case 2:
                return third;
        }
        return null;
    }

    @Override
    public int getCount() {
        if (first != null && third != null) {
            return 3;
        }
        else if (first == null && third == null) {
            return 1;
        }
        else {
            return 2;
        }
    }
}


