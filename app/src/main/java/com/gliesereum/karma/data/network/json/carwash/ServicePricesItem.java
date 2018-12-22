package com.gliesereum.karma.data.network.json.carwash;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ServicePricesItem {

    @SerializedName("duration")
    private int duration;

    @SerializedName("carBody")
    private Object carBody;

    @SerializedName("serviceClass")
    private List<ServiceClassItem> serviceClass;

    @SerializedName("price")
    private int price;

    @SerializedName("businessServiceId")
    private String businessServiceId;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    @SerializedName("serviceId")
    private String serviceId;

    @SerializedName("interiorType")
    private Object interiorType;

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setCarBody(Object carBody) {
        this.carBody = carBody;
    }

    public Object getCarBody() {
        return carBody;
    }

    public void setServiceClass(List<ServiceClassItem> serviceClass) {
        this.serviceClass = serviceClass;
    }

    public List<ServiceClassItem> getServiceClass() {
        return serviceClass;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setInteriorType(Object interiorType) {
        this.interiorType = interiorType;
    }

    public Object getInteriorType() {
        return interiorType;
    }
}