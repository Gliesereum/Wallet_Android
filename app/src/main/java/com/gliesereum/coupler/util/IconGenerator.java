package com.gliesereum.coupler.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.gliesereum.coupler.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import net.sharewire.googlemapsclustering.ClusterItem;
import net.sharewire.googlemapsclustering.DefaultIconGenerator;

import static com.gliesereum.coupler.util.Constants.BUSINESS_CODE;
import static com.gliesereum.coupler.util.Constants.SERVICE_CAR_SERVICE;
import static com.gliesereum.coupler.util.Constants.SERVICE_CAR_WASH;
import static com.gliesereum.coupler.util.Constants.SERVICE_TIRE_FITTING;

public class IconGenerator extends DefaultIconGenerator implements net.sharewire.googlemapsclustering.IconGenerator {

    private Context context;
    private String businessCode;

    public IconGenerator(@NonNull Context context, String businessCode) {
        super(context);
        this.context = context;
        this.businessCode = businessCode;
        FastSave.init(context);
    }

    @NonNull
    @Override
    public BitmapDescriptor getClusterItemIcon(@NonNull ClusterItem clusterItem) {
        Drawable background;
        Log.e("TAG", "getClusterItemIcon: " + FastSave.getInstance().getString(BUSINESS_CODE, ""));
        switch (FastSave.getInstance().getString(BUSINESS_CODE, "")) {
            case SERVICE_CAR_WASH:
                background = ContextCompat.getDrawable(context, R.drawable.ic_pin_car_wash);
                break;
            case SERVICE_TIRE_FITTING:
                background = ContextCompat.getDrawable(context, R.drawable.ic_pin_tire_fitting);
                break;
            case SERVICE_CAR_SERVICE:
                background = ContextCompat.getDrawable(context, R.drawable.ic_pin_car_service);
                break;
            default:
                background = ContextCompat.getDrawable(context, R.drawable.ic_pin_servicess);
                break;
        }
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
