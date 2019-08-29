package com.gliesereum.coupler.data.network.json.bonus;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class HistoryItem {

    @SerializedName("updateDate")
    private long updateDate;

    @SerializedName("bonusScoreId")
    private String bonusScoreId;

    @SerializedName("id")
    private String id;

    @SerializedName("value")
    private int value;

    @SerializedName("createDate")
    private long createDate;

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setBonusScoreId(String bonusScoreId) {
        this.bonusScoreId = bonusScoreId;
    }

    public String getBonusScoreId() {
        return bonusScoreId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getCreateDate() {
        return createDate;
    }
}