package com.italianswapp.yourtraining.OfflineDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WorkoutDao {

    @Query("SELECT * FROM workoutsaved ORDER BY date DESC")
    List<WorkoutSaved> getAll();

    @Insert
    void insertAll(WorkoutSaved... workouts);

    @Delete
    void delete(WorkoutSaved workoutSaved);
}
