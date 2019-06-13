package com.gliesereum.coupler.data.network.json.carwash;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class FilterCarWashBody {

    @SerializedName("serviceIds")
    private List<String> serviceIds;

    @SerializedName("targetId")
    private String targetId;

    @SerializedName("businessCategoryId")
    private String businessCategoryId;

    public List<String> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<String> serviceIds) {
        this.serviceIds = serviceIds;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getBusinessCategoryId() {
        return businessCategoryId;
    }

    public void setBusinessCategoryId(String businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }

    public FilterCarWashBody(String targetId, List<String> serviceIds) {
        this.targetId = targetId;
        this.serviceIds = serviceIds;
    }

    public FilterCarWashBody(List<String> serviceIds, String targetId, String businessCategoryId) {
        this.serviceIds = serviceIds;
        this.targetId = targetId;
        this.businessCategoryId = businessCategoryId;
    }

    public FilterCarWashBody(String businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }

    public FilterCarWashBody() {
    }
}
