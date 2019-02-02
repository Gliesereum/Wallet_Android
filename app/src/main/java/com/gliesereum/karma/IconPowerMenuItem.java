package com.gliesereum.karma;

public class IconPowerMenuItem {

    private String id;
    private String title;

    public IconPowerMenuItem(String title, String id) {
        this.title = title;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
