package com.techdev.goalbuzz.model;

import androidx.annotation.DrawableRes;

public class Social {

    @DrawableRes
    private int icon;
    private String title;

    @DrawableRes
    public int getIcon() {
        return icon;
    }

    public void setIcon(@DrawableRes int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
