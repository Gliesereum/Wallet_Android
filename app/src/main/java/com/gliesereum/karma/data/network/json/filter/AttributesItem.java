package com.gliesereum.karma.data.network.json.filter;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class AttributesItem {

    @SerializedName("filterId")
    private String filterId;

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("value")
    private String value;

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }

    public String getFilterId() {
        return filterId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}