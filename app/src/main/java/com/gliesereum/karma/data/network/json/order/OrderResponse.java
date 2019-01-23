package com.gliesereum.karma.data.network.json.order;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class OrderResponse {

    @SerializedName("statusRecord")
    private Object statusRecord;

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
    private Object statusWashing;

    @SerializedName("workingSpaceId")
    private String workingSpaceId;

    @SerializedName("price")
    private int price;

    @SerializedName("finish")
    private Long finish;

    @SerializedName("id")
    private Object id;

    @SerializedName("begin")
    private Long begin;

    @SerializedName("carWashId")
    private String carWashId;

    public void setStatusRecord(Object statusRecord) {
        this.statusRecord = statusRecord;
    }

    public Object getStatusRecord() {
        return statusRecord;
    }

    public void setServicesIds(List<String> servicesIds) {
        this.servicesIds = servicesIds;
    }

    public List<String> getServicesIds() {
        return servicesIds;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setServices(List<Object> services) {
        this.services = services;
    }

    public List<Object> getServices() {
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

    public void setFinish(Long finish) {
        this.finish = finish;
    }

    public Long getFinish() {
        return finish;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getId() {
        return id;
    }

    public void setBegin(Long begin) {
        this.begin = begin;
    }

    public Long getBegin() {
        return begin;
    }

    public void setCarWashId(String carWashId) {
        this.carWashId = carWashId;
    }

    public String getCarWashId() {
        return carWashId;
    }
}