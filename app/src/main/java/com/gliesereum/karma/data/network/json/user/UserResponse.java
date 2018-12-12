package com.gliesereum.karma.data.network.json.user;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class UserResponse {

    @SerializedName("user")
    private User user;

    @SerializedName("tokenInfo")
    private TokenInfo tokenInfo;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }
}