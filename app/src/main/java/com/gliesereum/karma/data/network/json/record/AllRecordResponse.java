package com.gliesereum.karma.data.network.json.record;

import com.gliesereum.karma.data.network.json.carwash.AllCarWashResponse;
import com.gliesereum.karma.data.network.json.carwash.PackagesItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class AllRecordResponse {

    @SerializedName("business")
    private AllCarWashResponse business;

    @SerializedName("packageDto")
    private PackagesItem packageDto;

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

    @SerializedName("targetId")
    private String targetId;

    @SerializedName("statusProcess")
    private String statusProcess;

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

    public AllCarWashResponse getBusiness() {
        return business;
    }

    public void setBusiness(AllCarWashResponse business) {
        this.business = business;
    }

    public PackagesItem getPackageDto() {
        return packageDto;
    }

    public void setPackageDto(PackagesItem packageDto) {
        this.packageDto = packageDto;
    }

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

    public Object getPackageId() {
        return packageId;
    }

    public void setPackageId(Object packageId) {
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

    public String getStatusProcess() {
        return statusProcess;
    }

    public void setStatusProcess(String statusProcess) {
        this.statusProcess = statusProcess;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllRecordResponse that = (AllRecordResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}