package com.italianswapp.yourtraining3.WorkoutProposed.WorkoutProposedType;

import com.italianswapp.yourtraining3.Timer.Circuit.CircuitSettings.ExerciseBuilder;
import com.italianswapp.yourtraining3.WorkoutProposed.Workout.Workout;
import com.italianswapp.yourtraining3.WorkoutProposed.Workout.WorkoutBuilder;

class FreeBodyWorkoutProposed extends  ProposedWorkoutList {

    public FreeBodyWorkoutProposed() {
        super();
        initializeWorkoutList();
    }

    private void initializeWorkoutList() {
        workoutList.add(
                WorkoutBuilder.newBuilder()
                        .setCategory(Workout.WorkoutCategory.FREEBODY)
                        .setLevel(Workout.WorkoutLevel.INTERMEDIATE)
                        .addExercise(ExerciseBuilder.newBuilder().
                                setName("Prova").build())
                        .build()
        );
    }
}
