package com.italianswapp.yourtraining.OfflineDatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.italianswapp.yourtraining.WorkoutProposed.Workout.Workout;

import java.util.Date;

@Entity
public class WorkoutSaved {


    public enum WorkoutType {
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

    public enum WorkoutSensation {
        EASY,
        NORMAL,
        DIFFICULT
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private WorkoutType type;

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
    private WorkoutSensation sensation;

    @ColumnInfo
    private Workout workout;

    /*
    Costruttore completo e costruttore vuoto obbligatori
     */

    @Ignore
    public WorkoutSaved(WorkoutType type, Date date, String time, WorkoutSensation sensation, Workout workout) {
        this.type = type;
        this.date = date;
        this.time = time;
        this.sensation = sensation;
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

    public WorkoutType getType() {
        return type;
    }

    public void setType(WorkoutType type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public WorkoutSensation getSensation() {
        return sensation;
    }

    public void setSensation(WorkoutSensation sensation) {
        this.sensation = sensation;
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
