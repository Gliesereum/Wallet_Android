package com.gliesereum.karma.data.network.json.carwash;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class WorkTimesItem {

    @SerializedName("isWork")
    private boolean isWork;

    @SerializedName("dayOfWeek")
    private String dayOfWeek;

    @SerializedName("carServiceType")
    private String carServiceType;

    @SerializedName("businessServiceId")
    private String businessServiceId;

    @SerializedName("from")
    private String from;

    @SerializedName("id")
    private String id;

    @SerializedName("to")
    private String to;

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

    public void setCarServiceType(String carServiceType) {
        this.carServiceType = carServiceType;
    }

    public String getCarServiceType() {
        return carServiceType;
    }

    public void setBusinessServiceId(String businessServiceId) {
        this.businessServiceId = businessServiceId;
    }

    public String getBusinessServiceId() {
        return businessServiceId;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }
}