package com.gliesereum.coupler.data.network.json.code;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class SignupBody {

    @SerializedName("value")
    private String value;

    @SerializedName("code")
    private String code;

    @SerializedName("type")
    private String type;

    @SerializedName("userType")
    private String userType;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SignupBody(String value, String code, String type, String userType) {
        this.value = value;
        this.code = code;
        this.type = type;
        this.userType = userType;
    }
}
