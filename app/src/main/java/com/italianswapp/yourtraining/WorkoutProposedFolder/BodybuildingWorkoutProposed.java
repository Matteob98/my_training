package com.italianswapp.yourtraining.WorkoutProposedFolder;

import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.Exercise;
import com.italianswapp.yourtraining.Workout;
import com.italianswapp.yourtraining.WorkoutBuilder;

class BodybuildingWorkoutProposed extends ProposedWorkoutList{

    public BodybuildingWorkoutProposed() {
        super();
        initializeWorkoutList();
    }

    private void initializeWorkoutList() {
        workoutList.add(
                WorkoutBuilder.newBuilder()
                .setCategory(Workout.WorkoutCategory.BODYBUILDING)
                .setLevel(Workout.WorkoutLevel.INTERMEDIATE)
                .addExercise(new Exercise("Prova"))
                .build()
        );
    }
}
