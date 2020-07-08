package com.italianswapp.yourtraining.Timer.Circuit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.italianswapp.yourtraining.R;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.ExerciseSettings;
import com.italianswapp.yourtraining.Timer.CountDownTimers.CountDownActivity;

import java.util.ArrayList;
import java.util.Objects;

public class CircuitActivity extends CountDownActivity {

    private final static String CIRCUIT_KEY="circuitKey";

    /*
    Lista degli esercizi del circuito
     */
    private ArrayList<ExerciseSettings> exercises;
    /**
     * Vero se quello corrente è un esercizio a ripetizioni
     * Falso altrimenti
     */
    private boolean isRepsExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mOverlinePrimaryTextView.setText(getResources().getString(R.string.this_exercise));
        mOverlineSecondaryTextView.setText(getResources().getString(R.string.next_exercise));

        exercises = new ArrayList<>();
        currentSet=0; //Indice della lista exercise
        ArrayList<ExerciseSettings> exerciseSettings = getIntent().getParcelableArrayListExtra(CIRCUIT_KEY);

        /*
        Impostazioni pulsante skip esercizio/recupero
         */
        mRightFloatingButton.setVisibility(FloatingActionButton.VISIBLE);
        mRightFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightButtonClick();
            }
        });

        mLeftFloatingButton.setVisibility(FloatingActionButton.VISIBLE);
        mLeftFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callExerciseListDialog();
            }
        });

        /*
        Aggiungo tutti gli esercizi alla lista
        Se un esercizio ha più ripetizioni lo aggiungo più volte
         */
        for ( ExerciseSettings e : Objects.requireNonNull(exerciseSettings)) {
            e.setName(e.getName().substring(0, 1).toUpperCase() + e.getName().substring(1));
            if(e.getRepetition()==1)
                exercises.add(e);
            else {
                String oldName = e.getName();
                for (int i = 0; i < e.getRepetition(); i++) {
                    e.setName(oldName + " " + (i+1) + "/" + e.getRepetition());
                    exercises.add(ExerciseSettings.copyOf(e)); //Altrimenti aggiungerei il riferimento allo stesso oggetto
                }
            }

        }
        startButtonCreator(); //Metodo sovrascritto da questa classe

        /*
        Rendo momentaneamente non visibili e disabilitati
        Saranno riabilitati dopo il ready
         */
        mRightFloatingButton.setEnabled(false);
        mRightFloatingButton.setImageResource(R.drawable.ic_forward_scuro);
        mLeftFloatingButton.setEnabled(false);
        mLeftFloatingButton.setImageResource(R.drawable.ic_assignament_scuro);
        mPrimaryTextView.setVisibility(TextView.INVISIBLE);
        mSecondaryTextView.setVisibility(TextView.INVISIBLE);

    }

    @Override
    protected void initializesTimer() {
        mRightFloatingButton.setEnabled(true);
        mRightFloatingButton.setImageResource(R.drawable.ic_forward);
        mLeftFloatingButton.setEnabled(true);
        mLeftFloatingButton.setImageResource(R.drawable.ic_assignament);
        mPrimaryTextView.setVisibility(TextView.VISIBLE);
        mSecondaryTextView.setVisibility(TextView.VISIBLE);
        nextExercise();
    }

    @Override
    protected CountDownTimer createTimer() {
        return new CountDownTimer(remainingTime, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                tickManagement(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                if (currentSet >= exercises.size())
                    // Se ho finito il circuito
                    finishCountDown();

                else if(isWork && exercises.get(currentSet).isHasRecs()) {
                    startRest();
                }
                else if (currentSet < exercises.size()) {
                    // Se ci sono altri elementi
                    mStartButton.setText(getResources().getString(R.string.pause).toUpperCase());
                    mStartButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    nextExercise();
                    startWork();
                }
            }
        };
    }

    /**
     * Prende l'esercizio corrente e imposta:
     *  - Se è un esercizio a tempo o a ripetizioni
     *  - Il tempo o le ripetizioni
     *  - Il recupero
     *  Si occupa anche di incrementare currentSet
     */
    @SuppressLint("SetTextI18n")
    private void nextExercise() {
        isRepsExercise=exercises.get(currentSet).isReps();

        if (isRepsExercise) {
            work = exercises.get(currentSet).getReps();
            mTimeTextView.setText(work + " " + getResources().getString(R.string.reps));
            mProgressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setProgress(0);
                    mProgressBar.setProgress(maxProgress);
                }
            }, 10);

            mStartButton.setText(res.getString(R.string.finish).toUpperCase());
            mStartButton.setBackgroundTintList(ColorStateList.valueOf(res.getColor(R.color.colorAccent)));
        }
        else {
            work = exercises.get(currentSet).getReps() * 1000;
            remainingTime =work;
            currentDuration = work;
        }

        mPrimaryTextView.setText(exercises.get(currentSet).getName());
        if (currentSet < exercises.size() - 1)
            mSecondaryTextView.setText(exercises.get(currentSet+1).getName());
        else
            mSecondaryTextView.setText(getResources().getString(R.string.finish));
        rest = exercises.get(currentSet).getRec();
        isWork=true;
        currentSet++;
    }

    @Override
    protected void startWork() {
        progressBarHandler.post(progressBarRun);

        if (! isRepsExercise) {
            remainingTime =work;
            currentDuration = work;
            timer = createTimer();
            timer.start();
            isRunning = true;
            isWork=true;
            mStartButton.setText(res.getString(R.string.pause).toUpperCase());
            mStartButton.setBackgroundTintList(ColorStateList.valueOf(res.getColor(R.color.red)));
        }

        workSound();
        workLayoutSettings();
        tick1000=true; tick2000=true; tick3000=true; tickHalf=true;
    }

    /**
     * Crea i listener dei pulsanti del cronometro
     */
    protected void startButtonCreator() {
        mStartButton.setOnClickListener(this.clickableTimer());
        mTimeTextView.setOnClickListener(this.clickableTimer());
    }

    /**
     * Setta le operazioni da effettuare quando si preme sul pulsante di start o sul testo del cornometro
     * @return L'evento al click sul timer
     */
    private View.OnClickListener clickableTimer() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isRepsExercise && isWork) {
                        if (currentSet < exercises.size()) {
                            mStartButton.setText(res.getString(R.string.pause).toUpperCase());
                            mStartButton.setBackgroundTintList(ColorStateList.valueOf(res.getColor(R.color.red)));
                            if (exercises.get(currentSet).isHasRecs())
                                startRest();
                            else{
                                // Se ci sono altri elementi
                                nextExercise();
                                startWork();
                            }
                        }
                        else
                            finishCountDown();
                }
                else {
                    if (!isRunning) {
                        if (isFirstStart) {
                            timer = readyTimer();
                            timer.start();
                        }
                        else {
                            timer = createTimer();
                            timer.start();
                            isRunning = true;
                            mStartButton.setText(res.getString(R.string.pause).toUpperCase());
                            mStartButton.setBackgroundTintList(ColorStateList.valueOf(res.getColor(R.color.red)));
                        }
                    }
                    else {
                        timer.cancel();
                        isRunning = false;
                        mStartButton.setText(res.getString(R.string.start).toUpperCase());
                        mStartButton.setBackgroundTintList(ColorStateList.valueOf(res.getColor(R.color.colorPrimary)));
                    }
                }
            }

        };
    }

    /**
     * Gestisce il floating button di destra
     */
    private void rightButtonClick() {
        if(!isRepsExercise || !isWork)
            timer.cancel(); //Annullo il timer

        if (isRepsExercise && isWork) {
            if (currentSet < exercises.size()) {
                mStartButton.setText(res.getString(R.string.pause).toUpperCase());
                mStartButton.setBackgroundTintList(ColorStateList.valueOf(res.getColor(R.color.red)));
                if (exercises.get(currentSet).isHasRecs())
                    startRest();
                else {
                    // Se ci sono altri elementi
                    nextExercise();
                    startWork();
                }
            }
            else
                finishCountDown();
        }
        else if (!isRepsExercise && isWork) {
            if (currentSet >= exercises.size())
                // Se ho finito il circuito
                finishCountDown();

            else if(exercises.get(currentSet).isHasRecs()) {
                startRest();
            }
            else if (currentSet < exercises.size()) {
                // Se ci sono altri elementi
                mStartButton.setText(res.getString(R.string.pause).toUpperCase());
                mStartButton.setBackgroundTintList(ColorStateList.valueOf(res.getColor(R.color.red)));
                nextExercise();
                startWork();
            }
        }
        else {
            //Se era un riposo
            if (currentSet < exercises.size()) {
                // Se ci sono altri elementi
                mStartButton.setText(res.getString(R.string.pause).toUpperCase());
                mStartButton.setBackgroundTintList(ColorStateList.valueOf(res.getColor(R.color.red)));
                nextExercise();
                startWork();
            }
            else
                finishCountDown();
        }
    }

    protected void callExerciseListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.exercise_list_dialog, null);
        builder.setView(dialogView);

        ImageButton mCloseButton= dialogView.findViewById(R.id.closeButtonExerciseList);
        ListView exerciseListView = dialogView.findViewById(R.id.exerciseListView);

        String[] dialogExerciseList = new String[exercises.size()];
        for(int i=0; i<exercises.size(); i++)
            dialogExerciseList[i] =
                    exercises.get(i).getName() +
                     "          " +
                    exercises.get(i).getReps() +
                    (exercises.get(i).isReps() ? " " + getResources().getString(R.string.reps)
                            : " "+ getResources().getString(R.string.secs));


        ArrayAdapter<String> adapterExerciseList = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, dialogExerciseList);
        exerciseListView.setAdapter(adapterExerciseList);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    public static Intent getCircuitActivity(Context context, ArrayList<ExerciseSettings> exercises) {
        Intent intent = new Intent(context, CircuitActivity.class);
        intent.putExtra(CIRCUIT_KEY, exercises);

        return intent;
    }

}
