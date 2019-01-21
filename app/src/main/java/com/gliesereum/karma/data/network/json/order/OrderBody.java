package com.gliesereum.karma.data.network.json.order;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class OrderBody {

    @SerializedName("workingSpaceId")
    private String workingSpaceId;

    @SerializedName("servicesIds")
    private List<String> servicesIds;

    @SerializedName("description")
    private String description;

    @SerializedName("begin")
    private long begin;

    @SerializedName("carId")
    private String carId;

    @SerializedName("carWashId")
    private String carWashId;

    @SerializedName("packageId")
    private String packageId;

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public void setWorkingSpaceId(String workingSpaceId) {
        this.workingSpaceId = workingSpaceId;
    }

    public String getWorkingSpaceId() {
        return workingSpaceId;
    }

    public void setServicesIds(List<String> servicesIds) {
        this.servicesIds = servicesIds;
    }

    public List<String> getServicesIds() {
        return servicesIds;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getBegin() {
        return begin;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarWashId(String carWashId) {
        this.carWashId = carWashId;
    }

    public String getCarWashId() {
        return carWashId;
    }
}