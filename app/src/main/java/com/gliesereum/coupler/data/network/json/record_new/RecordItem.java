package com.gliesereum.coupler.data.network.json.record_new;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class RecordItem {

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("canceledDescription")
    private String canceledDescription;

    @SerializedName("packageDto")
    private PackageDto packageDto;

    @SerializedName("businessId")
    private String businessId;

    @SerializedName("description")
    private String description;

    @SerializedName("statusPay")
    private String statusPay;

    @SerializedName("payType")
    private String payType;

    @SerializedName("workingSpaceId")
    private String workingSpaceId;

    @SerializedName("price")
    private int price;

    @SerializedName("client")
    private Object client;

    @SerializedName("finish")
    private long finish;

    @SerializedName("id")
    private String id;

    @SerializedName("notificationSend")
    private boolean notificationSend;

    @SerializedName("statusProcess")
    private String statusProcess;

    @SerializedName("recordNumber")
    private int recordNumber;

    @SerializedName("workerId")
    private String workerId;

    @SerializedName("clientId")
    private String clientId;

    @SerializedName("targetId")
    private String targetId;

    @SerializedName("business")
    private Business business;

    @SerializedName("statusRecord")
    private String statusRecord;

    @SerializedName("servicesIds")
    private List<ServicesItem> servicesIds;

    @SerializedName("packageId")
    private String packageId;

    @SerializedName("services")
    private List<ServicesItem> services;

    @SerializedName("specifiedWorkingSpace")
    private boolean specifiedWorkingSpace;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("businessCategoryId")
    private String businessCategoryId;

    @SerializedName("middleName")
    private String middleName;

    @SerializedName("begin")
    private long begin;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCanceledDescription() {
        return canceledDescription;
    }

    public void setCanceledDescription(String canceledDescription) {
        this.canceledDescription = canceledDescription;
    }

    public PackageDto getPackageDto() {
        return packageDto;
    }

    public void setPackageDto(PackageDto packageDto) {
        this.packageDto = packageDto;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatusPay() {
        return statusPay;
    }

    public void setStatusPay(String statusPay) {
        this.statusPay = statusPay;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
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

    public Object getClient() {
        return client;
    }

    public void setClient(Object client) {
        this.client = client;
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

    public boolean isNotificationSend() {
        return notificationSend;
    }

    public void setNotificationSend(boolean notificationSend) {
        this.notificationSend = notificationSend;
    }

    public String getStatusProcess() {
        return statusProcess;
    }

    public void setStatusProcess(String statusProcess) {
        this.statusProcess = statusProcess;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public String getStatusRecord() {
        return statusRecord;
    }

    public void setStatusRecord(String statusRecord) {
        this.statusRecord = statusRecord;
    }

    public List<ServicesItem> getServicesIds() {
        return servicesIds;
    }

    public void setServicesIds(List<ServicesItem> servicesIds) {
        this.servicesIds = servicesIds;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public List<ServicesItem> getServices() {
        return services;
    }

    public void setServices(List<ServicesItem> services) {
        this.services = services;
    }

    public boolean isSpecifiedWorkingSpace() {
        return specifiedWorkingSpace;
    }

    public void setSpecifiedWorkingSpace(boolean specifiedWorkingSpace) {
        this.specifiedWorkingSpace = specifiedWorkingSpace;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBusinessCategoryId() {
        return businessCategoryId;
    }

    public void setBusinessCategoryId(String businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordItem that = (RecordItem) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}