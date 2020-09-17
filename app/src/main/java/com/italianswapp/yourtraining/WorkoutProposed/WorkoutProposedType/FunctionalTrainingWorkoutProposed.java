package com.italianswapp.yourtraining.WorkoutProposed.WorkoutProposedType;

import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.ExerciseBuilder;
import com.italianswapp.yourtraining.WorkoutProposed.Workout.Workout;
import com.italianswapp.yourtraining.WorkoutProposed.Workout.WorkoutBuilder;

class FunctionalTrainingWorkoutProposed extends ProposedWorkoutList {

    public FunctionalTrainingWorkoutProposed() {
        super();
        initializeWorkoutList();
    }

    private void initializeWorkoutList() {
        workoutList.add(
                WorkoutBuilder.newBuilder()
                        .setCategory(Workout.WorkoutCategory.FUNCTIONALTRAINING)
                        .setLevel(Workout.WorkoutLevel.INTERMEDIATE)
                        .addExercise(ExerciseBuilder.newBuilder().
                                setName("Prova").build())
                        .build()
        );
    }
}
