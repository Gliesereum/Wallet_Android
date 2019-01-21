package com.gliesereum.karma;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gliesereum.karma.data.network.json.record.AllRecordResponse;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.ViewHolder> {

    private List<AllRecordResponse> allRecordList = new ArrayList<>();

    @NonNull
    @Override
    public RecordListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_item, parent, false);
        return new RecordListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordListAdapter.ViewHolder holder, int position) {
        holder.bind(allRecordList.get(position));
    }

    @Override
    public int getItemCount() {
        return allRecordList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView dataTextView;
        private TextView timeTextView;
        private TextView carWashAddressTextView;
        private TextView priceTextView;
        private TextView durationTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            dataTextView = itemView.findViewById(R.id.dataTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            carWashAddressTextView = itemView.findViewById(R.id.carWashAddressTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
        }

        public void bind(AllRecordResponse recordInfo) {
            dataTextView.setText("" + recordInfo.getBegin());
            timeTextView.setText("" + recordInfo.getBegin());
            priceTextView.setText(recordInfo.getPrice() + "грн");
            durationTextView.setText("" + (recordInfo.getFinish() - recordInfo.getBegin()));
            carWashAddressTextView.setText("carWashAddressTextView");
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public void setItems(List<AllRecordResponse> cars) {
        allRecordList.addAll(cars);
        notifyDataSetChanged();
    }
}
