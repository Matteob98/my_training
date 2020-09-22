package com.italianswapp.yourtraining3.OfflineDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

//todo ad ogni aggiornamento deve essere modificata la version altrimenti non aggiorna le modifiche
@Database(entities = {WorkoutSaved.class}, version = 1)
@TypeConverters({WorkoutDatabaseConverter.class})
public abstract class WorkoutDatabase extends RoomDatabase {
    private static WorkoutDatabase INSTANCE;
    public abstract WorkoutDao workoutDao();


    public static WorkoutDatabase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.inMemoryDatabaseBuilder(context.getApplicationContext(), WorkoutDatabase.class)
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static WorkoutDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), WorkoutDatabase.class, "workout_db")
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
