package com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings;

import android.content.res.Resources;

import com.italianswapp.yourtraining.R;
import com.italianswapp.yourtraining.Utilities.App;

public class ExerciseBuilder {

    private static Exercise exercise;

    private ExerciseBuilder() {
        exercise = new Exercise();
    }

    public static ExerciseBuilder newBuilder () {
        return  new ExerciseBuilder();
    }

    public ExerciseBuilder setReps(int reps) {
        exercise.setReps(reps);
        return this;
    }

    public ExerciseBuilder setRepetition(int repetition) {
        exercise.setRepetition(repetition);
        return this;
    }

    public ExerciseBuilder setNumberSets(int numberSets) {
        exercise.setNumberSets(numberSets);
        return this;
    }

    public ExerciseBuilder setTotalSets (int totalSets) {
        exercise.setTotalSets(totalSets);
        return this;
    }

    public ExerciseBuilder setRec(long rec){
        exercise.setRec(rec);
        return this;
    }

    public ExerciseBuilder setIsReps(boolean isReps) {
        exercise.setReps(isReps);
        return this;
    }

    public ExerciseBuilder setHasRecs(boolean hasRecs){
        exercise.setHasRecs(hasRecs);
        return this;
    }

    public ExerciseBuilder setHasSets(boolean hasSets) {
        exercise.setHasSets(hasSets);
        return this;
    }

    public ExerciseBuilder setName(String name) {
        exercise.setName(name);
        return this;
    }

    public ExerciseBuilder setSupersetExercise(int reps, boolean isReps, String name) {
        exercise.setType(Exercise.CircuitType.SUPERSET);
        exercise.setSupersetExercise(reps, isReps, name);
        return this;
    }

    public ExerciseBuilder setType(Exercise.CircuitType type) {
        exercise.setType(type);
        if(exercise.getName().equals("")) {
            switch (type) {
                case EMOM:
                    exercise.setName(App.getContext().getString(R.string.emom));
                    break;
                case REST:
                    exercise.setName(App.getContext().getString(R.string.rest));
                    break;
                case TABATA:
                    exercise.setName(App.getContext().getString(R.string.tabata));
                    break;
        }

        }
        return this;
    }

    public Exercise build() {
        return exercise;
    }
}
