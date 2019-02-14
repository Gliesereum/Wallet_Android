package com.gliesereum.karma.util.photo;

public class PhotoListIsEmptyException extends NullPointerException {
    public PhotoListIsEmptyException() {
        super("Impossible init the photos without some photo added!");
    }
}
