package com.gliesereum.karma.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.R;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.CustomCallback;
import com.gliesereum.karma.data.network.json.car.CarDeleteResponse;
import com.gliesereum.karma.data.network.json.carwash.CommentsItem;
import com.gliesereum.karma.ui.CarWashActivity;
import com.gliesereum.karma.util.SmartRatingBar;
import com.gliesereum.karma.util.Util;
import com.gohn.nativedialog.ButtonType;
import com.gohn.nativedialog.NDialog;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private List<CommentsItem> allCommentList = new ArrayList<>();
    private APIInterface API;
    private CustomCallback customCallback;
    private String TAG = "TAG";
    private boolean myComment;
    private PowerMenu editCommentMenu;
    private CarWashActivity activity;
    private Context context;
    private ScaleRatingBar scaleRatingBar;

    public CommentListAdapter(boolean myComment, CarWashActivity activity) {
        this.myComment = myComment;
        this.activity = activity;
        this.context = activity.getContext();
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(activity.getContext(), activity);
    }

    @NonNull
    @Override
    public CommentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list_item, parent, false);
        return new CommentListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentListAdapter.ViewHolder holder, int position) {
        if (position == 0 && myComment) {
            holder.bindFirst(allCommentList.get(position));
        } else {
            holder.bind(allCommentList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return allCommentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView nameTextView;
        private TextView dataTextView;
        private TextView commentTextView;
        private TextView idTextView;
        private SmartRatingBar smartRatingBar;
        private ImageView editComment;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            dataTextView = itemView.findViewById(R.id.dataTextView);
            idTextView = itemView.findViewById(R.id.idTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            smartRatingBar = itemView.findViewById(R.id.smart_rating_bar);
            editComment = itemView.findViewById(R.id.editComment);
            editComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editCommentMenu.showAsDropDown(editComment);
                }
            });
        }

        public void bindFirst(CommentsItem commentsItem) {
            nameTextView.setText(commentsItem.getFirstName());
            dataTextView.setText(Util.getStringDateTrue(commentsItem.getDateCreated()));
            commentTextView.setText(commentsItem.getText());
            smartRatingBar.setRatingNum(commentsItem.getRating());
            editComment.setVisibility(View.VISIBLE);
            editCommentMenu = new PowerMenu.Builder(context)
                    .addItem(new PowerMenuItem("Редактировать", false))
                    .addItem(new PowerMenuItem("Удалить", false))
                    .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT)
                    .setMenuRadius(10f)
                    .setMenuShadow(10f)
                    .setWidth(600) // set popup width size
//                .setTextColor(context.getResources().getColor(R.color.md_grey_800))
                    .setSelectedTextColor(Color.WHITE)
                    .setMenuColor(Color.WHITE)
                    .build();
            editCommentMenu.setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                @Override
                public void onItemClick(int position, PowerMenuItem item) {
                    switch (position) {
                        case 0:
                            editCommentMenu.onDestroy();
                            NDialog commentDialog = new NDialog(context, ButtonType.NO_BUTTON);
                            commentDialog.isCancelable(false);
                            commentDialog.setCustomView(R.layout.comment_dialog);
                            List<View> childViews = commentDialog.getCustomViewChildren();
                            for (View childView : childViews) {
                                switch (childView.getId()) {
                                    case R.id.commentTextView:
                                        commentTextView = childView.findViewById(R.id.commentTextView);
                                        commentTextView.setText(commentsItem.getText());
                                        break;
                                    case R.id.simpleRatingBar:
                                        scaleRatingBar = childView.findViewById(R.id.simpleRatingBar);
                                        scaleRatingBar.setRating(commentsItem.getRating());
                                        break;
                                    case R.id.sendCommentBtn:
                                        Button sendCommentBtn = childView.findViewById(R.id.sendCommentBtn);
                                        sendCommentBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                API.editComment(FastSave.getInstance().getString(ACCESS_TOKEN, ""), new CommentsItem((int) scaleRatingBar.getRating(), commentTextView.getText().toString(), commentsItem.getId()))
                                                        .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<CommentsItem>() {
                                                            @Override
                                                            public void onSuccessful(Call<CommentsItem> call, Response<CommentsItem> response) {
                                                                commentDialog.dismiss();
                                                                activity.setCommentList();
                                                            }

                                                            @Override
                                                            public void onEmpty(Call<CommentsItem> call, Response<CommentsItem> response) {

                                                            }
                                                        }));
                                            }
                                        });
                                        break;
                                    case R.id.cancelBtn:
                                        Button backBtn = childView.findViewById(R.id.nowOrderBtn);
                                        backBtn.setOnClickListener(v -> commentDialog.dismiss());
                                        break;
                                }
                            }
                            commentDialog.show();
                            break;
                        case 1:
                            editCommentMenu.onDestroy();
                            API.deleteComment(FastSave.getInstance().getString(ACCESS_TOKEN, ""), commentsItem.getId())
                                    .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<CarDeleteResponse>() {
                                        @Override
                                        public void onSuccessful(Call<CarDeleteResponse> call, Response<CarDeleteResponse> response) {
                                            activity.setCommentList();
                                        }

                                        @Override
                                        public void onEmpty(Call<CarDeleteResponse> call, Response<CarDeleteResponse> response) {

                                        }
                                    }));
                            break;
                    }
                }
            });
        }

        public void bind(CommentsItem commentsItem) {
            nameTextView.setText(commentsItem.getFirstName());
            dataTextView.setText(Util.getStringDateTrue(commentsItem.getDateCreated()));
            commentTextView.setText(commentsItem.getText());
            smartRatingBar.setRatingNum(commentsItem.getRating());
            editComment.setVisibility(View.GONE);
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
