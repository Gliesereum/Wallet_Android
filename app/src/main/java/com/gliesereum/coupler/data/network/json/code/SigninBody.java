package com.gliesereum.coupler.data.network.json.code;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class SigninBody {

    @SerializedName("value")
    private String value;

    @SerializedName("code")
    private String code;

    @SerializedName("type")
    private String type;

    @SerializedName("referralCode")
    private String referralCode;

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
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

    public SigninBody(String value, String code, String type, String referralCode) {
        this.value = value;
        this.code = code;
        this.type = type;
        this.referralCode = referralCode;
    }
}
