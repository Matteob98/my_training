package com.italianswapp.yourtraining;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.Exercise;
import java.util.ArrayList;

public class WorkoutBuilder {

    /**
     * GLi stessi attributi di workout
     */
    private static Workout.WorkoutCategory category;
    private static Workout.WorkoutLevel level;
    private static ArrayList<Exercise> exerciseList;

    /**
     * Essendo un builder non ha costruttore pubblico
     */
    private WorkoutBuilder () {
        exerciseList = new ArrayList<>();
    }

    public static WorkoutBuilder newBuilder () {
        return new WorkoutBuilder();
    }

    public WorkoutBuilder setCategory (Workout.WorkoutCategory category) {
        WorkoutBuilder.category = category;
        return this;
    }

    public WorkoutBuilder setLevel (Workout.WorkoutLevel level) {
        WorkoutBuilder.level = level;
        return this;
    }

    public WorkoutBuilder addExercise (Exercise exercise) {
        exerciseList.add(exercise);
        return this;
    }


    public Workout build() {
        Workout workout = new Workout();
        if(category!=null) workout.setCategory(category);
        if(level!=null) workout.setLevel(level);
        workout.setExerciseList(exerciseList);
        return workout;
    }

}
