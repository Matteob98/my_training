package com.italianswapp.yourtraining.WorkoutProposed.WorkoutProposed;
import com.italianswapp.yourtraining.WorkoutProposed.Workout.Workout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public abstract class ProposedWorkoutList {

    protected ArrayList<Workout> workoutList;

    public ProposedWorkoutList() {
        workoutList = new ArrayList<>();
    }

    public ArrayList<Workout> getWorkoutList() {
        Collections.shuffle(workoutList);
        return workoutList;
    }

    /**
     * La funzione prende in come parametri i filtri e restituisce una copia dela lista filtrata
     * Se i filtri sono vuoti restituisce la lista intera
     */
    public ArrayList<Workout> getWorkoutListWithFilter(ArrayList<Workout.WorkoutLevel> levelFilterSet, ArrayList<Workout.MuscleGroup> muscleFilterSet) {
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

        Collections.shuffle(filteredWorkoutList);

        return filteredWorkoutList;
    }

    protected long minToMills(long minute) {
        return TimeUnit.MINUTES.toMillis(minute);
    }

    protected long secToMills(long seconds) {
        return TimeUnit.SECONDS.toMillis(seconds);
    }
}
