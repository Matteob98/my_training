package com.italianswapp.yourtraining.WorkoutProposed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.italianswapp.yourtraining.R;
import com.italianswapp.yourtraining.Workout;
import com.italianswapp.yourtraining.WorkoutProposedType.BodybuildingWorkoutProposed;

import java.util.ArrayList;

public class ProposedWorkoutsActivity extends AppCompatActivity {

    private static final String CATEGORY = "CATEGORY";

    private RecyclerView mWorkoutProposedRecyclerView;
    private ArrayList<Workout> workoutList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposed_workouts);

        BodybuildingWorkoutProposed proposed = new BodybuildingWorkoutProposed();
        workoutList = proposed.getWorkoutList();
        mWorkoutProposedRecyclerView = findViewById(R.id.workoutProposedListRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mWorkoutProposedRecyclerView.setLayoutManager(linearLayoutManager);

        mWorkoutProposedRecyclerView.setAdapter(new WorkoutSummaryRecyclerViewAdapter(workoutList, this));

        /*
        Todo sono qui al 07/09/2020
        Ho implementato tutte le recyclerView ma non viene visualizzato nulla
        Controllare passo passo
         */
    }

    /**
     * Restituisce un istanza della classe passando la categoria di allenament proposto che l'utente desidera
     * @param context il contesto in cui ci si trova
     * @param workoutCategory la categoria di allenamento
     * @return l'Intent della classe
     */
    public static Intent getInstance(Context context, Workout.WorkoutCategory workoutCategory)
    {
        Intent intent = new Intent(context, ProposedWorkoutsActivity.class);

        intent.putExtra(CATEGORY, workoutCategory.toString());
        return intent;


    }
}