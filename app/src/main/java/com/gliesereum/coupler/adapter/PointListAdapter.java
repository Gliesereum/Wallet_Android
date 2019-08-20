package com.gliesereum.coupler.adapter;

import android.content.Context;
import android.util.Log;

import androidx.databinding.ViewDataBinding;

import com.gliesereum.coupler.R;
import com.gliesereum.coupler.adapter.customadapterrecycleview.AdapterRecyclerView;
import com.gliesereum.coupler.adapter.customadapterrecycleview.viewHolder.ItemViewHolder;
import com.gliesereum.coupler.data.network.json.carwashnew.CarWashResponse;
import com.gliesereum.coupler.databinding.PointItemBinding;
import com.squareup.picasso.Picasso;


public class PointListAdapter extends AdapterRecyclerView<CarWashResponse> {

    private Context context;

    @Override
    public int getItemLayout(int viewType) {
        return R.layout.point_item;
    }

    @Override
    public int onProgressLayout() {
        return R.layout.my_custom_progress_item;
    }

    @Override
    public void onBindView(ViewDataBinding binding, ItemViewHolder viewDataBinding, int position, int viewType, CarWashResponse element) {
        PointItemBinding itemBinding = (PointItemBinding) binding;
        if (element.getLogoUrl() != null) {
            Picasso.get().load(element.getLogoUrl()).into(itemBinding.businessLogo);
        }
        itemBinding.carWashName.setText(element.getName());
        itemBinding.carWashRating.setRatingNum(element.getRating());
        itemBinding.ratingCountTextView.setText("(" + element.getRatingCount() + ")");
        Log.d("TAG", "onBindView: ");
    }

    public PointListAdapter(Context context) {
        this.context = context;
    }


}
