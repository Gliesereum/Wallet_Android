package com.gliesereum.karma.util.photo;

import java.io.Serializable;

/**
 * Created by srokitskiy on 19.08.17.
 */

public class Photo implements Serializable {
    private String description = "";
    private String imageUrl;


    public Photo() {
    }

    public Photo(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Photo(String imageUrl, String description) {
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
