package com.gliesereum.coupler.data.network.json.record_new;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Business {

    @SerializedName("address")
    private String address;

    @SerializedName("corporationId")
    private String corporationId;

    @SerializedName("objectState")
    private String objectState;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("description")
    private String description;

    @SerializedName("timeZone")
    private int timeZone;

    @SerializedName("descriptions")
    private Object descriptions;

    @SerializedName("logoUrl")
    private Object logoUrl;

    @SerializedName("addPhone")
    private Object addPhone;

    @SerializedName("phone")
    private String phone;

    @SerializedName("workTimes")
    private List<WorkTimesItem> workTimes;

    @SerializedName("name")
    private String name;

    @SerializedName("businessCategoryId")
    private String businessCategoryId;

    @SerializedName("businessCategory")
    private BusinessCategory businessCategory;

    @SerializedName("spaces")
    private List<SpacesItem> spaces;

    @SerializedName("id")
    private String id;

    @SerializedName("longitude")
    private double longitude;

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setCorporationId(String corporationId) {
        this.corporationId = corporationId;
    }

    public String getCorporationId() {
        return corporationId;
    }

    public void setObjectState(String objectState) {
        this.objectState = objectState;
    }

    public String getObjectState() {
        return objectState;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTimeZone(int timeZone) {
        this.timeZone = timeZone;
    }

    public int getTimeZone() {
        return timeZone;
    }

    public void setDescriptions(Object descriptions) {
        this.descriptions = descriptions;
    }

    public Object getDescriptions() {
        return descriptions;
    }

    public void setLogoUrl(Object logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Object getLogoUrl() {
        return logoUrl;
    }

    public void setAddPhone(Object addPhone) {
        this.addPhone = addPhone;
    }

    public Object getAddPhone() {
        return addPhone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setWorkTimes(List<WorkTimesItem> workTimes) {
        this.workTimes = workTimes;
    }

    public List<WorkTimesItem> getWorkTimes() {
        return workTimes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setBusinessCategoryId(String businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }

    public String getBusinessCategoryId() {
        return businessCategoryId;
    }

    public void setBusinessCategory(BusinessCategory businessCategory) {
        this.businessCategory = businessCategory;
    }

    public BusinessCategory getBusinessCategory() {
        return businessCategory;
    }

    public void setSpaces(List<SpacesItem> spaces) {
        this.spaces = spaces;
    }

    public List<SpacesItem> getSpaces() {
        return spaces;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }
}