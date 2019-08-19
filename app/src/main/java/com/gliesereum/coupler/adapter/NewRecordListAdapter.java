package com.gliesereum.coupler.adapter;

import android.content.Context;

import androidx.databinding.ViewDataBinding;

import com.gliesereum.coupler.R;
import com.gliesereum.coupler.adapter.customadapterrecycleview.AdapterRecyclerView;
import com.gliesereum.coupler.adapter.customadapterrecycleview.viewHolder.ItemViewHolder;
import com.gliesereum.coupler.data.network.json.record_new.RecordItem;
import com.gliesereum.coupler.databinding.RecordItemBinding;
import com.gliesereum.coupler.util.Util;
import com.squareup.picasso.Picasso;

import static com.gliesereum.coupler.util.Constants.CANCELED;
import static com.gliesereum.coupler.util.Constants.COMPLETED;
import static com.gliesereum.coupler.util.Constants.IN_PROCESS;
import static com.gliesereum.coupler.util.Constants.WAITING;


public class NewRecordListAdapter extends AdapterRecyclerView<RecordItem> {

    private Context context;

    @Override
    public int getItemLayout(int viewType) {
        return R.layout.record_item;
    }

    @Override
    public int onProgressLayout() {
        return R.layout.my_custom_progress_item;
    }

    @Override
    public void onBindView(ViewDataBinding binding, ItemViewHolder viewDataBinding, int position, int viewType, RecordItem element) {

        RecordItemBinding itemBinding = (RecordItemBinding) binding;
        itemBinding.dataTextView.setText(Util.getStringDate(element.getBegin()));
        itemBinding.timeTextView.setText(Util.getStringTime(element.getBegin()));
        if (element.getStatusRecord().equals(CANCELED)) {
            itemBinding.statusLabel.setText("Отменена");
            itemBinding.statusLabel.setTextColor(context.getResources().getColor(R.color.md_red_A200));
        } else {
            switch (element.getStatusProcess()) {
                case WAITING:
                    itemBinding.statusLabel.setText("В ожидании");
                    itemBinding.statusLabel.setTextColor(context.getResources().getColor(R.color.material_drawer_selected));
                    break;
                case IN_PROCESS:
                    itemBinding.statusLabel.setText("В процессе");
                    itemBinding.statusLabel.setTextColor(context.getResources().getColor(R.color.accent));
                    break;
                case COMPLETED:
                    itemBinding.statusLabel.setText("Завершена");
                    itemBinding.statusLabel.setTextColor(context.getResources().getColor(R.color.md_green_300));
                    break;
                default:
                    itemBinding.statusLabel.setText("");
                    break;
            }
        }
        itemBinding.priceTextView.setText(element.getPrice() + "грн");
        if (element.getBusiness() != null && element.getBusiness().getName() != null) {
            itemBinding.carWashName.setText(element.getBusiness().getName());
        }
        itemBinding.recordId.setText(element.getId());
        if (element.getBusiness().getLogoUrl() != null) {
            Picasso.get().load(element.getBusiness().getLogoUrl()).into(itemBinding.carWashLogo);
        }

    }


//    private String getStringDate(Long millisecond) {
//        SimpleDateFormat format = new SimpleDateFormat("dd.MM");
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(millisecond);
//        return format.format(calendar.getTime());
//
//    }

    public NewRecordListAdapter(Context context) {
        this.context = context;
    }


}
