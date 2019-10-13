package com.gliesereum.coupler.data.network.json.carwash;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class AllCarWashResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("businessVerify")
    private Boolean businessVerify;

    @SerializedName("businessCategoryId")
    private String businessCategoryId;

    @SerializedName("address")
    private String address;

    @SerializedName("comments")
    private List<CommentsItem> comments;

    @SerializedName("servicePrices")
    private List<ServicePricesItem> servicePrices;

    @SerializedName("records")
    private List<RecordsItem> records;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("rating")
    private Rating rating;

    @SerializedName("description")
    private String description;

    @SerializedName("media")
    private List<MediaItem> media;

    @SerializedName("packages")
    private List<PackagesItem> packages;

    @SerializedName("logoUrl")
    private String logoUrl;

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

    @SerializedName("carWashId")
    private String carWashId;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("timeZone")
    private Integer timeZone;

    @SerializedName("coverUrl")
    private String coverUrl;

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Boolean getBusinessVerify() {
        return businessVerify;
    }

    public void setBusinessVerify(Boolean businessVerify) {
        this.businessVerify = businessVerify;
    }

    public String getBusinessCategoryId() {
        return businessCategoryId;
    }

    public void setBusinessCategoryId(String businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }

    public Integer getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(Integer timeZone) {
        this.timeZone = timeZone;
    }

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

    public List<CommentsItem> getComments() {
        return comments;
    }

    public void setComments(List<CommentsItem> comments) {
        this.comments = comments;
    }

    public void setServicePrices(List<ServicePricesItem> servicePrices) {
        this.servicePrices = servicePrices;
    }

    public List<ServicePricesItem> getServicePrices() {
        return servicePrices;
    }

    public void setRecords(List<RecordsItem> records) {
        this.records = records;
    }

    public List<RecordsItem> getRecords() {
        return records;
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

    public List<MediaItem> getMedia() {
        return media;
    }

    public void setMedia(List<MediaItem> media) {
        this.media = media;
    }

    public void setPackages(List<PackagesItem> packages) {
        this.packages = packages;
    }

    public List<PackagesItem> getPackages() {
        return packages;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
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

    public void setCarWashId(String carWashId) {
        this.carWashId = carWashId;
    }

    public String getCarWashId() {
        return carWashId;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }
}