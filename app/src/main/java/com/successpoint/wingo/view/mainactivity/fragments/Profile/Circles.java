package com.successpoint.wingo.view.mainactivity.fragments.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.successpoint.wingo.R;

import androidx.recyclerview.widget.RecyclerView;
import com.successpoint.wingo.R;

public class Circles extends RecyclerView.Adapter<Circles.ViewHolder> {
    Context context;
    int positionAll;
    int size;
    public Circles(Context context, int positionAll, int size) {
        this.context = context;
        this.positionAll=positionAll;
        this.size=size;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_circles ,parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (positionAll==position)
            holder.image.setImageResource(0);
        else {
            holder.image.setImageResource(0);
        }
    }

    @Override
    public int getItemCount() {
        return size;
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