package com.italianswapp.yourtraining;

import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.Exercise;

import java.util.ArrayList;

public class Workout {

    public enum WorkoutCategory {
        BODYBUILDING,
        FUNCTIONALTRAINING,
        FREEBODY,
        STRETCHING
    }

    public enum WorkoutLevel {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }

    private WorkoutCategory category;
    private  WorkoutLevel level;
    private ArrayList<Exercise> exerciseList;

    //Todo implementare la classe. Rappresenta un allenamento (composto da più esercizi)

    public Workout () {}

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
}
