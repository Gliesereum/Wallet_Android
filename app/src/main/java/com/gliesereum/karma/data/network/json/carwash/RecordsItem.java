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
    private Object packageId;

    @SerializedName("description")
    private String description;

    @SerializedName("services")
    private List<ServicesItem> services;

    @SerializedName("statusPay")
    private Object statusPay;

    @SerializedName("carId")
    private String carId;

    @SerializedName("statusWashing")
    private Object statusWashing;

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

    @SerializedName("carWashId")
    private String carWashId;

    public void setStatusRecord(String statusRecord) {
        this.statusRecord = statusRecord;
    }

    public String getStatusRecord() {
        return statusRecord;
    }

    public void setServicesIds(List<Object> servicesIds) {
        this.servicesIds = servicesIds;
    }

    public List<Object> getServicesIds() {
        return servicesIds;
    }

    public void setPackageId(Object packageId) {
        this.packageId = packageId;
    }

    public Object getPackageId() {
        return packageId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setServices(List<ServicesItem> services) {
        this.services = services;
    }

    public List<ServicesItem> getServices() {
        return services;
    }

    public void setStatusPay(Object statusPay) {
        this.statusPay = statusPay;
    }

    public Object getStatusPay() {
        return statusPay;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarId() {
        return carId;
    }

    public void setStatusWashing(Object statusWashing) {
        this.statusWashing = statusWashing;
    }

    public Object getStatusWashing() {
        return statusWashing;
    }

    public void setWorkingSpaceId(String workingSpaceId) {
        this.workingSpaceId = workingSpaceId;
    }

    public String getWorkingSpaceId() {
        return workingSpaceId;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setFinish(long finish) {
        this.finish = finish;
    }

    public long getFinish() {
        return finish;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getBegin() {
        return begin;
    }

    public void setCarWashId(String carWashId) {
        this.carWashId = carWashId;
    }

    public String getCarWashId() {
        return carWashId;
    }
}