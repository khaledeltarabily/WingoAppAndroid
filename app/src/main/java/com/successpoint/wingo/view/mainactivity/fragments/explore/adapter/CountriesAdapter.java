package com.successpoint.wingo.view.mainactivity.fragments.explore.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.CountryModel;

import java.util.List;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.ViewHolder> {

    private static final String TAG = CountriesAdapter.class.getSimpleName();
    private Context mContext;
    private List<CountryModel> mList;
    private OnItemClickListener mListener;

    public CountriesAdapter(Context context, List<CountryModel> list, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mList = list;
        this.mListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.row_country, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CountryModel item = mList.get(position);

        //TODO setup views
        holder.setData(item, position);
        holder.itemView.setOnClickListener(v -> {
            mListener.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(CountryModel item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //TODO Bind views
        private ImageView ivImage;
        private TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);

        }

        public void setData(CountryModel item, int position) {
            SvgLoader.pluck()
                    .with((Activity) mContext)
                    .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                    .load(item.getFlag(), ivImage);

            tvName.setText(item.getName());
        }
    }
}