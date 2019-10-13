package com.successpoint.wingo.view.search.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.bumptech.glide.Glide;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.FollowingModel;
import com.successpoint.wingo.model.FollowingModel.Content;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.Mainsharedprefs;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.ActivityProfile.ProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.successpoint.wingo.utils.CommonMethods.LogMe;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {

    private static final String TAG = FollowingAdapter.class.getSimpleName();
    private Context mContext;
    private List<FollowingModel.Content> mList;
    private OnItemClickListener mListener;

    public FollowingAdapter(Context context, List<FollowingModel.Content> list, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mList = list;
        this.mListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.row_following, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FollowingModel.Content item = mList.get(position);

        //TODO setup views
        holder.setData(item, position);
        holder.itemView.setOnClickListener(v -> {
            ApiRequest apiRequest = ApiRequest.getInstance(mContext);
            HashMap<String,String> params = new HashMap<>();
            params.put("api_token", Constants.api_token);
            params.put("user_token", Mainsharedprefs.getToken());
            params.put("user_id",item.getUserId());
            apiRequest.createPostRequest(Constants.UserProfile, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
                @Override
                public void onSuccess(String response) throws JSONException {
                    Log.e("Done","response" + response);
                    LogMe(response);
                    JSONObject result = new JSONObject(response);
                    UserModelObject modelObject = new UserModelObject(result.getJSONObject("content"));
                    modelObject.setUser_id(item.getUserId());
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("model",modelObject);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("ID",item.getUserId());
                    mContext.startActivity(intent);
                }

                @Override
                public void onFail(ANError error) throws JSONException {

                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(FollowingModel.Content item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //TODO Bind views
        private CircleImageView ivProfileImg;
        private TextView tvLevel, tvName, tvId, tvFans, tvLive;
        private ImageView ivFollow;
        private TextView tvFollowing;

        ApiRequest apiRequest;
        public ViewHolder(View itemView) {
            super(itemView);

            ivProfileImg = itemView.findViewById(R.id.ivProfileImg);
            tvLevel = itemView.findViewById(R.id.tvLevel);
            tvName = itemView.findViewById(R.id.tvName);
            tvId = itemView.findViewById(R.id.tvId);
            tvFans = itemView.findViewById(R.id.tvFans);
            tvLive = itemView.findViewById(R.id.tvLive);
            ivFollow = itemView.findViewById(R.id.ivFollow);
            tvFollowing = itemView.findViewById(R.id.tvFollowing);

            apiRequest = ApiRequest.getInstance(mContext);
        }

        public void setData(FollowingModel.Content item, int position) {
            Glide.with(mContext)
                    .load(item.getPicture())
                    .into(ivProfileImg);

            tvLevel.setText("Lv"+item.getLevel());
            tvName.setText(item.getName());
            tvId.setText("ID:"+item.getLevel());
            tvFans.setText("Fans:"+item.getFans());
            ivFollow.setOnClickListener(view -> {
                HashMap<String,String> params = new HashMap<>();
                params.put("api_token",Constants.api_token);
                params.put("user_token", Mainsharedprefs.getToken());
                params.put("user_id",item.getUserId());
                apiRequest.createPostRequest(Constants.Follow, params, Priority.MEDIUM, new ApiRequest.ServiceCallback() {
                    @Override
                    public void onSuccess(Object response) throws JSONException {
                       tvFollowing.setVisibility(View.VISIBLE);
                       ivFollow.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFail(ANError error) throws JSONException {

                    }
                });
            });
        }
    }
}