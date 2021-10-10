package com.techdev.goalbuzz.core.datasource.local.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.techdev.goalbuzz.core.datasource.local.db.entities.Match;

import java.util.List;

@Dao
public interface MatchDao {

    @Query("SELECT * FROM `Match` ORDER BY id")
    List<Match> findAll();

    @Insert
    void insert(Match match);

    @Update
    void update(Match match);

    @Delete
    void delete(Match match);

    @Query("SELECT * FROM `Match` WHERE id = :id")
    Match findById(int id);
}
