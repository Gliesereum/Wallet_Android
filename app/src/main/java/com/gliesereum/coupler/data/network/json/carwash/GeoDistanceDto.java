package com.gliesereum.coupler.data.network.json.carwash;

import com.google.gson.annotations.SerializedName;

class GeoDistanceDto {

    @SerializedName("longitude")
    private Double longitude;

    @SerializedName("latitude")
    private Double latitude;

    @SerializedName("distanceMeters")
    private Integer distanceMeters;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(Integer distanceMeters) {
        this.distanceMeters = distanceMeters;
    }
}
