package com.successpoint.wingo.view.toptrending.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.TopTrendingModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopTrendingAdapter extends RecyclerView.Adapter<TopTrendingAdapter.ViewHolder> {

    private static final String TAG = TopTrendingAdapter.class.getSimpleName();
    private Context mContext;
    private List<TopTrendingModel.Content> mList;
    private OnItemClickListener mListener;

    public TopTrendingAdapter(Context context, List<TopTrendingModel.Content> list, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mList = list;
        this.mListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.row_top_trending, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TopTrendingModel.Content item = mList.get(position);

        //TODO setup views
        holder.setData(item, position);
        holder.itemView.setOnClickListener(v -> {
            mListener.onItemClick(item);
            item.setSelected(!item.isSelected());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(TopTrendingModel.Content item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //TODO Bind views
        private CircleImageView ivUser;
        private ImageView ivSelect;
        private TextView tvUsername;
        private TextView tvFollowers;
        public ViewHolder(View itemView) {
            super(itemView);
            ivUser = itemView.findViewById(R.id.ivUser);
            ivSelect = itemView.findViewById(R.id.ivSelect);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvFollowers = itemView.findViewById(R.id.tvFollowers);
        }

        public void setData(TopTrendingModel.Content item, int position) {
            Glide.with(mContext)
                    .load(item.getPictureIndex())
                    .fallback(R.drawable.profiler)
                    .error(R.drawable.profiler)
                    .into(ivUser);

            ivSelect.setImageResource(item.isSelected() ? R.drawable.select : R.drawable.deselect);
            tvUsername.setText(item.getName());
            tvFollowers.setText(item.getFans().toString());
        }
    }
}