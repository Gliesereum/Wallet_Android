package com.gliesereum.karma.data.network.json.carwash;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class FilterCarWashBody {

    @SerializedName("serviceIds")
    private List<String> serviceIds = new ArrayList<>();

    @SerializedName("targetId")
    private String targetId;

    @SerializedName("serviceType")
    private String serviceType;

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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public FilterCarWashBody(String targetId, List<String> serviceIds) {
        this.targetId = targetId;
        this.serviceIds = serviceIds;
    }

    public FilterCarWashBody(List<String> serviceIds, String targetId, String serviceType) {
        this.serviceIds = serviceIds;
        this.targetId = targetId;
        this.serviceType = serviceType;
    }

    public FilterCarWashBody() {
    }
}
