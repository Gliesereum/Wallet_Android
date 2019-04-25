package com.gliesereum.karma.data.network.json.businesscategory;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class BusinesCategoryResponse {

    @SerializedName("code")
    private String code;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("id")
    private String id;

    @SerializedName("businessType")
    private String businessType;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessType() {
        return businessType;
    }
}