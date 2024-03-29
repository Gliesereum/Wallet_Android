package com.gliesereum.coupler.data.network.json.carwashnew;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class CarWashResponse {

    @SerializedName("address")
    private String address;

    @SerializedName("corporationId")
    private String corporationId;

    @SerializedName("objectState")
    private String objectState;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("description")
    private String description;

    @SerializedName("timeZone")
    private int timeZone;

    @SerializedName("services")
    private Object services;

    @SerializedName("geoPoint")
    private GeoPoint geoPoint;

    @SerializedName("countBox")
    private Object countBox;

    @SerializedName("logoUrl")
    private String logoUrl;

    @SerializedName("score")
    private double score;

    @SerializedName("addPhone")
    private Object addPhone;

    @SerializedName("phone")
    private String phone;

    @SerializedName("workTimes")
    private List<WorkTimesItem> workTimes;

    @SerializedName("name")
    private String name;

    @SerializedName("businessCategoryId")
    private String businessCategoryId;

    @SerializedName("id")
    private String id;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("rating")
    private float rating;

    @SerializedName("ratingCount")
    private int ratingCount;

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setCorporationId(String corporationId) {
        this.corporationId = corporationId;
    }

    public String getCorporationId() {
        return corporationId;
    }

    public void setObjectState(String objectState) {
        this.objectState = objectState;
    }

    public String getObjectState() {
        return objectState;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTimeZone(int timeZone) {
        this.timeZone = timeZone;
    }

    public int getTimeZone() {
        return timeZone;
    }

    public void setServices(Object services) {
        this.services = services;
    }

    public Object getServices() {
        return services;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setCountBox(Object countBox) {
        this.countBox = countBox;
    }

    public Object getCountBox() {
        return countBox;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScore() {
        return score;
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

    public void setBusinessCategoryId(String businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }

    public String getBusinessCategoryId() {
        return businessCategoryId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarWashResponse that = (CarWashResponse) o;
        return Double.compare(that.latitude, latitude) == 0 &&
                timeZone == that.timeZone &&
                Double.compare(that.longitude, longitude) == 0 &&
                Objects.equals(logoUrl, that.logoUrl) &&
                Objects.equals(name, that.name) &&
                Objects.equals(businessCategoryId, that.businessCategoryId) &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, timeZone, logoUrl, name, businessCategoryId, id, longitude);
    }
}