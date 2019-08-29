package com.gliesereum.coupler.data.network.json.bonus;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class BonusScoreResponse {

    @SerializedName("score")
    private int score;

    @SerializedName("updateDate")
    private long updateDate;

    @SerializedName("id")
    private String id;

    @SerializedName("history")
    private List<HistoryItem> history;

    @SerializedName("userId")
    private String userId;

    @SerializedName("createDate")
    private long createDate;

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setHistory(List<HistoryItem> history) {
        this.history = history;
    }

    public List<HistoryItem> getHistory() {
        return history;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getCreateDate() {
        return createDate;
    }
}