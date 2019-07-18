package com.gliesereum.coupler.data.network.json.record_new;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class PackageDto {

    @SerializedName("duration")
    private int duration;

    @SerializedName("objectState")
    private String objectState;

    @SerializedName("servicesIds")
    private List<Object> servicesIds;

    @SerializedName("name")
    private String name;

    @SerializedName("businessId")
    private String businessId;

    @SerializedName("discount")
    private int discount;

    @SerializedName("id")
    private String id;

    @SerializedName("services")
    private List<ServicesItem> services;

    @SerializedName("descriptions")
    private Object descriptions;

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setObjectState(String objectState) {
        this.objectState = objectState;
    }

    public String getObjectState() {
        return objectState;
    }

    public void setServicesIds(List<Object> servicesIds) {
        this.servicesIds = servicesIds;
    }

    public List<Object> getServicesIds() {
        return servicesIds;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessId() {
        return businessId;
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

    public void setDescriptions(Object descriptions) {
        this.descriptions = descriptions;
    }

    public Object getDescriptions() {
        return descriptions;
    }
}