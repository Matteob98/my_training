package com.italianswapp.yourtraining3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.italianswapp.yourtraining3.OfflineDatabase.WorkoutDatabase;
import com.italianswapp.yourtraining3.OfflineDatabase.WorkoutSaved;
import com.italianswapp.yourtraining3.WorkoutProposed.Workout.Workout;

import java.util.Calendar;

public class FinishActivity extends AppCompatActivity {

    private final static String TEXT_FINISH = "text_finish";
    private final static String WORKOUT_FINISH = "workout_finish";
    private static final String WORKOUT_TYPE = "workout_type";
    private static final String WORKOUT_DURATION = "workout_duration";

    private FloatingActionButton mHomeButton, mSaveButton;
    private TextView mFinishTextView;
    private String text;

    /*
    Oggetti per il dialog per salvare gli allenamenti
     */

    private String workoutName;
    private String workoutDuration;
    private WorkoutSaved.WorkoutSensation workoutSensation;
    private WorkoutSaved.WorkoutType workoutType;
    private Workout.WorkoutLevel workoutLevel;
    private Workout workout;
    private ImageButton mEasySensationButton, mNormalSensationButton, mDifficultSensationButton,
            mBeginnerLevelButton, mIntermediateLevelButton, mAdvancedLevelButton;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        mFinishTextView = findViewById(R.id.finishTextView);
        mHomeButton = findViewById(R.id.homeButtonFinishActivity);
        mSaveButton = findViewById(R.id.saveButtonFinishActivity);

        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { goToHome(); }
        });
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWorkout(v);
            }
        });

        text = getIntent().getStringExtra(TEXT_FINISH);
        workout = getIntent().getParcelableExtra(WORKOUT_FINISH);
        workoutType = WorkoutSaved.WorkoutType.valueOf(
                getIntent().getStringExtra(WORKOUT_TYPE));
        workoutDuration = getIntent().getStringExtra(WORKOUT_DURATION);

        mFinishTextView.setText(text);

    }

    private void saveWorkout(View v) {
        /*
        Inizializzo le variabili
         */
        workoutSensation = WorkoutSaved.WorkoutSensation.EASY;
        workoutLevel = Workout.WorkoutLevel.BEGINNER;
        workoutName = "";

        /*
        Creo il builder del dialog
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.save_workout_dialog, null);
        builder.setView(dialogView);

        /*
        Imposto il cambiamento del nome dell'allenamento
         */
        final EditText mTitle = dialogView.findViewById(R.id.workoutTitleSaveWorkoutDialog);
        /*
        Ogni volta che cambia il testo modifico workoutName
         */
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                workoutName = mTitle.getText().toString();
            }
        });

        /*
        Richiamo gli oggetti sensazione
         */
        mEasySensationButton = dialogView.findViewById(R.id.easySensationButton);
        mNormalSensationButton = dialogView.findViewById(R.id.normalSensationButton);
        mDifficultSensationButton = dialogView.findViewById(R.id.difficultSensationButton);
        setSmileImage(true, false, false);

        /*
        Imposto l'onClick delle sensazioni
         */
        mEasySensationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workoutSensation = WorkoutSaved.WorkoutSensation.EASY;
                setSmileImage(true, false, false);
            }
        });
        mNormalSensationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workoutSensation = WorkoutSaved.WorkoutSensation.NORMAL;
                setSmileImage(true, true, false);
            }
        });
        mDifficultSensationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workoutSensation = WorkoutSaved.WorkoutSensation.DIFFICULT;
                setSmileImage(true, true, true);
            }
        });

        /*
        Richimo gli oggetti livello
         */

        mBeginnerLevelButton = dialogView.findViewById(R.id.beginnerLevelButton);
        mIntermediateLevelButton = dialogView.findViewById(R.id.intermediateLevelButton);
        mAdvancedLevelButton = dialogView.findViewById(R.id.advancedLevelButton);
        setLevelImage(true, false, false);

        /*
        Imposto l'onClick dei livelli
         */
        mBeginnerLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workoutLevel = Workout.WorkoutLevel.BEGINNER;
                setLevelImage(true, false, false);
            }
        });
        mIntermediateLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workoutLevel = Workout.WorkoutLevel.INTERMEDIATE;
                setLevelImage(true, true, false);
            }
        });
        mAdvancedLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workoutLevel = Workout.WorkoutLevel.ADVANCED;
                setLevelImage(true, true, true);
            }
        });

        Button mSaveButtonDialog = dialogView.findViewById(R.id.saveButtonSaveWorkoutDialog);
        mSaveButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    /**
     * Imposta l'immagine dei button del livello in base ai valori passati. Se true imposta il
     * selezionato, altrimenti imposta il deselezionato
     * @param first Livello principiante
     * @param second Livello intermedio
     * @param third Livello avanzato
     */
    private void setLevelImage(boolean first, boolean second, boolean third) {
        if(first)
            mBeginnerLevelButton.setImageResource(R.drawable.ic_level_filled);
        else
            mBeginnerLevelButton.setImageResource(R.drawable.ic_level_empty);
        if(second)
            mIntermediateLevelButton.setImageResource(R.drawable.ic_level_filled);
        else
            mIntermediateLevelButton.setImageResource(R.drawable.ic_level_empty);
        if(third)
            mAdvancedLevelButton.setImageResource(R.drawable.ic_level_filled);
        else
            mAdvancedLevelButton.setImageResource(R.drawable.ic_level_empty);
    }

    /**
     * Imposta l'immagine dei button delle sensazioni in base ai valori passati, se true imposta
     * Il selezionato, se false imposta il deselezionato
     * @param first sensazione facile
     * @param second sensazione intermedia
     * @param third sensazione difficile
     */
    private void setSmileImage(boolean first, boolean second, boolean third) {
        if(first)
            mEasySensationButton.setImageResource(R.drawable.ic_smile_green);
        else
            mEasySensationButton.setImageResource(R.drawable.ic_smile);
        if(second)
            mNormalSensationButton.setImageResource(R.drawable.ic_smile_green);
        else
            mNormalSensationButton.setImageResource(R.drawable.ic_smile);
        if(third)
            mDifficultSensationButton.setImageResource(R.drawable.ic_smile_green);
        else
            mDifficultSensationButton.setImageResource(R.drawable.ic_smile);
    }

    private void goToHome() {
        finish();
    }

    @Override
    public void onBackPressed() { goToHome();}

    public void save() {

        /*
            Se il titolo del workout è vuoto lo imposto di default
         */

        if (workoutName.equals("")) {
            Calendar calendar = Calendar.getInstance();
            workoutName = getResources().getString(R.string.workout) + " " +
                    calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                    calendar.get(Calendar.MONTH) + "/" +
                    calendar.get(Calendar.YEAR);
        }

        WorkoutSaved workoutSaved = new WorkoutSaved();
        workoutSaved.setDate(Calendar.getInstance().getTime());
        workoutSaved.setSensation(workoutSensation);
        workoutSaved.setTime(workoutDuration);
        workoutSaved.setType(workoutType);
        workout.setLevel(workoutLevel);
        workout.setTitle(workoutName);
        workoutSaved.setWorkout(workout);

        WorkoutDatabase db = WorkoutDatabase.getInMemoryDatabase(getApplicationContext());
        db.workoutDao().insertAll(workoutSaved);


        Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
        goToHome();
    }

    /**
     * Ritorna un istanza di finish activity
     * @param context Il contesto
     * @param text Il testo che deve apparire in alto in finish activity
     * @param workout L'allenamento appena finito
     * @param workoutType Il tipo di allenamento
     * @param workoutDuration Una stringa contenente la durata dell'allenamento
     * @return
     */
    public static Intent getInstance (Context context, String text, Workout workout, WorkoutSaved.WorkoutType workoutType, String workoutDuration) {
        Intent intent = new Intent(context, FinishActivity.class);
        intent.putExtra(TEXT_FINISH,text);
        intent.putExtra(WORKOUT_FINISH, workout);
        intent.putExtra(WORKOUT_TYPE, workoutType.name());
        intent.putExtra(WORKOUT_DURATION, workoutDuration);
        return intent;
    }
}
