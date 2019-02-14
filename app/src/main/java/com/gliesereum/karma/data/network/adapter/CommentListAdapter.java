package com.gliesereum.karma.data.network.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gliesereum.karma.R;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.carwash.CommentsItem;
import com.gliesereum.karma.util.ErrorHandler;
import com.gliesereum.karma.util.SmartRatingBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private List<CommentsItem> allCommentList = new ArrayList<>();
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private String TAG = "TAG";

    @NonNull
    @Override
    public CommentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list_item, parent, false);
        return new CommentListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentListAdapter.ViewHolder holder, int position) {
        holder.bind(allCommentList.get(position));
    }

    @Override
    public int getItemCount() {
        return allCommentList.size();
    }

    // Предоставляет прямую ссылку на каждый View-компонент
    // Используется для кэширования View-компонентов и последующего быстрого доступа к ним
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView nameTextView;
        private TextView dataTextView;
        private TextView commentTextView;
        private SmartRatingBar smartRatingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            dataTextView = itemView.findViewById(R.id.dataTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            smartRatingBar = itemView.findViewById(R.id.smart_rating_bar);
        }

        public void bind(CommentsItem commentsItem) {
            nameTextView.setText(commentsItem.getFirstName());
            dataTextView.setText(String.valueOf(commentsItem.getDateCreated()));
            commentTextView.setText(commentsItem.getText());
            smartRatingBar.setRatingNum(commentsItem.getRating());
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public void setItems(List<CommentsItem> commentsItems) {
        allCommentList.addAll(commentsItems);
        notifyDataSetChanged();
    }

}
