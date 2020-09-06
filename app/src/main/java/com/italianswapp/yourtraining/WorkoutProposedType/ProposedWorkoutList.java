package com.italianswapp.yourtraining.WorkoutProposedType;
import com.italianswapp.yourtraining.Workout;

import java.util.ArrayList;

public abstract class ProposedWorkoutList {

    protected ArrayList<Workout> workoutList;

    public ProposedWorkoutList() {
        workoutList = new ArrayList<>();
    }

    public ArrayList<Workout> getWorkoutList() {
        return workoutList;
    }

    /**
     * Applica un filtro alla lista secondo il livello passato
     * Non modifica la lista di partenza
     * @param level
     * @return la lista filtrata
     */
    public ArrayList<Workout> getWorkoutListLevelFilter(Workout.WorkoutLevel level) {
        ArrayList<Workout> filteredWorkoutList = new ArrayList<>();

        for (Workout w: workoutList) {
            if ( w.getLevel().equals(level) )
                filteredWorkoutList.add(w);
        }

        return filteredWorkoutList;
    }
}
