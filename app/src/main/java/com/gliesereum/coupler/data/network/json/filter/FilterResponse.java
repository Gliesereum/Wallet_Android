package com.gliesereum.coupler.data.network.json.filter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class FilterResponse {

    @SerializedName("serviceType")
    private String serviceType;

    @SerializedName("attributes")
    private List<AttributesItem> attributes;

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("value")
    private String value;

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setAttributes(List<AttributesItem> attributes) {
        this.attributes = attributes;
    }

    public List<AttributesItem> getAttributes() {
        return attributes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}