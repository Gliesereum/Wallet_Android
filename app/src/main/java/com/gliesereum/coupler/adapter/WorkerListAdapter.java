package com.gliesereum.coupler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.coupler.R;
import com.gliesereum.coupler.data.network.json.worker_new.WorkerItem;
import com.gliesereum.coupler.util.CircleTransform;
import com.gliesereum.coupler.util.SmartRatingBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WorkerListAdapter extends RecyclerView.Adapter<WorkerListAdapter.ViewHolder> {

    private List<WorkerItem> allWorkerList = new ArrayList<>();
    //    private Map<String, String> carWashNameMap = new HashMap<>();
    private Context context;
    private int i = 0;
    private ItemClickListener mClickListener;

    @NonNull
    @Override
    public WorkerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.worker_item, parent, false);
        return new WorkerListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerListAdapter.ViewHolder holder, int position) {
        holder.bind(allWorkerList.get(position));
    }

    @Override
    public int getItemCount() {
        return allWorkerList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView masterNameTextView;
        private TextView masterSecondNameTextView;
        private TextView masterPositionTextView;
        private SmartRatingBar masterRating;
        private ImageView masterAvatarImg;


        public ViewHolder(View itemView) {
            super(itemView);
            masterNameTextView = itemView.findViewById(R.id.masterNameTextView);
            masterSecondNameTextView = itemView.findViewById(R.id.masterSecondNameTextView);
            masterPositionTextView = itemView.findViewById(R.id.masterPositionTextView);
            masterRating = itemView.findViewById(R.id.masterRating);
            masterAvatarImg = itemView.findViewById(R.id.masterAvatarImg);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        public void bind(WorkerItem workerItem) {
            if (workerItem == null) {
                masterNameTextView.setText("Любой мастер");
                masterPositionTextView.setVisibility(View.INVISIBLE);
                masterRating.setVisibility(View.INVISIBLE);
                masterAvatarImg.setVisibility(View.GONE);
            } else {
                masterNameTextView.setText(workerItem.getUser().getFirstName());
                masterSecondNameTextView.setText(workerItem.getUser().getMiddleName());
                masterPositionTextView.setText(workerItem.getPosition());
                masterRating.setRatingNum((float) workerItem.getRating().getRating());
                if (workerItem.getUser().getAvatarUrl() != null) {
                    Picasso.get().load(workerItem.getUser().getAvatarUrl()).transform(new CircleTransform()).into(masterAvatarImg);
                }
            }
        }
    }

    public void setItems(List<WorkerItem> workers) {
        allWorkerList.addAll(workers);
        notifyDataSetChanged();
    }

    public void clearItems() {
        allWorkerList.clear();
        notifyDataSetChanged();
    }

    public WorkerListAdapter() {
    }

    public WorkerListAdapter(Context context) {
        this.context = context;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public WorkerItem getItem(int id) {
        return allWorkerList.get(id);
    }
}
