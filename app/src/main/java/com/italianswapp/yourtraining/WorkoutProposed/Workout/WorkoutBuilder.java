package com.italianswapp.yourtraining.WorkoutProposed.Workout;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.Exercise;
import java.util.ArrayList;

public class WorkoutBuilder {

    /**
     * GLi stessi attributi di workout
     */
    private static Workout.WorkoutCategory category;
    private static Workout.WorkoutLevel level;
    private static ArrayList<Exercise> exerciseList;
    private static String title;
    private static ArrayList<Workout.MuscleGroup> muscleGroupList;
    private static boolean isPremium;

    /**
     * Essendo un builder non ha costruttore pubblico
     */
    private WorkoutBuilder () {
        isPremium = false;
        exerciseList = new ArrayList<>();
        muscleGroupList = new ArrayList<>();
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

    public WorkoutBuilder setTitle (String title) {
        WorkoutBuilder.title = title;
        return this;
    }

    public WorkoutBuilder addExercise (Exercise exercise) {
        exerciseList.add(exercise);
        return this;
    }

    public WorkoutBuilder addMuscleGroup (Workout.MuscleGroup muscleGroup) {
        muscleGroupList.add(muscleGroup);
        return this;
    }

    public WorkoutBuilder setPremium() {
        isPremium = true;
        return this;
    }

    public WorkoutBuilder setNotPremium() {
        isPremium = false;
        return this;
    }


    public Workout build() {
        Workout workout = new Workout();
        if(category!=null) workout.setCategory(category);
        if(level!=null) workout.setLevel(level);
        workout.setPremium(isPremium);
        workout.setExerciseList(exerciseList);
        workout.setTitle(title);
        workout.setMuscleGroupList(muscleGroupList);
        return workout;
    }

}
