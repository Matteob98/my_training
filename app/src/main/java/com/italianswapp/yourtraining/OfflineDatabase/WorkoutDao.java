package com.italianswapp.yourtraining.OfflineDatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WorkoutDao {

    @Query("SELECT * FROM workoutsaved")
    List<WorkoutSaved> getAll();

    @Insert
    void insertAll(WorkoutSaved... workouts);
}
