package com.gliesereum.coupler.data.network.json.carwash;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class AddPointBody {

    @SerializedName("name")
    private String name;

    @SerializedName("businessCategoryId")
    private String businessCategoryId;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("phone")
    private String phone;

    @SerializedName("longitude")
    private double longitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessCategoryId() {
        return businessCategoryId;
    }

    public void setBusinessCategoryId(String businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}