package com.gliesereum.coupler.data.network.json.worker_new;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class User {

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("gender")
    private Object gender;

    @SerializedName("phone")
    private String phone;

    @SerializedName("avatarUrl")
    private String avatarUrl;

    @SerializedName("middleName")
    private String middleName;

    @SerializedName("id")
    private String id;

    @SerializedName("email")
    private Object email;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public Object getGender() {
        return gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
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

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getEmail() {
        return email;
    }
}