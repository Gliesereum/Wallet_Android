package com.gliesereum.coupler.data.network.json.carwash;

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

    @SerializedName("corporationServiceId")
    private String corporationServiceId;

    @SerializedName("from")
    private long from;

    @SerializedName("id")
    private String id;

    @SerializedName("to")
    private long to;

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

    public void setCorporationServiceId(String corporationServiceId) {
        this.corporationServiceId = corporationServiceId;
    }

    public String getCorporationServiceId() {
        return corporationServiceId;
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
}