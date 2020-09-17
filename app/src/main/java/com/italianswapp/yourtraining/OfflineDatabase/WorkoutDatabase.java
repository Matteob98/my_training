package com.italianswapp.yourtraining.OfflineDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

//todo ad ogni aggiornamento deve essere modificata la version altrimenti non aggiorna le modifiche
@Database(entities = {WorkoutSaved.class}, version = 1)
public abstract class WorkoutDatabase extends RoomDatabase {
    public abstract WorkoutDao workoutDao();
}
