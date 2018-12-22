package com.gliesereum.karma.data.network.json.carwash;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class AllCarWashResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("address")
    private String address;

    @SerializedName("comments")
    private List<Object> comments;

    @SerializedName("servicePrices")
    private List<ServicePricesItem> servicePrices;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("rating")
    private Rating rating;

    @SerializedName("description")
    private String description;

    @SerializedName("media")
    private List<Object> media;

    @SerializedName("packages")
    private List<PackagesItem> packages;

    @SerializedName("logoUrl")
    private Object logoUrl;

    @SerializedName("addPhone")
    private Object addPhone;

    @SerializedName("phone")
    private String phone;

    @SerializedName("workTimes")
    private List<WorkTimesItem> workTimes;

    @SerializedName("name")
    private String name;

    @SerializedName("spaces")
    private List<SpacesItem> spaces;

    @SerializedName("longitude")
    private double longitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setComments(List<Object> comments) {
        this.comments = comments;
    }

    public List<Object> getComments() {
        return comments;
    }

    public void setServicePrices(List<ServicePricesItem> servicePrices) {
        this.servicePrices = servicePrices;
    }

    public List<ServicePricesItem> getServicePrices() {
        return servicePrices;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Rating getRating() {
        return rating;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setMedia(List<Object> media) {
        this.media = media;
    }

    public List<Object> getMedia() {
        return media;
    }

    public void setPackages(List<PackagesItem> packages) {
        this.packages = packages;
    }

    public List<PackagesItem> getPackages() {
        return packages;
    }

    public void setLogoUrl(Object logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Object getLogoUrl() {
        return logoUrl;
    }

    public void setAddPhone(Object addPhone) {
        this.addPhone = addPhone;
    }

    public Object getAddPhone() {
        return addPhone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setWorkTimes(List<WorkTimesItem> workTimes) {
        this.workTimes = workTimes;
    }

    public List<WorkTimesItem> getWorkTimes() {
        return workTimes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSpaces(List<SpacesItem> spaces) {
        this.spaces = spaces;
    }

    public List<SpacesItem> getSpaces() {
        return spaces;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }
}