package com.gliesereum.karma.data.network.json.carwash;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class FilterCarWashBody {

    @SerializedName("serviceIds")
    private List<String> serviceIds;

    @SerializedName("carId")
    private String carId;

    public List<String> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<String> serviceIds) {
        this.serviceIds = serviceIds;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public FilterCarWashBody(String carId, List<String> serviceIds) {
        this.carId = carId;
        this.serviceIds = serviceIds;
    }

    public FilterCarWashBody() {
    }
}
