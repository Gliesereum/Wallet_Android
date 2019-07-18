package com.gliesereum.coupler.data.network.json.record_new;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ServicesItem {

    @SerializedName("duration")
    private int duration;

    @SerializedName("objectState")
    private String objectState;

    @SerializedName("serviceClass")
    private List<Object> serviceClass;

    @SerializedName("price")
    private int price;

    @SerializedName("service")
    private Service service;

    @SerializedName("name")
    private String name;

    @SerializedName("businessId")
    private String businessId;

    @SerializedName("description")
    private Object description;

    @SerializedName("attributes")
    private List<Object> attributes;

    @SerializedName("id")
    private String id;

    @SerializedName("serviceId")
    private String serviceId;

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

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getDescription() {
        return description;
    }

    public void setAttributes(List<Object> attributes) {
        this.attributes = attributes;
    }

    public List<Object> getAttributes() {
        return attributes;
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

    public void setDescriptions(Object descriptions) {
        this.descriptions = descriptions;
    }

    public Object getDescriptions() {
        return descriptions;
    }
}