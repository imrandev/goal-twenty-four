package com.techdev.goalbuzz.model;

import androidx.annotation.DrawableRes;

public class League {

    private String name;
    @DrawableRes private int drawableRes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public void setDrawableRes(int drawableRes) {
        this.drawableRes = drawableRes;
    }
}
