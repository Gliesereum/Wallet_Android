package com.gliesereum.karma.data.network.json.user;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class User {

    @SerializedName("lastName")
    private Object lastName;

    @SerializedName("country")
    private Object country;

    @SerializedName("address")
    private Object address;

    @SerializedName("gender")
    private Object gender;

    @SerializedName("city")
    private Object city;

    @SerializedName("avatarUrl")
    private Object avatarUrl;

    @SerializedName("banStatus")
    private String banStatus;

    @SerializedName("addAddress")
    private Object addAddress;

    @SerializedName("coverUrl")
    private Object coverUrl;

    @SerializedName("firstName")
    private Object firstName;

    @SerializedName("verifiedStatus")
    private String verifiedStatus;

    @SerializedName("middleName")
    private Object middleName;

    @SerializedName("id")
    private String id;

    @SerializedName("position")
    private Object position;

    @SerializedName("userType")
    private String userType;

    public void setLastName(Object lastName) {
        this.lastName = lastName;
    }

    public Object getLastName() {
        return lastName;
    }

    public void setCountry(Object country) {
        this.country = country;
    }

    public Object getCountry() {
        return country;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public Object getAddress() {
        return address;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public Object getGender() {
        return gender;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public Object getCity() {
        return city;
    }

    public void setAvatarUrl(Object avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Object getAvatarUrl() {
        return avatarUrl;
    }

    public void setBanStatus(String banStatus) {
        this.banStatus = banStatus;
    }

    public String getBanStatus() {
        return banStatus;
    }

    public void setAddAddress(Object addAddress) {
        this.addAddress = addAddress;
    }

    public Object getAddAddress() {
        return addAddress;
    }

    public void setCoverUrl(Object coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Object getCoverUrl() {
        return coverUrl;
    }

    public void setFirstName(Object firstName) {
        this.firstName = firstName;
    }

    public Object getFirstName() {
        return firstName;
    }

    public void setVerifiedStatus(String verifiedStatus) {
        this.verifiedStatus = verifiedStatus;
    }

    public String getVerifiedStatus() {
        return verifiedStatus;
    }

    public void setMiddleName(Object middleName) {
        this.middleName = middleName;
    }

    public Object getMiddleName() {
        return middleName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setPosition(Object position) {
        this.position = position;
    }

    public Object getPosition() {
        return position;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }
}