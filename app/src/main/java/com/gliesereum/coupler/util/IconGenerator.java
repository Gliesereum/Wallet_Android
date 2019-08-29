package com.gliesereum.coupler.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.gliesereum.coupler.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import net.sharewire.googlemapsclustering.ClusterItem;
import net.sharewire.googlemapsclustering.DefaultIconGenerator;

import static com.gliesereum.coupler.util.Constants.BUSINESS_CODE;
import static com.gliesereum.coupler.util.Constants.CODE_BEAUTY_SALONS;
import static com.gliesereum.coupler.util.Constants.CODE_CAR_SERVICE;
import static com.gliesereum.coupler.util.Constants.CODE_CAR_WASH;
import static com.gliesereum.coupler.util.Constants.CODE_DEVELOPMENT;
import static com.gliesereum.coupler.util.Constants.CODE_MARKETING;
import static com.gliesereum.coupler.util.Constants.CODE_TIRE_FITTING;

public class IconGenerator extends DefaultIconGenerator implements net.sharewire.googlemapsclustering.IconGenerator {

    private Context context;

    public IconGenerator(@NonNull Context context) {
        super(context);
        this.context = context;
        FastSave.init(context);
    }

    @NonNull
    @Override
    public BitmapDescriptor getClusterItemIcon(@NonNull ClusterItem clusterItem) {
        Drawable background;
        switch (FastSave.getInstance().getString(BUSINESS_CODE, "")) {
            case CODE_BEAUTY_SALONS:
                background = ContextCompat.getDrawable(context, R.drawable.ic_pin_beauty);
                break;
            case CODE_CAR_WASH:
                background = ContextCompat.getDrawable(context, R.drawable.ic_pin_carwash);
                break;
            case CODE_TIRE_FITTING:
                background = ContextCompat.getDrawable(context, R.drawable.ic_pin_tires);
                break;
            case CODE_CAR_SERVICE:
                background = ContextCompat.getDrawable(context, R.drawable.ic_pin_sto);
                break;
            case CODE_MARKETING:
                background = ContextCompat.getDrawable(context, R.drawable.ic_pin_consulting);
                break;
            case CODE_DEVELOPMENT:
                background = ContextCompat.getDrawable(context, R.drawable.ic_pin_freelance);
                break;
            default:
                background = ContextCompat.getDrawable(context, R.drawable.ic_pin_others);
                break;
        }
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
