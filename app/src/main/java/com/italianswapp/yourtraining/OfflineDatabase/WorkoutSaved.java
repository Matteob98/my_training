package com.italianswapp.yourtraining.OfflineDatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.italianswapp.yourtraining.WorkoutProposed.Workout.Workout;

import java.util.Date;

@Entity
public class WorkoutSaved {

    public enum workoutType {
        WORKOUT,
        CHRONOMETER,
        TABATA,
        AMRAP,
        TIMER,
        EMOM,
        CONCENTRIC_PHASE,
        BODYBUILDING,
        FUNCTIONALTRAINING,
        FREEBODY,
        STRETCHING
    }

    public enum workoutSensation {
        EASY,
        NORMAL,
        DIFFICULT
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private workoutType type;

    @ColumnInfo
    private String title;

    /**
     * Permette di vedere sia data che ora
     */
    @ColumnInfo
    private Date date;

    /**
     * Tempo impiegato per finire l'allenamento
     */
    @ColumnInfo
    private String time;

    @ColumnInfo
    private workoutSensation sensation;

    @ColumnInfo
    private Workout.WorkoutLevel level;

    private Workout workout;

    /*
    Costruttore completo e costruttore vuoto obbligatori
     */

    public WorkoutSaved(int id, workoutType type, String title, Date date, String time, workoutSensation sensation, Workout.WorkoutLevel level, Workout workout) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.date = date;
        this.time = time;
        this.sensation = sensation;
        this.level = level;
        this.workout = workout;
    }

    public WorkoutSaved() {
    }

    /*
    Getter e setter obbligatori
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public workoutType getType() {
        return type;
    }

    public void setType(workoutType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public workoutSensation getSensation() {
        return sensation;
    }

    public void setSensation(workoutSensation sensation) {
        this.sensation = sensation;
    }

    public Workout.WorkoutLevel getLevel() {
        return level;
    }

    public void setLevel(Workout.WorkoutLevel level) {
        this.level = level;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
