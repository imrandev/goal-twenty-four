package com.techdev.goalbuzz.model;

import androidx.annotation.NonNull;

public class Query {

    private Integer id;
    private String name;

    public Query() {
    }

    public Query(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "Query {" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
