package com.successpoint.wingo.view.mainactivity.fragments.nearby.adapter;

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
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.NearbyModel;
import com.successpoint.wingo.model.NearbyModel.Content;
import com.successpoint.wingo.view.LiveData.TestDetailFragment;
import com.successpoint.wingo.view.showLiveView.TestContent;
import com.successpoint.wingo.view.showLiveView.ViewPagerTestSlider;

import java.io.Serializable;
import java.util.List;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ViewHolder> {

    private static final String TAG = NearbyAdapter.class.getSimpleName();
    private Context mContext;
    private List<NearbyModel.Content> mList;
    private OnItemClickListener mListener;

    public NearbyAdapter(Context context, List<NearbyModel.Content> list, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mList = list;
        this.mListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.row_nearby, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NearbyModel.Content item = mList.get(position);

        //TODO setup views
        holder.setData(item,position);
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
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(NearbyModel.Content item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //TODO Bind views
        private ImageView ivProfile;
        private TextView tvName;
        private TextView tvNumber,tvDistance;

        public ViewHolder(View itemView) {
            super(itemView);

            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvName = itemView.findViewById(R.id.tvName);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvDistance = itemView.findViewById(R.id.tvDistance);

        }

        public void setData(NearbyModel.Content item, int position) {

            Glide.with(mContext).load(item.getStreamCover()).error(R.drawable.wing).into(ivProfile);
            tvName.setText(item.getProdcaster().getName());
            tvNumber.setText(String.valueOf(item.getViewers()) );
             tvDistance.setText(item.getDistance()+" Km");
        }
    }
}