package com.gliesereum.coupler.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.coupler.R;
import com.gliesereum.coupler.data.network.json.record_new.ContentItem;
import com.gliesereum.coupler.ui.SingleRecordActivity;
import com.gliesereum.coupler.util.FastSave;
import com.gliesereum.coupler.util.Util;

import java.util.ArrayList;
import java.util.List;

import static com.gliesereum.coupler.util.Constants.CANCELED;
import static com.gliesereum.coupler.util.Constants.COMPLETED;
import static com.gliesereum.coupler.util.Constants.IN_PROCESS;
import static com.gliesereum.coupler.util.Constants.RECORD;
import static com.gliesereum.coupler.util.Constants.WAITING;

//import com.gliesereum.coupler.GlideApp;

//import com.appizona.yehiahd.fastsave.FastSave;

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.ViewHolder> {

    private List<ContentItem> allRecordList = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public RecordListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_item, parent, false);
        view.setOnClickListener(v -> {
            ContentItem allRecordResponse = new ContentItem();
            allRecordResponse.setId(((TextView) v.findViewById(R.id.recordId)).getText().toString());
            FastSave.getInstance().saveObject(RECORD, allRecordList.get(allRecordList.indexOf(allRecordResponse)));
            Intent intent = new Intent(context, SingleRecordActivity.class);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dataTextView;
        private TextView timeTextView;
        private TextView priceTextView;
        private TextView carWashName;
        private TextView recordId;
        private TextView statusLabel;
        private ImageView carWashLogo;


        public ViewHolder(View itemView) {
            super(itemView);
            dataTextView = itemView.findViewById(R.id.dataTextView);
            recordId = itemView.findViewById(R.id.recordId);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            carWashName = itemView.findViewById(R.id.carWashName);
            carWashLogo = itemView.findViewById(R.id.carWashLogo);
            statusLabel = itemView.findViewById(R.id.statusLabel);
        }

        public void bind(ContentItem recordInfo) {
            dataTextView.setText(Util.getStringDate(recordInfo.getBegin()));
            timeTextView.setText(Util.getStringTime(recordInfo.getBegin()));
            if (recordInfo.getStatusRecord().equals(CANCELED)) {
                statusLabel.setText("Отменена");
                statusLabel.setTextColor(context.getResources().getColor(R.color.md_red_A200));
            } else {
                switch (recordInfo.getStatusProcess()) {
                    case WAITING:
                        statusLabel.setText("В ожидании");
                        statusLabel.setTextColor(context.getResources().getColor(R.color.material_drawer_selected));
                        break;
                    case IN_PROCESS:
                        statusLabel.setText("В процессе");
                        statusLabel.setTextColor(context.getResources().getColor(R.color.accent));
                        break;
                    case COMPLETED:
                        statusLabel.setText("Завершена");
                        statusLabel.setTextColor(context.getResources().getColor(R.color.md_green_300));
                        break;
                    default:
                        statusLabel.setText("");
                        break;
                }
            }
            priceTextView.setText(recordInfo.getPrice() + "грн");
            if (recordInfo.getBusiness() != null && recordInfo.getBusiness().getName() != null) {
                carWashName.setText(recordInfo.getBusiness().getName());
            }
            recordId.setText(recordInfo.getId());
//            Picasso.get().load(R.mipmap.ic_launcher_round).into(carWashLogo);
        }
    }

    public void setItems(List<ContentItem> record) {
        allRecordList.addAll(record);
        notifyDataSetChanged();
    }

    public RecordListAdapter() {
    }

    public RecordListAdapter(Context context) {
        this.context = context;
    }
}
