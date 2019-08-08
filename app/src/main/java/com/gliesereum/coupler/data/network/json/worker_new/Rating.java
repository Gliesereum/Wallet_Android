package com.gliesereum.coupler.data.network.json.worker_new;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Rating {

    @SerializedName("rating")
    private double rating;

    @SerializedName("count")
    private double count;

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getCount() {
        return count;
    }
}