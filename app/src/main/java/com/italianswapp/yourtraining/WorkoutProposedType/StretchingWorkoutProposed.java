package com.italianswapp.yourtraining.WorkoutProposedType;

import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.Exercise;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.ExerciseBuilder;
import com.italianswapp.yourtraining.Workout;
import com.italianswapp.yourtraining.WorkoutBuilder;

class StretchingWorkoutProposed extends  ProposedWorkoutList {

    public StretchingWorkoutProposed() {
        super();
        initializeWorkoutList();
    }

    private void initializeWorkoutList() {
        workoutList.add(
                WorkoutBuilder.newBuilder()
                        .setCategory(Workout.WorkoutCategory.STRETCHING)
                        .setLevel(Workout.WorkoutLevel.INTERMEDIATE)
                        .addExercise(ExerciseBuilder.newBuilder().
                                setName("Prova").build())
                        .build()
        );
    }
}
