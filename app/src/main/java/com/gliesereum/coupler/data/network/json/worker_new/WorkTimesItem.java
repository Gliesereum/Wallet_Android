package com.gliesereum.coupler.data.network.json.worker_new;

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
    private long from;

    @SerializedName("id")
    private String id;

    @SerializedName("to")
    private long to;

    @SerializedName("type")
    private String type;

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

    public void setFrom(long from) {
        this.from = from;
    }

    public long getFrom() {
        return from;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public long getTo() {
        return to;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }
}