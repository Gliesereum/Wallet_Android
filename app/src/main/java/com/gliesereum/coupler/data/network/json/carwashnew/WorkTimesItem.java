package com.gliesereum.coupler.data.network.json.carwashnew;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class WorkTimesItem {

    @SerializedName("isWork")
    private boolean isWork;

    @SerializedName("dayOfWeek")
    private String dayOfWeek;

    @SerializedName("businessCategoryId")
    private String businessCategoryId;

    @SerializedName("from")
    private String from;

    @SerializedName("to")
    private String to;

    @SerializedName("objectId")
    private String objectId;

    public void setIsWork(boolean isWork) {
        this.isWork = isWork;
    }

    public boolean isIsWork() {
        return isWork;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setBusinessCategoryId(String businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }

    public String getBusinessCategoryId() {
        return businessCategoryId;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }
}