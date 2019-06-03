package com.gliesereum.coupler.data.network.json.car;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class CarInfo {

    @SerializedName("brandId")
    private String brandId;

    @SerializedName("modelId")
    private String modelId;

    @SerializedName("yearId")
    private String yearId;

    @SerializedName("registrationNumber")
    private String registrationNumber;

    @SerializedName("description")
    private String description;

    @SerializedName("interior")
    private String interior;

    @SerializedName("carBody")
    private String carBody;

    @SerializedName("colour")
    private String colour;

    public String getBrandId() {
        return brandId;
    }

    public String getModelId() {
        return modelId;
    }

    public String getYearId() {
        return yearId;
    }


    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getDescription() {
        return description;
    }

    public String getInterior() {
        return interior;
    }

    public String getCarBody() {
        return carBody;
    }

    public String getColour() {
        return colour;
    }


    public CarInfo(String brandId, String modelId, String yearId, String registrationNumber, String description, String interior, String carBody, String colour) {
        this.brandId = brandId;
        this.modelId = modelId;
        this.yearId = yearId;
        this.registrationNumber = registrationNumber;
        this.description = description;
        this.interior = interior;
        this.carBody = carBody;
        this.colour = colour;
    }
}
