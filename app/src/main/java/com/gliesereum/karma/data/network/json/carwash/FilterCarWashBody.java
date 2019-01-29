package com.gliesereum.karma.data.network.json.carwash;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class FilterCarWashBody {

    @SerializedName("serviceIds")
    private List<String> serviceIds;

    @SerializedName("targetId")
    private String targetId;

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

    public FilterCarWashBody(String targetId, List<String> serviceIds) {
        this.targetId = targetId;
        this.serviceIds = serviceIds;
    }

    public FilterCarWashBody() {
    }
}
