package com.italianswapp.yourtraining3.OfflineDatabase;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.italianswapp.yourtraining3.WorkoutProposed.Workout.Workout;

import java.util.Date;

public class WorkoutDatabaseConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static WorkoutSaved.WorkoutType typeFromString(String type) {
        return type == null ? null : WorkoutSaved.WorkoutType.valueOf(type);
    }

    @TypeConverter
    public static String typeToString(WorkoutSaved.WorkoutType type) {
        return type.name();
    }

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static WorkoutSaved.WorkoutSensation sensationFromString(String sensation) {
        return sensation == null ? null : WorkoutSaved.WorkoutSensation.valueOf(sensation);
    }

    @TypeConverter
    public static String sensationToString(WorkoutSaved.WorkoutSensation sensation) {
        return sensation.name();
    }

    @TypeConverter
    public static Workout workoutFromString(String workoutString) {
        if (workoutString == null)
            return new Workout();
        Workout workout  =  gson.fromJson(workoutString, Workout.class);
        return workout;
    }

    @TypeConverter
    public static String workoutToString(Workout workout) {
        String json = gson.toJson(workout);
        return json;
    }


}
