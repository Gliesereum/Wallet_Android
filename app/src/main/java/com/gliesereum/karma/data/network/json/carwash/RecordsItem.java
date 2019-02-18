package com.gliesereum.karma.data.network.json.carwash;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class RecordsItem {

    @SerializedName("statusRecord")
    private String statusRecord;

    @SerializedName("servicesIds")
    private List<Object> servicesIds;

    @SerializedName("packageId")
    private String packageId;

    @SerializedName("description")
    private String description;

    @SerializedName("services")
    private List<ServicesItem> services;

    @SerializedName("statusPay")
    private Object statusPay;

    @SerializedName("targetId")
    private String targetId;

    @SerializedName("workingSpaceId")
    private String workingSpaceId;

    @SerializedName("price")
    private int price;

    @SerializedName("finish")
    private long finish;

    @SerializedName("id")
    private String id;

    @SerializedName("begin")
    private long begin;

    @SerializedName("businessId")
    private String businessId;

    public String getStatusRecord() {
        return statusRecord;
    }

    public void setStatusRecord(String statusRecord) {
        this.statusRecord = statusRecord;
    }

    public List<Object> getServicesIds() {
        return servicesIds;
    }

    public void setServicesIds(List<Object> servicesIds) {
        this.servicesIds = servicesIds;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ServicesItem> getServices() {
        return services;
    }

    public void setServices(List<ServicesItem> services) {
        this.services = services;
    }

    public Object getStatusPay() {
        return statusPay;
    }

    public void setStatusPay(Object statusPay) {
        this.statusPay = statusPay;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getWorkingSpaceId() {
        return workingSpaceId;
    }

    public void setWorkingSpaceId(String workingSpaceId) {
        this.workingSpaceId = workingSpaceId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getFinish() {
        return finish;
    }

    public void setFinish(long finish) {
        this.finish = finish;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}