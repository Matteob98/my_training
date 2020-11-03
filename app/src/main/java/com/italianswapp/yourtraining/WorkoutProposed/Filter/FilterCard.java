package com.italianswapp.yourtraining.WorkoutProposed.Filter;

import com.italianswapp.yourtraining.WorkoutProposed.Workout.Workout;

/**
 * Classe per utilizzare le card "filtro" per gli allenamenti proposti
 * Usata sia dalla recyclerView del dialog per la selezione dei filtri sia dalla recyclerView della schermata workoutProposed
 */
class FilterCard {

    /**
     * Tipi di filtro
     */
    public enum Type {
        MUSCLE,
        LEVEL
    };

    private String name;
    private Workout.MuscleGroup muscleGroup;
    private Workout.WorkoutLevel workoutLevel;
    private Type type;

    public FilterCard(Workout.MuscleGroup muscleGroup) {
        type = Type.MUSCLE;
        this.muscleGroup = muscleGroup;
    }

    public FilterCard(Workout.WorkoutLevel level) {
        type = Type.LEVEL;
        this.workoutLevel=level;

    }

    public String getName() {
        return type==Type.LEVEL ?
                workoutLevel.toString() :
                muscleGroup.toString();
    }

    public Workout.MuscleGroup getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(Workout.MuscleGroup muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public Workout.WorkoutLevel getWorkoutLevel() {
        return workoutLevel;
    }

    public void setWorkoutLevel(Workout.WorkoutLevel workoutLevel) {
        this.workoutLevel = workoutLevel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}
