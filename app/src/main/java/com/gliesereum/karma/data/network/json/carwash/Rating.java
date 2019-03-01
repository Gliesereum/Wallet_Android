package com.gliesereum.karma.data.network.json.carwash;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Rating {

    @SerializedName("rating")
    private Float rating;

    @SerializedName("count")
    private int count;

    public float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}