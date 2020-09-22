package com.italianswapp.yourtraining3.WorkoutProposed.WorkoutProposedType;

import com.italianswapp.yourtraining3.Timer.Circuit.CircuitSettings.ExerciseBuilder;
import com.italianswapp.yourtraining3.WorkoutProposed.Workout.Workout;
import com.italianswapp.yourtraining3.WorkoutProposed.Workout.WorkoutBuilder;

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
