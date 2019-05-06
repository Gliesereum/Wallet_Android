package com.gliesereum.karma.data.network.json.notificatoin;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificatoinBody {

    @SerializedName("userId")
    private String userId;

    @SerializedName("firebaseRegistrationToken")
    private String firebaseRegistrationToken;

    @SerializedName("notificationEnable")
    private Boolean notificationEnable;

    @SerializedName("subscribes")
    private List<UserSubscribe> subscribes;

    public NotificatoinBody(String firebaseRegistrationToken, Boolean notificationEnable) {
        this.firebaseRegistrationToken = firebaseRegistrationToken;
        this.notificationEnable = notificationEnable;
    }

    public NotificatoinBody(String firebaseRegistrationToken, Boolean notificationEnable, List<UserSubscribe> subscribes) {
        this.firebaseRegistrationToken = firebaseRegistrationToken;
        this.notificationEnable = notificationEnable;
        this.subscribes = subscribes;
    }
}
