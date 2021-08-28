package com.techdev.goalbuzz.room.dao;

import androidx.room.Query;

import com.techdev.goalbuzz.room.model.BaseEntity;

import java.util.List;

public interface EntityDao<T extends BaseEntity> {

    List<T> findAll();

    void insert(T model);

    void update(T model);

    void delete(T model);

    T findById(int id);
}
