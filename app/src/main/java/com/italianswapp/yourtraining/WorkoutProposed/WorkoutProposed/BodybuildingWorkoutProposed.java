package com.italianswapp.yourtraining.WorkoutProposed.WorkoutProposed;

import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.Exercise;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.ExerciseBuilder;
import com.italianswapp.yourtraining.WorkoutProposed.Workout.Workout;
import com.italianswapp.yourtraining.WorkoutProposed.Workout.WorkoutBuilder;

public class BodybuildingWorkoutProposed extends ProposedWorkoutList  {

    public BodybuildingWorkoutProposed() {
        super();
        initializeWorkoutList();
    }

    private void initializeWorkoutList() {
        workoutList.add(
                WorkoutBuilder.newBuilder()
                        .setTitle("Primo Allenamento")
                        .setCategory(Workout.WorkoutCategory.BODYBUILDING)
                        .setLevel(Workout.WorkoutLevel.INTERMEDIATE)
                        .addExercise(ExerciseBuilder.newBuilder()
                            .setName("Prova BD")
                            .build())
                        .addMuscleGroup(Workout.MuscleGroup.ABS)
                        .addMuscleGroup(Workout.MuscleGroup.BACK)
                .build()
        );
        workoutList.add(
                WorkoutBuilder.newBuilder()
                        .setTitle("Secondo")
                        .setCategory(Workout.WorkoutCategory.BODYBUILDING)
                        .setLevel(Workout.WorkoutLevel.ADVANCED)
                        .addExercise(ExerciseBuilder.newBuilder()
                                .setType(Exercise.CircuitType.REST)
                                .setRec(3000)
                                .build())
                        .addExercise(ExerciseBuilder.newBuilder()
                                .setName("Prova")
                                .build())
                        .addExercise(ExerciseBuilder.newBuilder()
                                .setName("Terzo")
                                .build())
                        .addExercise(ExerciseBuilder.newBuilder()
                                .setName("Quarto")
                                .build())
                        .addMuscleGroup(Workout.MuscleGroup.ABS)
                        .addMuscleGroup(Workout.MuscleGroup.CHEST)
                        .build()
        );
    }
}
