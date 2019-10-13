package com.successpoint.wingo.view.ChatFragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.successpoint.wingo.AllUrls;
import com.successpoint.wingo.App;
import com.successpoint.wingo.model.MessageModel;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.utils.FirebaseImageLoader;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;
import com.successpoint.wingo.R;
import com.successpoint.wingo.R;

public class MessagesRecycler extends RecyclerView.Adapter<MessagesRecycler.ViewHolder> {
     ArrayList<MessageModel> ListData;
     Context context;
     UserModelObject user_model;
    public MessagesRecycler(Context context, ArrayList<MessageModel> ListData, UserModelObject user_model){
        this.ListData=ListData;
        this.user_model = user_model;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        if (ListData.get(position).isImage()) {
            Log.e("REQ","gg");
            if (ListData.get(position).getSender_id().equals(App.userModelObject_of_Project.getUser_id())) {
                return R.layout.senderchatimage;
            }
            else {
                return R.layout.receiverchatimage;
            }
        }
        else {
            Log.e("REQ","ggh");
            if (ListData.get(position).getSender_id().equals(App.userModelObject_of_Project.getUser_id())) {
                return R.layout.senderchat;
            }
            else {
                return R.layout.receiverchat;
            }
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.messageTime.setText(ListData.get(position).getMessage_time());
        if (ListData.get(position).getSender_id().equals(App.userModelObject_of_Project.getUser_id())) {
            if (App.userModelObject_of_Project.getImages().size()>0)
                Glide.with(context).load(App.userModelObject_of_Project.getImages().get(0)).into(holder.userImage);
        } else {
            if (user_model.getImages().size()>0)
                Glide.with(context).load(user_model.getImages().get(0)).into(holder.userImage);

        }

        Log.e("GGG",ListData.get(position).getMessage_text());
        if (ListData.get(position).isImage()){
            Glide.with(context).using(new FirebaseImageLoader()).load(FirebaseStorage.getInstance().getReference()
                    .child(AllUrls.GetMessageUrlFromMessageId(ListData.get(position).getMessage_id())+".jpg")).into(holder.image);
        }
        else
//            holder.messageText.setText("xxxxxxxxxxxxx");
            holder.messageText.setText(ListData.get(position).getMessage_text());


    }

    @Override
    public int getItemCount() {
        return ListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView userImage,image;
        TextView messageText,messageTime;
        public ViewHolder(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.image);
            messageText = itemView.findViewById(R.id.messageText);
            messageTime=itemView.findViewById(R.id.messageTime);
            image = itemView.findViewById(R.id.image_chat);
        }
        @Override
        public void onClick(View v) {

        }
    }
}
