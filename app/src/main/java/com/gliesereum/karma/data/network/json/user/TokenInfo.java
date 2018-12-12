package com.gliesereum.karma.data.network.json.user;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class TokenInfo {

    @SerializedName("refreshExpirationDate")
    private long refreshExpirationDate;

    @SerializedName("accessExpirationDate")
    private long accessExpirationDate;

    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("userId")
    private String userId;

    @SerializedName("refreshToken")
    private String refreshToken;

    public void setRefreshExpirationDate(long refreshExpirationDate) {
        this.refreshExpirationDate = refreshExpirationDate;
    }

    public long getRefreshExpirationDate() {
        return refreshExpirationDate;
    }

    public void setAccessExpirationDate(long accessExpirationDate) {
        this.accessExpirationDate = accessExpirationDate;
    }

    public long getAccessExpirationDate() {
        return accessExpirationDate;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}