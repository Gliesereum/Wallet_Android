package com.gliesereum.karma.data.network.json.carwash;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ServicesItem {

    @SerializedName("duration")
    private int duration;

    @SerializedName("serviceClass")
    private List<Object> serviceClass;

    @SerializedName("price")
    private int price;

    @SerializedName("businessServiceId")
    private String businessServiceId;

    @SerializedName("service")
    private Service service;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    @SerializedName("serviceId")
    private String serviceId;

    @SerializedName("carBodies")
    private List<Object> carBodies;

    @SerializedName("interiorTypes")
    private List<Object> interiorTypes;

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setServiceClass(List<Object> serviceClass) {
        this.serviceClass = serviceClass;
    }

    public List<Object> getServiceClass() {
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

    public void setService(Service service) {
        this.service = service;
    }

    public Service getService() {
        return service;
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

    public void setCarBodies(List<Object> carBodies) {
        this.carBodies = carBodies;
    }

    public List<Object> getCarBodies() {
        return carBodies;
    }

    public void setInteriorTypes(List<Object> interiorTypes) {
        this.interiorTypes = interiorTypes;
    }

    public List<Object> getInteriorTypes() {
        return interiorTypes;
    }
}