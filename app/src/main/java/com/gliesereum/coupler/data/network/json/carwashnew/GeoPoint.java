package com.gliesereum.coupler.data.network.json.carwashnew;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class GeoPoint {

    @SerializedName("lon")
    private double lon;

    @SerializedName("lat")
    private double lat;

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLon() {
        return lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }
}