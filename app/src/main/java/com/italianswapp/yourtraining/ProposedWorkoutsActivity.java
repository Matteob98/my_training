package com.italianswapp.yourtraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.italianswapp.yourtraining.Timer.CountDownTimers.TabataActivity;

public class ProposedWorkoutsActivity extends AppCompatActivity {

    //todo 04/09/2020 sono rimasto qui
    //todo creare la card per visualizzare i workout e iniziare a caricare i workout nella recycler view

    private static final String CATEGORY = "CATEGORY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposed_workouts);

        String categoryPassed = getIntent().getStringExtra(CATEGORY);
        Toast.makeText(getApplicationContext(), categoryPassed, Toast.LENGTH_SHORT).show();
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