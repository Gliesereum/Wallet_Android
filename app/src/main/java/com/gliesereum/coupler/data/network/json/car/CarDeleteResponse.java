package com.gliesereum.coupler.data.network.json.car;

import com.google.gson.annotations.SerializedName;

public class CarDeleteResponse {

    @SerializedName("result")
    private Boolean result;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
