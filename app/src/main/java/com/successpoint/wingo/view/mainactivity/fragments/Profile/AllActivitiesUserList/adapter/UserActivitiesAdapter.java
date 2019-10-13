package com.successpoint.wingo.view.mainactivity.fragments.Profile.AllActivitiesUserList.adapter;

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
import com.successpoint.wingo.model.TopFansModel;
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

public class UserActivitiesAdapter extends RecyclerView.Adapter<UserActivitiesAdapter.ViewHolder> {

    private static final String TAG = UserActivitiesAdapter.class.getSimpleName();
    private Context mContext;
    private List<TopFansModel.Datum> mList;
    private OnItemClickListener mListener;

    public UserActivitiesAdapter(Context context, List<TopFansModel.Datum> list, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mList = list;
        this.mListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.row_fan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TopFansModel.Datum item = mList.get(position);

        //TODO setup views
        holder.setData(item, position);
        holder.itemView.setOnClickListener(v -> {
            ApiRequest apiRequest = ApiRequest.getInstance(mContext);
            HashMap<String,String> params = new HashMap<>();
            params.put("api_token", Constants.api_token);
            params.put("user_token", Mainsharedprefs.getToken());
            params.put("user_id",item.getUserId());
            apiRequest.createPostRequest(Constants.UserProfileFromToken, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
                @Override
                public void onSuccess(String response) throws JSONException {
                    Log.e("Done","response" + response);
                    LogMe(response);
                    JSONObject result = new JSONObject(response);
                    UserModelObject modelObject = new UserModelObject(result.getJSONObject("content"));
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("model",modelObject);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("ID",modelObject.getUser_id());
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
        void onItemClick(TopFansModel.Datum item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //TODO Bind views
        private TextView tvIndex;
        private ImageView ivTag;
        private CircleImageView ivProfile;
        private TextView tvUserName;
        private TextView tvContrVal;
        private TextView tvLevel;

        public ViewHolder(View itemView) {
            super(itemView);

            tvIndex = itemView.findViewById(R.id.tvIndex);
            ivTag = itemView.findViewById(R.id.ivTag);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvContrVal = itemView.findViewById(R.id.tvContrVal);
            tvLevel = itemView.findViewById(R.id.tvLevel);
        }

        public void setData(TopFansModel.Datum item, int position) {
            tvIndex.setText(String.valueOf(position));
            switch (position) {
                case 0:
                    ivTag.setImageResource(R.drawable.gold);
                    break;
                case 1:
                    ivTag.setImageResource(R.drawable.silver);
                    break;
                case 2:
                    ivTag.setImageResource(R.drawable.pronze);
                    break;
            }
            Glide.with(mContext)
                    .load(item.getPicture())
                    .into(ivProfile);
            tvUserName.setText(item.getName());
            tvContrVal.setText(String.valueOf(item.getAmount()));
            tvLevel.setText(String.valueOf(item.getLevel()));

//            ivFollow.setOnClickListener(view -> {
//                HashMap<String,String> params = new HashMap<>();
//                params.put("api_token", Constants.api_token);
//                params.put("user_token", Mainsharedprefs.getToken());
//                params.put("user_ids",item.getUserId());
//                apiRequest.createPostRequest(Constants.Follow, params, Priority.MEDIUM, new ApiRequest.ServiceCallback() {
//                    @Override
//                    public void onSuccess(Object response) throws JSONException {
//                        tvFollowing.setVisibility(View.VISIBLE);
//                        ivFollow.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onFail(ANError error) throws JSONException {
//
//                    }
//                });
//            });

        }
    }
}