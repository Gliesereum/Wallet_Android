package com.gliesereum.karma.data.network.json.notificatoin;

import com.google.gson.annotations.SerializedName;

public class RegistrationTokenDeleteResponse {

    @SerializedName("result")
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
