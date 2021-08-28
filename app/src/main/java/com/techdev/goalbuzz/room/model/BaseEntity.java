package com.techdev.goalbuzz.room.model;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class BaseEntity {
    @PrimaryKey
    @ColumnInfo(name ="id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
