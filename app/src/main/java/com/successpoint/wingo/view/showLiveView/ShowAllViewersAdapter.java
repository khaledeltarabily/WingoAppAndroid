package com.successpoint.wingo.view.showLiveView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.TopTrendingModel;
import com.successpoint.wingo.model.UserModelObject;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShowAllViewersAdapter extends RecyclerView.Adapter<ShowAllViewersAdapter.ViewHolder> {

    private static final String TAG = ShowAllViewersAdapter.class.getSimpleName();
    private Context mContext;
    private List<UserModelObject> mList;
    private OnItemClickListener mListener;

    public ShowAllViewersAdapter(Context context, List<UserModelObject> list, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mList = list;
        this.mListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.image_circles, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserModelObject item = mList.get(position);

        //TODO setup views
        holder.setData(item, position);
        holder.itemView.setOnClickListener(v -> {
            Log.e("DATATA","CLICKED");
            mListener.onItemClick(mList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(UserModelObject item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //TODO Bind views
        private CircleImageView ivUser;
        public ViewHolder(View itemView) {
            super(itemView);
            ivUser = itemView.findViewById(R.id.image);
        }

        public void setData(UserModelObject item, int position) {
            if (item.getImages().size()>0) {
                Glide.with(mContext)
                        .load(item.getImages().get(0))
                        .into(ivUser);
            }
        }
    }
}