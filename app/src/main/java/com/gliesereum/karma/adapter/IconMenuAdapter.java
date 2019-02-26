package com.gliesereum.karma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gliesereum.karma.IconPowerMenuItem;
import com.gliesereum.karma.R;
import com.skydoves.powermenu.MenuBaseAdapter;

public class IconMenuAdapter extends MenuBaseAdapter<IconPowerMenuItem> {

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_icon_menu, viewGroup, false);
        }

        IconPowerMenuItem item = (IconPowerMenuItem) getItem(index);
        TextView titlteTextView = view.findViewById(R.id.titlteTextView);

        titlteTextView.setText(item.getTitle());

//        if (progressBar.getProgress() >= 0 && progressBar.getProgress() < 10) {
//            progressBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.c0_10)));
//        }
//        if (progressBar.getProgress() >= 10 && progressBar.getProgress() < 20) {
//            progressBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.c10_20)));
//        }
//        if (progressBar.getProgress() >= 20 && progressBar.getProgress() < 30) {
//            progressBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.c20_30)));
//        }
//        if (progressBar.getProgress() >= 30 && progressBar.getProgress() < 40) {
//            progressBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.c30_40)));
//        }
//        if (progressBar.getProgress() >= 40 && progressBar.getProgress() < 50) {
//            progressBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.c40_50)));
//        }
//        if (progressBar.getProgress() >= 50 && progressBar.getProgress() < 60) {
//            progressBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.c50_60)));
//        }
//        if (progressBar.getProgress() >= 60 && progressBar.getProgress() < 70) {
//            progressBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.c60_70)));
//        }
//        if (progressBar.getProgress() >= 70 && progressBar.getProgress() < 80) {
//            progressBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.c70_80)));
//        }
//        if (progressBar.getProgress() >= 80 && progressBar.getProgress() < 90) {
//            progressBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.c80_90)));
//        }
//        if (progressBar.getProgress() >= 90 && progressBar.getProgress() <= 100) {
//            progressBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.c90_100)));
//        }

        return super.getView(index, view, viewGroup);
    }

}
