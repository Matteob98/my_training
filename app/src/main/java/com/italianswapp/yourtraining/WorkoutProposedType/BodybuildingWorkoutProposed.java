package com.italianswapp.yourtraining.WorkoutProposedType;

import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.Exercise;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.ExerciseBuilder;
import com.italianswapp.yourtraining.Workout;
import com.italianswapp.yourtraining.WorkoutBuilder;

public class BodybuildingWorkoutProposed extends ProposedWorkoutList{

    public BodybuildingWorkoutProposed() {
        super();
        initializeWorkoutList();
    }

    private void initializeWorkoutList() {
        workoutList.add(
                WorkoutBuilder.newBuilder()
                .setCategory(Workout.WorkoutCategory.BODYBUILDING)
                .setLevel(Workout.WorkoutLevel.INTERMEDIATE)
                .addExercise(ExerciseBuilder.newBuilder()
                        .setName("Prova BD")
                        .build())
                .build()
        );
        workoutList.add(
                WorkoutBuilder.newBuilder()
                        .setCategory(Workout.WorkoutCategory.BODYBUILDING)
                        .setLevel(Workout.WorkoutLevel.INTERMEDIATE)
                        .addExercise(ExerciseBuilder.newBuilder()
                                .setType(Exercise.CircuitType.REST)
                                .setRec(3000)
                                .build())
                        .addExercise(ExerciseBuilder.newBuilder()
                                .setName("Prova ")
                                .build())
                        .build()
        );
    }
}
