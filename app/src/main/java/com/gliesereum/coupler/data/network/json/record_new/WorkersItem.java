package com.gliesereum.coupler.data.network.json.record_new;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class WorkersItem {

    @SerializedName("workingSpaceId")
    private String workingSpaceId;

    @SerializedName("corporationId")
    private String corporationId;

    @SerializedName("workTimes")
    private List<Object> workTimes;

    @SerializedName("businessId")
    private String businessId;

    @SerializedName("id")
    private String id;

    @SerializedName("position")
    private String position;

    @SerializedName("userId")
    private String userId;

    @SerializedName("user")
    private Object user;

    public void setWorkingSpaceId(String workingSpaceId) {
        this.workingSpaceId = workingSpaceId;
    }

    public String getWorkingSpaceId() {
        return workingSpaceId;
    }

    public void setCorporationId(String corporationId) {
        this.corporationId = corporationId;
    }

    public String getCorporationId() {
        return corporationId;
    }

    public void setWorkTimes(List<Object> workTimes) {
        this.workTimes = workTimes;
    }

    public List<Object> getWorkTimes() {
        return workTimes;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public Object getUser() {
        return user;
    }
}