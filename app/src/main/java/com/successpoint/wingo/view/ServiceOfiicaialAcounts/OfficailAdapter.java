package com.successpoint.wingo.view.ServiceOfiicaialAcounts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.OfficaialAcounts;
import com.successpoint.wingo.model.TopTrendingModel;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class OfficailAdapter extends RecyclerView.Adapter<OfficailAdapter.ViewHolder> {

    private static final String TAG = OfficailAdapter.class.getSimpleName();
    private Context mContext;
    private List<OfficaialAcounts> mList;

    public OfficailAdapter(Context context, List<OfficaialAcounts> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.row_offically, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OfficaialAcounts item = mList.get(position);
        //TODO setup views
        holder.setData(item, position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(TopTrendingModel.Content item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //TODO Bind views
        private CircleImageView ivUser;
        private TextView level;
        private TextView tvUsername;
        public ViewHolder(View itemView) {
            super(itemView);
            ivUser = itemView.findViewById(R.id.ivUser);
            level = itemView.findViewById(R.id.level);
            tvUsername = itemView.findViewById(R.id.tvUsername);
        }

        public void setData(OfficaialAcounts item, int position) {
            Glide.with(mContext)
                    .load(item.getImage())
                    .fallback(R.drawable.profiler)
                    .error(R.drawable.profiler)
                    .into(ivUser);
            tvUsername.setText(item.getName());
            level.setText(item.getLevel()+"");
        }
    }
}