package com.gliesereum.karma.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.gliesereum.karma.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import net.sharewire.googlemapsclustering.ClusterItem;
import net.sharewire.googlemapsclustering.DefaultIconGenerator;

public class IconGenerator extends DefaultIconGenerator implements net.sharewire.googlemapsclustering.IconGenerator {

    private Context contextt;

    public IconGenerator(@NonNull Context context) {
        super(context);
        this.contextt = context;
    }


//    @NonNull
//    @Override
//    public BitmapDescriptor getClusterIcon(@NonNull Cluster cluster) {
//        Drawable background = ContextCompat.getDrawable(contextt, R.drawable.ic_assignment_black_24dp);
//        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
//        Drawable vectorDrawable = ContextCompat.getDrawable(contextt, R.drawable.ic_send_black_24dp);
//        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
//        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        background.draw(canvas);
//        vectorDrawable.draw(canvas);
//        return BitmapDescriptorFactory.fromBitmap(bitmap);
//    }

    @NonNull
    @Override
    public BitmapDescriptor getClusterItemIcon(@NonNull ClusterItem clusterItem) {
//        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_edit_black_24dp);
        Drawable background = ContextCompat.getDrawable(contextt, R.drawable.ic_pin);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
//        Drawable vectorDrawable = ContextCompat.getDrawable(contextt, R.drawable.ic_directions_car_black_24dp);
//        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
//        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
