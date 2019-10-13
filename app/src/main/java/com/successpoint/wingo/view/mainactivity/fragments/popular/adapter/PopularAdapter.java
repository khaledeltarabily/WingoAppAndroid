package com.successpoint.wingo.view.mainactivity.fragments.popular.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.successpoint.wingo.R;
import com.successpoint.wingo.view.showLiveView.ViewPagerTestSlider;
import com.successpoint.wingo.model.PopularMainModel;
import com.successpoint.wingo.view.LiveData.TestDetailFragment;
import com.successpoint.wingo.view.showLiveView.TestContent;

import java.io.Serializable;
import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {

    private static final String TAG = PopularAdapter.class.getSimpleName();
    private Context mContext;
    private List<PopularMainModel> mList;
    private OnItemClickListener mListener;

    public PopularAdapter(Context context, List<PopularMainModel> list, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mList = list;
        this.mListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        if (viewType == 1) {
            view = inflater.inflate(R.layout.row_popular, parent, false);
        } else {
            view = inflater.inflate(R.layout.row_home_slider, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PopularMainModel item = mList.get(position);

        //TODO setup views
        holder.setData(item, position);
        holder.itemView.setOnClickListener(v -> {

            Resources res = mContext.getResources();

            //Load XML TESTS
            TestContent.LoadTests(res.openRawResource(R.raw.tests));

            TestContent.SetTestItem( 1 );

            Intent intent = new Intent(mContext,ViewPagerTestSlider.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", (Serializable) mList);
            bundle.putInt("position",position);
            bundle.putString(TestDetailFragment.ARG_ITEM_ID, 1+"");
            intent.putExtras(bundle);
            mContext.startActivity(intent);

//            mListener.onItemClick(item);
        });
    }

    @Override
    public int getItemViewType(int position) {
        PopularMainModel item = mList.get(position);
        if (item.type.equals("Stream")) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(PopularMainModel item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //TODO Bind views
        private ImageView ivProfile;
        private TextView tvName, tvNumber;
        private PagerIndicator customIndicator;
        private SliderLayout slider;

        public ViewHolder(View itemView) {
            super(itemView);

            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvName = itemView.findViewById(R.id.tvName);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            customIndicator = itemView.findViewById(R.id.custom_indicator);
            slider = itemView.findViewById(R.id.slider);

        }

        public void setData(PopularMainModel item, int position) {
            if (ivProfile != null) {
                Glide.with(mContext)
                        .load(item.streamerImgUrl)
                        .into(ivProfile);
                tvName.setText(item.streamerName);
                tvNumber.setText(item.viewrs);
            } else {
                for (int i = 0; i < item.streamerUrl.size(); i++) {
                    DefaultSliderView sliderView = new DefaultSliderView(mContext);
                    sliderView.image(item.streamerUrl.get(i));
                    slider.addSlider(sliderView);
                }
            }
        }
    }
}