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

public class GustsUserAdapter extends RecyclerView.Adapter<GustsUserAdapter.ViewHolder> {

    private static final String TAG = GustsUserAdapter.class.getSimpleName();
    private Context mContext;
    private List<UserModelObject> mList;
    String Live_Id;

    public GustsUserAdapter(Context context, List<UserModelObject> mList,String Live_Id) {
        this.mList = mList;
        this.mContext = context;
        this.Live_Id = Live_Id;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.guest_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserModelObject item = mList.get(position);

        //TODO setup views
        holder.setData(item, position);
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
        private TextView accept;
        private TextView tvUsername;
        private ImageView delete;
        public ViewHolder(View itemView) {
            super(itemView);
            ivUser = itemView.findViewById(R.id.user_image);
            accept = itemView.findViewById(R.id.accept);
            tvUsername = itemView.findViewById(R.id.user_name);
            delete = itemView.findViewById(R.id.delete);
        }

        public void setData(UserModelObject item, int position) {
            Glide.with(mContext)
                    .load(item.getImages().get(0))
                    .into(ivUser);

            tvUsername.setText(item.getName());

            if (item.getUser_id().equals(App.userModelObject_of_Project.getUser_id()))
                delete.setVisibility(View.VISIBLE);
            else
                delete.setVisibility(View.GONE);

            delete.setOnClickListener(view -> FirebaseDatabase.getInstance().getReference().child(AllUrls.Listen_To_Guests(Live_Id) + "/" +item.getUser_id()).removeValue());
            accept.setVisibility(View.GONE);
//            accept.setOnClickListener(view -> {
//                delete.performClick();
//                JoinAsGuest(position);
//            });
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
            setDataToServer data = new setDataToServer(mContext, AllUrls.Listen_To_Guests(Live_Id) + "/" +mList.get(position).getUser_id(),params);
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