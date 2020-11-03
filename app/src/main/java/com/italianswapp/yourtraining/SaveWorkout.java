package com.italianswapp.yourtraining;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;

import com.italianswapp.yourtraining.OfflineDatabase.WorkoutDatabase;
import com.italianswapp.yourtraining.OfflineDatabase.WorkoutSaved;
import com.italianswapp.yourtraining.WorkoutProposed.Workout.Workout;

import java.util.Calendar;

/**
 * Classe che grazie ai suoi metodi statici permette di salvare un workout
 */
public class SaveWorkout {

    private static Resources res;
    private static WorkoutSaved.WorkoutSensation workoutSensation;
    private static Workout.WorkoutLevel workoutLevel;
    private static String workoutName;
    private static String workoutDuration;
    private static WorkoutSaved.WorkoutType workoutType;
    private static Workout workout;

    private static AlertDialog alertDialog;

    public static void saveWorkout(final Activity activity, String workoutDuration, WorkoutSaved.WorkoutType workoutType, Workout workout) {

        setWorkoutDuration(workoutDuration);
        setWorkout(workout);
        setWorkoutType(workoutType);

        res = activity.getResources();

        /*
        Inizializzo le variabili
         */
        workoutSensation = WorkoutSaved.WorkoutSensation.EASY;
        workoutLevel = Workout.WorkoutLevel.BEGINNER;
        workoutName = "";

        /*
        Creo il builder del dialog
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
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

        final ImageButton mLevelButton = dialogView.findViewById(R.id.levelImageButtonSaveWorkoutDialog);
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
        final ImageButton mSensationButton = dialogView.findViewById(R.id.sensationImageButtonSaveWorkoutDialog);
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
                save(activity);
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

    private static void save(Activity activity) {

        /*
            Se il titolo del workout è vuoto lo imposto di default
         */

        if (workoutName.equals("")) {
            Calendar calendar = Calendar.getInstance();
            workoutName = res.getString(R.string.workout) + " " +
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

        WorkoutDatabase db = WorkoutDatabase.getDatabase(activity.getApplicationContext());
        db.workoutDao().insertAll(workoutSaved);

        alertDialog.dismiss();
        activity.finish();
    }

    private static void setWorkoutDuration(String workoutDuration) {
        SaveWorkout.workoutDuration = workoutDuration;
    }

    private static void setWorkoutType(WorkoutSaved.WorkoutType workoutType) {
        SaveWorkout.workoutType = workoutType;
    }

    private static void setWorkout(Workout workout) {
        SaveWorkout.workout = workout;
    }
}
