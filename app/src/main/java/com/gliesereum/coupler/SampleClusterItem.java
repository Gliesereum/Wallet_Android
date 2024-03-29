package com.gliesereum.coupler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import net.sharewire.googlemapsclustering.ClusterItem;

public class SampleClusterItem implements ClusterItem {
    private LatLng location;
    private String title;
    private String snippet;
    private boolean isWork;

    public SampleClusterItem(@NonNull LatLng location, String title, String snippet) {
        this.location = location;
        this.title = title;
        this.snippet = snippet;
    }

    @Override
    public double getLatitude() {
        return location.latitude;
    }

    @Override
    public double getLongitude() {
        return location.longitude;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    @Nullable
    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public boolean isWork() {
        return isWork;
    }

    public void setWork(boolean work) {
        isWork = work;
    }

}
