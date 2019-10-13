package com.successpoint.wingo.view.showLiveView;

import android.content.Context;
import android.service.autofill.UserData;
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

public class TopFansAdapter extends RecyclerView.Adapter<TopFansAdapter.ViewHolder> {

    private static final String TAG = TopFansAdapter.class.getSimpleName();
    private Context mContext;
    private List<UserModelObject> mList;

    public TopFansAdapter(Context context, List<UserModelObject> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.topfansday_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserModelObject item = mList.get(position);

        //TODO setup views
        holder.setData(item, position);

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
        private TextView tvUsername;
        public ViewHolder(View itemView) {
            super(itemView);
            ivUser = itemView.findViewById(R.id.user_image);
            tvUsername = itemView.findViewById(R.id.user_name);
        }

        public void setData(UserModelObject item, int position) {
            if (item.getImages().size()>0) {
                Glide.with(mContext)
                        .load(item.getImages().get(0))
                        .into(ivUser);
            }

            tvUsername.setText(item.getName());
        }
    }
}