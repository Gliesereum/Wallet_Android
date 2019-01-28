package com.gliesereum.karma;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gliesereum.karma.data.network.json.record.AllRecordResponse;
import com.gliesereum.karma.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.ViewHolder> {

    private List<AllRecordResponse> allRecordList = new ArrayList<>();
    private Map<String, String> carWashNameMap = new HashMap<>();
    private Context context;

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
        private TextView priceTextView;
        private TextView carWashName;
        private ImageView carWashLogo;


        public ViewHolder(View itemView) {
            super(itemView);
            dataTextView = itemView.findViewById(R.id.dataTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            carWashName = itemView.findViewById(R.id.carWashName);
            carWashLogo = itemView.findViewById(R.id.carWashLogo);
        }

        public void bind(AllRecordResponse recordInfo) {
            dataTextView.setText(Util.getStringDate(recordInfo.getBegin()));
            timeTextView.setText(Util.getStringTime(recordInfo.getBegin()));
            priceTextView.setText(recordInfo.getPrice() + "грн");
//            if (recordInfo.getCarWashName()!=null){
            carWashName.setText(carWashNameMap.get(recordInfo.getCarWashId()));
//            }else {
//                carWashName.setText("Загрузка...");
//            }
            GlideApp.with(context).load(R.drawable.header).circleCrop().into(carWashLogo);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public void setItems(List<AllRecordResponse> cars, Map<String, String> carWashNameMap) {
        allRecordList.addAll(cars);
        this.carWashNameMap = carWashNameMap;
        notifyDataSetChanged();
    }

    public RecordListAdapter() {
    }

    public RecordListAdapter(Context context) {
        this.context = context;
    }
}
