package com.gliesereum.karma.data.network.json.carwash;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class SpacesItem {

    @SerializedName("workerId")
    private Object workerId;

    @SerializedName("statusSpace")
    private Object statusSpace;

    @SerializedName("carServiceType")
    private String carServiceType;

    @SerializedName("businessServiceId")
    private String businessServiceId;

    @SerializedName("id")
    private String id;

    @SerializedName("indexNumber")
    private int indexNumber;

    public void setWorkerId(Object workerId) {
        this.workerId = workerId;
    }

    public Object getWorkerId() {
        return workerId;
    }

    public void setStatusSpace(Object statusSpace) {
        this.statusSpace = statusSpace;
    }

    public Object getStatusSpace() {
        return statusSpace;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setIndexNumber(int indexNumber) {
        this.indexNumber = indexNumber;
    }

    public int getIndexNumber() {
        return indexNumber;
    }
}