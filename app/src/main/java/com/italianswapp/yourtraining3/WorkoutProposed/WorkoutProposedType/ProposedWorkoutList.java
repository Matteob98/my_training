package com.italianswapp.yourtraining3.WorkoutProposed.WorkoutProposedType;
import com.italianswapp.yourtraining3.WorkoutProposed.Workout.Workout;

import java.util.ArrayList;
import java.util.Set;

public abstract class ProposedWorkoutList {

    protected ArrayList<Workout> workoutList;

    public ProposedWorkoutList() {
        workoutList = new ArrayList<>();
    }

    public ArrayList<Workout> getWorkoutList() {
        return workoutList;
    }

    /**
     * La funzione prende in come parametri i filtri e restituisce una copia dela lista filtrata
     * Se i filtri sono vuoti restituisce la lista intera
     */
    public ArrayList<Workout> getWorkoutListWithFilter(Set<Workout.WorkoutLevel> levelFilterSet, Set<Workout.MuscleGroup> muscleFilterSet) {
        ArrayList<Workout> filteredWorkoutList = new ArrayList<>();
        boolean filterPassed;
        for (Workout w: workoutList) {
            filterPassed = false;
            if(levelFilterSet.size()!=0)
                if (! levelFilterSet.contains(w.getLevel()))
                    continue;

            if (muscleFilterSet.size()==0)
                filterPassed = true;
            else {
                for (Workout.MuscleGroup muscleGroup : w.getMuscleGroupList())
                    if (muscleFilterSet.contains(muscleGroup))
                        filterPassed = true;
            }

            if (filterPassed)
                filteredWorkoutList.add(w);

        }

        return filteredWorkoutList;
    }
}
