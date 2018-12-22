package com.gliesereum.karma.data.network.json.carwash;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ServiceClassItem {

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("orderIndex")
    private int orderIndex;

    @SerializedName("id")
    private String id;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}