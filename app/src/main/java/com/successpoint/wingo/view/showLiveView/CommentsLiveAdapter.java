package com.successpoint.wingo.view.showLiveView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.CommentModel;
import com.successpoint.wingo.model.TopTrendingModel;
import com.successpoint.wingo.model.UserModelObject;

import java.util.List;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsLiveAdapter extends RecyclerView.Adapter<CommentsLiveAdapter.ViewHolder> {

    private static final String TAG = CommentsLiveAdapter.class.getSimpleName();
    private Context mContext;
    private List<CommentModel> mList;

    public CommentsLiveAdapter(Context context, List<CommentModel> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recycler_live_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        UserModelObject item = mList.get(position);

        //TODO setup views
        holder.setData(mList.get(position), position);
        holder.itemView.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(CommentModel item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //TODO Bind views
        private TextView level;
        private TextView user_name;
        private TextView text_comment;
        public ViewHolder(View itemView) {
            super(itemView);
            level = itemView.findViewById(R.id.user_level);
            user_name = itemView.findViewById(R.id.user_name);
            text_comment = itemView.findViewById(R.id.text);
        }

        public void setData(CommentModel item, int position) {

            level.setText(item.getUser_level());
            user_name.setText(item.getUser_name());
            text_comment.setText(item.getComment_text());
        }
    }
}