package com.techdev.goalbuzz.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.techdev.goalbuzz.room.model.Result;

import java.util.List;

@Dao
public interface ResultDao {

    @Query("SELECT * FROM result ORDER BY id")
    List<Result> findAll();

    @Insert
    void insert(Result result);

    @Update
    void update(Result result);

    @Delete
    void delete(Result result);

    @Query("SELECT * FROM result WHERE id = :id")
    Result findById(int id);
}
