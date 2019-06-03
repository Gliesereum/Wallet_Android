package com.gliesereum.coupler.data.network.json.user;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class User {

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("country")
    private String country;

    @SerializedName("address")
    private String address;

    @SerializedName("gender")
    private Object gender;

    @SerializedName("city")
    private String city;

    @SerializedName("avatarUrl")
    private String avatarUrl;

    @SerializedName("banStatus")
    private String banStatus;

    @SerializedName("addAddress")
    private String addAddress;

    @SerializedName("coverUrl")
    private String coverUrl;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("verifiedStatus")
    private String verifiedStatus;

    @SerializedName("middleName")
    private String middleName;

    @SerializedName("id")
    private String id;

    @SerializedName("position")
    private String position;

    @SerializedName("userType")
    private String userType;

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public Object getGender() {
        return gender;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setBanStatus(String banStatus) {
        this.banStatus = banStatus;
    }

    public String getBanStatus() {
        return banStatus;
    }

    public void setAddAddress(String addAddress) {
        this.addAddress = addAddress;
    }

    public String getAddAddress() {
        return addAddress;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setVerifiedStatus(String verifiedStatus) {
        this.verifiedStatus = verifiedStatus;
    }

    public String getVerifiedStatus() {
        return verifiedStatus;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }
}