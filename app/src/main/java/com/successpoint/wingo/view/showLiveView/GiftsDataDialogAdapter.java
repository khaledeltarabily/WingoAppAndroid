package com.successpoint.wingo.view.showLiveView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.successpoint.wingo.App;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.GiftModel;
import com.successpoint.wingo.model.TopTrendingModel;
import com.successpoint.wingo.model.UserModelObject;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class GiftsDataDialogAdapter extends RecyclerView.Adapter<GiftsDataDialogAdapter.ViewHolder> {

    private static final String TAG = GiftsDataDialogAdapter.class.getSimpleName();
    private Context mContext;
    private List<GiftModel> mList;
    private OnItemClickListener mListener;
    int position;
    public GiftsDataDialogAdapter(Context context, List<GiftModel> list, OnItemClickListener onItemClickListener,int position) {
        this.mContext = context;
        this.position = position;
        this.mList = list;
        this.mListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.gift_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GiftModel item = mList.get(position);

        //TODO setup views
        holder.setData(item, position);
        holder.itemView.setOnClickListener(v -> {
            App.position_is_be = position;
            mListener.onItemClick(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //TODO Bind views
        private TextView gift_name , gift_diamonds;
        private ImageView gift_img;
        private LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            gift_name = itemView.findViewById(R.id.gift_name);
            gift_diamonds = itemView.findViewById(R.id.gift_diamonds);
            gift_img = itemView.findViewById(R.id.gift_img);
            container = itemView.findViewById(R.id.container);
        }

        public void setData(GiftModel item, int position) {
            gift_diamonds.setText(item.getDiamonds()+"");
            gift_name.setText(item.getGiftName()+"");
            Glide.with(mContext).load(item.getImage_url()).into(gift_img);
            if (App.position_is_be == position){
                container.setBackground(mContext.getResources().getDrawable(R.drawable.gift_background));
            }
            else {
                container.setBackground(null);
            }
        }
    }
}