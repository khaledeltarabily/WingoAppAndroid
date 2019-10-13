package com.successpoint.wingo.view.ChatFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.successpoint.wingo.AllUrls;
import com.successpoint.wingo.model.MainChats;
import com.successpoint.wingo.model.MessageModel;
import com.successpoint.wingo.utils.UtilsFirebase.DataFromFirebaseOnGet;
import com.successpoint.wingo.utils.UtilsFirebase.getDataFromServer;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import com.successpoint.wingo.R;

public class MainChatsRecycler extends RecyclerView.Adapter<MainChatsRecycler.ViewHolder> {
     ArrayList<MainChats> ListData;
     Context context;
    public MainChatsRecycler(Context context,ArrayList<MainChats> ListData){
        this.ListData=ListData;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.outsidechat,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
      holder.messageTime.setText(ListData.get(position).getLast_Message().getMessage_time());
      holder.messageText.setText(ListData.get(position).getLast_Message().getMessage_text());
      holder.userName.setText(ListData.get(position).getUser_data().getName());
      if (ListData.get(position).getUser_data().getImages().size()>0)
          Glide.with(context).load(ListData.get(position).getUser_data().getImages().get(0)).into(holder.userImage);

        getDataFromServer data = new getDataFromServer(context, AllUrls.ListenToChatLastMessage(ListData.get(position).getChat_id()));
//        try {
//            data.Data(new DataFromFirebaseOnGet() {
//                @Override
//                public void onSuccess(DataSnapshot dataSnapshot) {
//                    getDataFromServer data_inside = new getDataFromServer(context, AllUrls.GetMessageUrlFromMessageId(dataSnapshot.getValue().toString()));
//                    try {
//                        data_inside.Data(new DataFromFirebaseOnGet() {
//                            @Override
//                            public void onSuccess(DataSnapshot dataSnapshot) {
//                                for (DataSnapshot dss : dataSnapshot.getChildren()) {
//                                    MessageModel item = dss.getValue(MessageModel.class);
//                                    item.setMessage_id(dss.getKey());
//                                    ListData.get(position).setLastMessage(item);
//                                    holder.messageTime.setText(ListData.get(position).getLastMessage().getMessage_time());
//                                    holder.messageText.setText(ListData.get(position).getLastMessage().getMessage_text());
//                                }
//
//                            }
//
//                            @Override
//                            public void onCancel(DatabaseError databaseError) {
//
//                            }
//                        });
//                    } catch (Exception e) {
//
//                    }
//
//                }
//
//                @Override
//                public void onCancel(DatabaseError databaseError) {
//
//                }
//            });
//        } catch (Exception e) {
//
//        }


    }

    @Override
    public int getItemCount() {
        return ListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView userImage;
        TextView messageText,messageTime,userName;
        public ViewHolder(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            messageText = itemView.findViewById(R.id.messageText);
            messageTime=itemView.findViewById(R.id.messageTime);
            userName = itemView.findViewById(R.id.userName);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,MessagesActivity.class);
            intent.putExtra("chat_id",ListData.get(getAdapterPosition()).getChat_id());
            Bundle bundle = new Bundle();
            bundle.putSerializable("model",ListData.get(getAdapterPosition()));
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }
}
