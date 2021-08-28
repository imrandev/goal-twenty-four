package com.techdev.goalbuzz.room.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "admob")
public class Admob extends BaseEntity {
    @ColumnInfo(name ="count")
    private int count;
    @ColumnInfo(name ="adType")
    private String adType;
    @ColumnInfo(name ="name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }
}
