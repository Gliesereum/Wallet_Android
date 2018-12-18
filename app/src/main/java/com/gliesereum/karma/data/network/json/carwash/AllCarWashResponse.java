package com.gliesereum.karma.data.network.json.carwash;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class AllCarWashResponse {

    @SerializedName("address")
    private String address;

    @SerializedName("workTimes")
    private List<WorkTimesItem> workTimes;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("userBusinessId")
    private String userBusinessId;

    @SerializedName("id")
    private String id;

    @SerializedName("countBox")
    private int countBox;

    @SerializedName("longitude")
    private double longitude;

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setWorkTimes(List<WorkTimesItem> workTimes) {
        this.workTimes = workTimes;
    }

    public List<WorkTimesItem> getWorkTimes() {
        return workTimes;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setUserBusinessId(String userBusinessId) {
        this.userBusinessId = userBusinessId;
    }

    public String getUserBusinessId() {
        return userBusinessId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCountBox(int countBox) {
        this.countBox = countBox;
    }

    public int getCountBox() {
        return countBox;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }
}