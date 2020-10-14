package com.italianswapp.yourtraining;

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
import com.italianswapp.yourtraining.OfflineDatabase.WorkoutDatabase;
import com.italianswapp.yourtraining.OfflineDatabase.WorkoutSaved;
import com.italianswapp.yourtraining.WorkoutProposed.Workout.Workout;

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
    private ImageButton mLevelButton, mSensationButton;
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
        Level
         */

        mLevelButton = dialogView.findViewById(R.id.levelImageButtonSaveWorkoutDialog);
        mLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (workoutLevel) {
                    case BEGINNER:
                        mLevelButton.setImageResource(R.drawable.intermediate_switch);
                        workoutLevel = Workout.WorkoutLevel.INTERMEDIATE;
                        break;
                    case INTERMEDIATE:
                        mLevelButton.setImageResource(R.drawable.difficult_switch);
                        workoutLevel = Workout.WorkoutLevel.ADVANCED;
                        break;
                    case ADVANCED:
                        mLevelButton.setImageResource(R.drawable.beginner_switch);
                        workoutLevel = Workout.WorkoutLevel.BEGINNER;
                        break;
                }
            }
        });

        /*
        Sensation
         */
        mSensationButton = dialogView.findViewById(R.id.sensationImageButtonSaveWorkoutDialog);
        mSensationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (workoutSensation) {
                    case EASY:
                        mSensationButton.setImageResource(R.drawable.normal);
                        workoutSensation = WorkoutSaved.WorkoutSensation.NORMAL;
                        break;
                    case NORMAL:
                        mSensationButton.setImageResource(R.drawable.difficult);
                        workoutSensation = WorkoutSaved.WorkoutSensation.DIFFICULT;
                        break;
                    case DIFFICULT:
                        mSensationButton.setImageResource(R.drawable.easy);
                        workoutSensation = WorkoutSaved.WorkoutSensation.EASY;
                        break;
                }
            }
        });

        /*
        Gestisto pulsante salva
         */
        Button mSaveButtonDialog = dialogView.findViewById(R.id.saveButtonSaveWorkoutDialog);
        mSaveButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        /*
        Gestisco pulsante esci, che seplicemente chiude il dialog
         */
        Button mExitButtonDialog = dialogView.findViewById(R.id.exitButtonSaveWorkoutDialog);
        mExitButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
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

        WorkoutDatabase db = WorkoutDatabase.getDatabase(getApplicationContext());
        db.workoutDao().insertAll(workoutSaved);

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
