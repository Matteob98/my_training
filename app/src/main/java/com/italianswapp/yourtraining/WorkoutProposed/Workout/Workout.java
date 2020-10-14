package com.italianswapp.yourtraining.WorkoutProposed.Workout;

import android.os.Parcel;
import android.os.Parcelable;

import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.Exercise;

import java.util.ArrayList;

public class Workout implements Parcelable {



    public enum WorkoutCategory {
        BODYBUILDING,
        FUNCTIONALTRAINING,
        FREEBODY,
        STRETCHING
        /*
        Se aggiungo qui devo aggiungere anche in workoutSaved
         */
    }

    public enum WorkoutLevel {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }

    public enum MuscleGroup {
        ABS,
        BACK,
        CHEST
    }

    private String title;
    private WorkoutCategory category;
    private  WorkoutLevel level;
    private ArrayList<Exercise> exerciseList;
    private ArrayList<MuscleGroup> muscleGroupList;
    private boolean isPremium;

    public Workout () {
        title = "";
        category = WorkoutCategory.BODYBUILDING;
        level = WorkoutLevel.BEGINNER;
        exerciseList = new ArrayList<>();
        muscleGroupList = new ArrayList<>();
        isPremium = false;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public WorkoutCategory getCategory() {
        return category;
    }

    public void setCategory(WorkoutCategory category) {
        this.category = category;
    }

    public ArrayList<Exercise> getExerciseList() {
        return exerciseList;
    }

    public void setExerciseList(ArrayList<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    public WorkoutLevel getLevel() {
        return level;
    }

    public void setLevel(WorkoutLevel level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<MuscleGroup> getMuscleGroupList() {
        return muscleGroupList;
    }

    public void setMuscleGroupList(ArrayList<MuscleGroup> muscleGroupList) {
        this.muscleGroupList = muscleGroupList;
    }

    /*

    Metodi che rendono la classe parcellabile

     */

    public static final Creator<Workout> CREATOR = new Creator<Workout>() {
        @Override
        public Workout createFromParcel(Parcel in) { return new Workout(in); }

        @Override
        public Workout[] newArray(int size) { return new Workout[size]; }
    };

    @Override
    public int describeContents() { return hashCode(); }

    /**
     * GLi enum sono salvati sotto forma di stringa
     * muscleGroupList essendo una lista di enum dava problemi quindi ho fatto così:
     * Numero di elementi nella lista
     * String(enum) 1
     * String(enum) 2
     * ...
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        /*

         */
        ArrayList<String> muscleGroupNameList = new ArrayList<>();
        for (MuscleGroup muscleGroup: muscleGroupList)
            muscleGroupNameList.add(muscleGroup.name());


        dest.writeString(title);
        dest.writeString(category.name());
        dest.writeString(level.name());
        dest.writeStringList(muscleGroupNameList);
        dest.writeInt(isPremium ? 1 : 0);
        dest.writeTypedList(exerciseList);

    }

    /**
     * GLi enum sono salvati sotto forma di stringa
     * muscleGroupList essendo una lista di enum dava problemi quindi ho fatto così:
     * Numero di elementi nella lista
     * String(enum) 1
     * String(enum) 2
     * ...
     * @param in
     */
    protected Workout(Parcel in) {

        title = in.readString();
        category = WorkoutCategory.valueOf(in.readString());
        level = WorkoutLevel.valueOf(in.readString());
        ArrayList<String> muscleGroupNameList = new ArrayList<String>();
        in.readStringList(muscleGroupNameList);
        isPremium = in.readInt() == 1;
        exerciseList = in.createTypedArrayList(Exercise.CREATOR);

        muscleGroupList = new ArrayList<>();
        for (String name : muscleGroupNameList)
            muscleGroupList.add(MuscleGroup.valueOf(name));
    }
}
