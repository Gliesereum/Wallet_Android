//package com.gliesereum.karma.data.network.json.car;
//
//import com.google.gson.annotations.SerializedName;
//
//import java.util.Objects;
//
//import javax.annotation.Generated;
//
//@Generated("com.robohorse.robopojogenerator")
//public class ServiceClassItem {
//
//    @SerializedName("serviceType")
//    private String serviceType;
//
//    @SerializedName("name")
//    private String name;
//
//    @SerializedName("description")
//    private String description;
//
//    @SerializedName("orderIndex")
//    private int orderIndex;
//
//    @SerializedName("id")
//    private String id;
//
//    public void setServiceType(String serviceType) {
//        this.serviceType = serviceType;
//    }
//
//    public String getServiceType() {
//        return serviceType;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setOrderIndex(int orderIndex) {
//        this.orderIndex = orderIndex;
//    }
//
//    public int getOrderIndex() {
//        return orderIndex;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ServiceClassItem that = (ServiceClassItem) o;
//        return Objects.equals(id, that.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
//}