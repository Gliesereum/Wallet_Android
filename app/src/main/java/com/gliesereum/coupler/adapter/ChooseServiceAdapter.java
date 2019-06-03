package com.gliesereum.coupler.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.coupler.GlideApp;
import com.gliesereum.coupler.R;
import com.gliesereum.coupler.data.network.json.businesscategory.BusinesCategoryResponse;
import com.gliesereum.coupler.ui.MapsActivity;
import com.gliesereum.coupler.util.FastSave;

import java.util.ArrayList;
import java.util.List;

import static com.gliesereum.coupler.util.Constants.BUSINESS_CATEGORY_ID;
import static com.gliesereum.coupler.util.Constants.BUSINESS_CATEGORY_NAME;
import static com.gliesereum.coupler.util.Constants.BUSINESS_CODE;
import static com.gliesereum.coupler.util.Constants.BUSINESS_TYPE;

public class ChooseServiceAdapter extends RecyclerView.Adapter<ChooseServiceAdapter.ViewHolder> {

    private List<BusinesCategoryResponse> businesCategoryList = new ArrayList<>();
    private Context context;


    @NonNull
    @Override
    public ChooseServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choose_service_item, parent, false);
        view.setOnClickListener(v -> {
//            FastSave.getInstance().saveObject("RECORD", allRecordList.get(Integer.parseInt(((TextView) v.findViewById(R.id.recordId)).getText().toString())));
//            AllRecordResponse allRecordResponse = new AllRecordResponse();
//            FastSave.getInstance().saveObject("RECORD", allRecordList.get(allRecordList.indexOf(allRecordResponse)));
            if (businesCategoryList.get(businesCategoryList.indexOf(new BusinesCategoryResponse(((TextView) v.findViewById(R.id.businessCategoryId)).getText().toString()))).getActive()) {
                FastSave.getInstance().saveString(BUSINESS_CODE, ((TextView) v.findViewById(R.id.businessCode)).getText().toString());
                FastSave.getInstance().saveString(BUSINESS_TYPE, ((TextView) v.findViewById(R.id.businessType)).getText().toString());
                FastSave.getInstance().saveString(BUSINESS_CATEGORY_ID, ((TextView) v.findViewById(R.id.businessCategoryId)).getText().toString());
                FastSave.getInstance().saveString(BUSINESS_CATEGORY_NAME, " (" + ((TextView) v.findViewById(R.id.businessCategoryName)).getText().toString() + ")");
                Intent intent = new Intent(context, MapsActivity.class);
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            } else {
                Toast.makeText(context, "Скоро будет доступно", Toast.LENGTH_SHORT).show();
            }

        });
        return new ChooseServiceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseServiceAdapter.ViewHolder holder, int position) {
        holder.bind(businesCategoryList.get(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView serviceImage;
        private TextView serviceName;
        private TextView businessCode;
        private TextView businessType;
        private TextView businessCategoryId;
        private TextView businessCategoryName;


        public ViewHolder(View itemView) {
            super(itemView);
            serviceImage = itemView.findViewById(R.id.serviceImage);
            serviceName = itemView.findViewById(R.id.serviceName);
            businessCode = itemView.findViewById(R.id.businessCode);
            businessType = itemView.findViewById(R.id.businessType);
            businessCategoryId = itemView.findViewById(R.id.businessCategoryId);
            businessCategoryName = itemView.findViewById(R.id.businessCategoryName);
        }

        public void bind(BusinesCategoryResponse businesCategoryResponse) {
            serviceName.setText(businesCategoryResponse.getName());
            businessCode.setText(businesCategoryResponse.getCode());
            businessType.setText(businesCategoryResponse.getBusinessType());
            businessCategoryId.setText(businesCategoryResponse.getId());
            businessCategoryName.setText(businesCategoryResponse.getName());
            if (businesCategoryResponse.getActive()) {
                serviceName.setTextColor(Color.parseColor("#282828"));
            } else {
                serviceName.setTextColor(Color.parseColor("#BABABA"));
            }
            if (businesCategoryResponse.getImageUrl() != null) {
                GlideApp.with(context).load(businesCategoryResponse.getImageUrl()).into(serviceImage);
            } else {
                GlideApp.with(context).load(R.mipmap.ic_launcher_round).circleCrop().into(serviceImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        return businesCategoryList.size();
    }

    public void setItems(List<BusinesCategoryResponse> businessCategory) {
        businesCategoryList.addAll(businessCategory);
        notifyDataSetChanged();
    }

    public ChooseServiceAdapter() {
    }

    public ChooseServiceAdapter(Context context) {
        this.context = context;
        FastSave.init(context);
    }
}
