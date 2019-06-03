package com.gliesereum.coupler.data.network.json.order;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class OrderResponse {

    @SerializedName("statusRecord")
    private String statusRecord;

    @SerializedName("servicesIds")
    private List<String> servicesIds;

    @SerializedName("packageId")
    private String packageId;

    @SerializedName("description")
    private String description;

    @SerializedName("services")
    private List<Object> services;

    @SerializedName("statusPay")
    private Object statusPay;

    @SerializedName("carId")
    private String carId;

    @SerializedName("statusWashing")
    private String statusWashing;

    @SerializedName("workingSpaceId")
    private String workingSpaceId;

    @SerializedName("price")
    private int price;

    @SerializedName("finish")
    private Long finish;

    @SerializedName("id")
    private String id;

    @SerializedName("begin")
    private Long begin;

    @SerializedName("carWashId")
    private String carWashId;

    public String getStatusRecord() {
        return statusRecord;
    }

    public void setStatusRecord(String statusRecord) {
        this.statusRecord = statusRecord;
    }

    public List<String> getServicesIds() {
        return servicesIds;
    }

    public void setServicesIds(List<String> servicesIds) {
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

    public List<Object> getServices() {
        return services;
    }

    public void setServices(List<Object> services) {
        this.services = services;
    }

    public Object getStatusPay() {
        return statusPay;
    }

    public void setStatusPay(Object statusPay) {
        this.statusPay = statusPay;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getStatusWashing() {
        return statusWashing;
    }

    public void setStatusWashing(String statusWashing) {
        this.statusWashing = statusWashing;
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

    public Long getFinish() {
        return finish;
    }

    public void setFinish(Long finish) {
        this.finish = finish;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getBegin() {
        return begin;
    }

    public void setBegin(Long begin) {
        this.begin = begin;
    }

    public String getCarWashId() {
        return carWashId;
    }

    public void setCarWashId(String carWashId) {
        this.carWashId = carWashId;
    }
}