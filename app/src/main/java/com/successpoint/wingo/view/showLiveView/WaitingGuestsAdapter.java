package com.successpoint.wingo.view.showLiveView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.successpoint.wingo.AllUrls;
import com.successpoint.wingo.App;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.TopTrendingModel;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.utils.UtilsFirebase.DataFromFirebaseOnAdded;
import com.successpoint.wingo.utils.UtilsFirebase.setDataToServer;

import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class WaitingGuestsAdapter extends RecyclerView.Adapter<WaitingGuestsAdapter.ViewHolder> {

    private static final String TAG = WaitingGuestsAdapter.class.getSimpleName();
    private Context mContext;
    private List<UserModelObject> mList;
    String live_id;

    public WaitingGuestsAdapter(Context context, List<UserModelObject> list , String live_id ) {
        this.mContext = context;
        this.mList = list;
        this.live_id = live_id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.row_top_trending, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserModelObject item = mList.get(position);

        //TODO setup views
//        holder.setData(item, position);
//        holder.itemView.setOnClickListener(v -> {
//
//        });
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
        private ImageView DELETE,ACCEPT;
        public ViewHolder(View itemView) {
            super(itemView);
            ivUser = itemView.findViewById(R.id.user_image);
            ACCEPT = itemView.findViewById(R.id.accept);
            tvUsername = itemView.findViewById(R.id.user_name);
            DELETE = itemView.findViewById(R.id.delete);
        }

        public void setData(UserModelObject item, int position) {
            Glide.with(mContext)
                    .load(item.getImages().get(0))
                    .fallback(R.drawable.profiler)
                    .error(R.drawable.profiler)
                    .into(ivUser);

            tvUsername.setText(item.getName());
            ACCEPT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DELETE.performClick();
                    JoinAsGuest(position);
                }});
            DELETE.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference().child(AllUrls.Listen_To_Guests(live_id) + "/" +item.getUser_id()).removeValue();
                }
            });
        }
    public void JoinAsGuest(int position) {
        Log.e("DATATA","DATAOiutside");
        HashMap<String,String> params = new HashMap<>();
        params.put("name",App.userModelObject_of_Project.getName());
        if (App.userModelObject_of_Project.getImages().size()>0)
            params.put("image", App.userModelObject_of_Project.getImages().get(0));
        else
            params.put("image", "");
        params.put("Vip", String.valueOf(App.userModelObject_of_Project.getVip()));
        params.put("Bio", App.userModelObject_of_Project.getBio());
        setDataToServer data = new setDataToServer(mContext, AllUrls.Listen_To_Guests(live_id) + "/" +mList.get(position).getUser_id(),params);
        try {
            data.Data(new DataFromFirebaseOnAdded() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("DATATA","DATAInternal");

                }
                @Override
                public void onFailure(Exception e) {
                    Log.e("DATATA","DATAEorror");
                }
            });
        } catch (Exception e) {

        }
    }
    }
}