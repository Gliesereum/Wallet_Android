package com.gliesereum.karma;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.data.network.json.record.AllRecordResponse;
import com.gliesereum.karma.util.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.ViewHolder> {

    private List<AllRecordResponse> allRecordList = new ArrayList<>();
    //    private Map<String, String> carWashNameMap = new HashMap<>();
    private Context context;
    private int i = 0;

    @NonNull
    @Override
    public RecordListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_item, parent, false);
        view.setOnClickListener(v -> {
            FastSave.getInstance().saveObject("RECORD", allRecordList.get(Integer.parseInt(((TextView) v.findViewById(R.id.recordId)).getText().toString())));
//            Toast.makeText(context, ((TextView) v.findViewById(R.id.recordId)).getText().toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, SingleRecordActivity.class);
//            intent.putExtra("recordId", ((TextView) v.findViewById(R.id.recordId)).getText().toString());
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
        });
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
        private TextView recordId;
        private ImageView carWashLogo;


        public ViewHolder(View itemView) {
            super(itemView);
            dataTextView = itemView.findViewById(R.id.dataTextView);
            recordId = itemView.findViewById(R.id.recordId);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            carWashName = itemView.findViewById(R.id.carWashName);
            carWashLogo = itemView.findViewById(R.id.carWashLogo);
        }

        public void bind(AllRecordResponse recordInfo) {
            dataTextView.setText(Util.getStringDate(recordInfo.getBegin()));
            timeTextView.setText(Util.getStringTime(recordInfo.getBegin()));
            priceTextView.setText(recordInfo.getPrice() + "грн");
            if (recordInfo.getBusiness().getName() != null) {
                carWashName.setText(recordInfo.getBusiness().getName());
            }
            recordId.setText(String.valueOf(i));
            GlideApp.with(context).load(R.mipmap.ic_launcher_round).circleCrop().into(carWashLogo);
            i++;
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public void setItems(List<AllRecordResponse> cars) {
        allRecordList.addAll(cars);
//        this.carWashNameMap = carWashNameMap;
        notifyDataSetChanged();
    }

    public RecordListAdapter() {
    }

    public RecordListAdapter(Context context) {
        this.context = context;
    }
}
