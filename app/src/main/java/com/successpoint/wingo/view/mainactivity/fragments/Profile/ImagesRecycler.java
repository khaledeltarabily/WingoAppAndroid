package com.successpoint.wingo.view.mainactivity.fragments.Profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.successpoint.wingo.R;


public class ImagesRecycler extends RecyclerView.Adapter<ImagesRecycler.ViewHolder> {
    Context context;
    List<String> images;
    public ImagesRecycler(Context context, List<String> images) {
        this.context = context;
        this.images=images;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_recycler ,parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(context).load(images.get(position)).into(holder.image);
    }

    @Override
    public int getItemCount() {
        Log.e("Done","Size"+images.size());
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);

        }

        @Override
        public void onClick(View v) {


        }
    }
}