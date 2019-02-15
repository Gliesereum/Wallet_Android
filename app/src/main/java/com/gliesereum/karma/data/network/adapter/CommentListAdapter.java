package com.gliesereum.karma.data.network.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.CarWashActivity;
import com.gliesereum.karma.R;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.car.CarDeleteResponse;
import com.gliesereum.karma.data.network.json.carwash.CommentsItem;
import com.gliesereum.karma.util.ErrorHandler;
import com.gliesereum.karma.util.SmartRatingBar;
import com.gliesereum.karma.util.Util;
import com.gohn.nativedialog.ButtonType;
import com.gohn.nativedialog.NDialog;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.willy.ratingbar.ScaleRatingBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private List<CommentsItem> allCommentList = new ArrayList<>();
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private String TAG = "TAG";
    private boolean myComment;
    private PowerMenu editCommentMenu;
    private CarWashActivity activity;
    private Context context;
    private ScaleRatingBar scaleRatingBar;
    private ProgressDialog progressDialog;

    public CommentListAdapter(boolean myComment, CarWashActivity activity) {
        this.myComment = myComment;
        this.activity = activity;
        this.context = activity.getContext();
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
                                    case R.id.okBtn:
                                        Button okBtn = childView.findViewById(R.id.okBtn);
                                        okBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                showProgressDialog();
                                                apiInterface = APIClient.getClient().create(APIInterface.class);
                                                Call<CommentsItem> call = apiInterface.editComment(FastSave.getInstance().getString(ACCESS_TOKEN, ""), new CommentsItem((int) scaleRatingBar.getRating(), commentTextView.getText().toString(), commentsItem.getId()));
                                                call.enqueue(new Callback<CommentsItem>() {
                                                    @Override
                                                    public void onResponse(Call<CommentsItem> call, Response<CommentsItem> response) {
                                                        if (response.code() == 200) {
                                                            closeProgressDialog();
                                                            commentDialog.dismiss();
                                                            activity.setCommentList(activity.getCarWashObjact());
                                                        } else {
                                                            try {
                                                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                                                errorHandler.showError(jObjError.getInt("code"));
                                                                closeProgressDialog();
                                                                commentDialog.dismiss();
                                                            } catch (Exception e) {
                                                                errorHandler.showCustomError(e.getMessage());
                                                                closeProgressDialog();
                                                                commentDialog.dismiss();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<CommentsItem> call, Throwable t) {
                                                        errorHandler.showCustomError(t.getMessage());
                                                        closeProgressDialog();
                                                        commentDialog.dismiss();
                                                    }
                                                });
                                            }
                                        });
                                        break;
                                    case R.id.backBtn:
                                        Button backBtn = childView.findViewById(R.id.backBtn);
                                        backBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                commentDialog.dismiss();
                                            }
                                        });
                                        break;
                                }
                            }
                            commentDialog.show();
                            editCommentMenu.onDestroy();
                            break;
                        case 1:
                            editCommentMenu.onDestroy();
                            showProgressDialog();
                            apiInterface = APIClient.getClient().create(APIInterface.class);
                            Call<CarDeleteResponse> call = apiInterface.deleteComment(FastSave.getInstance().getString(ACCESS_TOKEN, ""), commentsItem.getId());
                            call.enqueue(new Callback<CarDeleteResponse>() {
                                @Override
                                public void onResponse(Call<CarDeleteResponse> call, Response<CarDeleteResponse> response) {
                                    if (response.code() == 200) {
                                        closeProgressDialog();
                                        activity.setCommentList(activity.getCarWashObjact());
                                    } else {
                                        try {
                                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                                            errorHandler.showError(jObjError.getInt("code"));
                                            closeProgressDialog();
                                        } catch (Exception e) {
                                            errorHandler.showCustomError(e.getMessage());
                                            closeProgressDialog();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<CarDeleteResponse> call, Throwable t) {
                                    errorHandler.showCustomError(t.getMessage());
                                    closeProgressDialog();
                                }
                            });

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

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(context, "Ща сек...", "Ща все сделаю...");

    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
