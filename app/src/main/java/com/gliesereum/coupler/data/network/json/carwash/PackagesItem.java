package com.gliesereum.coupler.data.network.json.carwash;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class PackagesItem {

    @SerializedName("duration")
    private int duration;

    @SerializedName("businessServiceId")
    private String businessServiceId;

    @SerializedName("name")
    private String name;

    @SerializedName("discount")
    private int discount;

    @SerializedName("id")
    private String id;

    @SerializedName("services")
    private List<ServicesItem> services;

    @SerializedName("objectState")
    private String objectState;

    public String getObjectState() {
        return objectState;
    }

    public void setObjectState(String objectState) {
        this.objectState = objectState;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setBusinessServiceId(String businessServiceId) {
        this.businessServiceId = businessServiceId;
    }

    public String getBusinessServiceId() {
        return businessServiceId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setServices(List<ServicesItem> services) {
        this.services = services;
    }

    public List<ServicesItem> getServices() {
        return services;
    }
}