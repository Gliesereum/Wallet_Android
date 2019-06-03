package com.gliesereum.coupler.data.network.json.notificatoin;

import com.google.gson.annotations.SerializedName;

public class UserSubscribe {

    @SerializedName("userDeviceId")
    private String userDeviceId;

    @SerializedName("notificationEnable")
    private Boolean notificationEnable;

    @SerializedName("objectId")
    private String objectId;

    @SerializedName("subscribeDestination")
    private String subscribeDestination;

    public UserSubscribe(Boolean notificationEnable, String subscribeDestination) {
        this.notificationEnable = notificationEnable;
        this.subscribeDestination = subscribeDestination;
    }
}
