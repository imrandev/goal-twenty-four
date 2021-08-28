package com.techdev.goalbuzz.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.techdev.goalbuzz.room.model.Admob;

import java.util.List;

@Dao
public interface AdmobDao {

    @Query("SELECT * FROM Admob ORDER BY id")
    List<Admob> findAll();

    @Insert
    void insert(Admob adMob);

    @Update
    void update(Admob adMob);

    @Delete
    void delete(Admob adMob);

    @Query("SELECT * FROM Admob WHERE name = :name")
    Admob findById(String name);

}
