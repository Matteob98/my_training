package com.italianswapp.yourtraining.Timer.Circuit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.italianswapp.yourtraining.ExerciseTypeNotCorrectException;
import com.italianswapp.yourtraining.R;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.Exercise;
import com.italianswapp.yourtraining.Timer.CountDownTimers.CountDownActivity;
import java.util.ArrayList;
import java.util.Objects;

public class CircuitActivity extends CountDownActivity {

    private final static String CIRCUIT_KEY="circuitKey";

    private View mPrimaryView, mSecondaryView;
    private TextView mPrimarySets,mSecondarySets;
    private ImageButton mRepsButton;

    /*
    Lista di esercizi che mi viene passata
     */

    ArrayList<Exercise> exerciseSettings;

    /*
    Lista degli esercizi del circuito elaborata
     */
    private ArrayList<Exercise> exercises;
    /**
     * Vero se quello corrente è un esercizio a ripetizioni
     * Falso altrimenti
     */
    private boolean isRepsExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrimarySets = findViewById(R.id.primarySetsTextView);
        mSecondarySets = findViewById(R.id.secondarySetsTextView);
        mRepsButton = findViewById(R.id.repsButtonCountDownActivity);

        mOverlinePrimaryTextView.setText(getResources().getString(R.string.this_exercise));
        mOverlineSecondaryTextView.setText(getResources().getString(R.string.next_exercise));

        exercises = new ArrayList<>();
        currentSet=0; //Indice della lista exercise
        exerciseSettings = getIntent().getParcelableArrayListExtra(CIRCUIT_KEY);

        /*
        Aggiungo tutti gli esercizi alla lista
        Se un esercizio ha più ripetizioni lo aggiungo più volte
         */
        try {
            initializesExerciseList(exerciseSettings);
        } catch (ExerciseTypeNotCorrectException e) {
            e.printStackTrace();
        }
        initializesColoredView();

        startButtonCreator(); //Metodo sovrascritto da questa classe

        initializesFloatingButton();

        //initializesTimer();


    }

    /**
     *
     */
    private void initializesFloatingButton() {
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
        mRightFloatingButton.setImageResource(R.drawable.ic_forward);



        mLeftFloatingButton.setVisibility(FloatingActionButton.VISIBLE);
        mLeftFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callExerciseListDialog();
            }
        });
        mLeftFloatingButton.setImageResource(R.drawable.ic_assignament);

        /*
        Rendo momentaneamente non visibili e disabilitati
        Saranno riabilitati dopo il ready
         */
        mRightFloatingButton.setEnabled(false);

    }

    private void initializesExerciseList( ArrayList<Exercise> exerciseSettings) throws ExerciseTypeNotCorrectException {
        for ( Exercise e : Objects.requireNonNull(exerciseSettings)) {
            e.setName(e.getName().substring(0, 1).toUpperCase() + e.getName().substring(1));

            if (e.getType() != Exercise.CircuitType.SUPERSET) {
                if(e.getRepetition()==1)
                    exercises.add(Exercise.copyOf(e));
                else {
                    for (int i = 0; i < e.getRepetition(); i++) {
                        e.setHasSets(true);
                        e.setNumberSets(i+1);
                        e.setTotalSets(e.getRepetition());
                        exercises.add(Exercise.copyOf(e)); //Altrimenti aggiungerei il riferimento allo stesso oggetto
                    }
                }
            }
            else {
                /*
                Mi prendo l'esercizio in superset
                 */
                Exercise supersetExercise = null;
                Exercise.SupersetExercise supersetExercisePassed = null;
                supersetExercisePassed = e.getSupersetExercise();

                Log.d("EccPass", "Ho passato l'eccezione");

                supersetExercise = new Exercise(
                        supersetExercisePassed.getName(),
                        supersetExercisePassed.getReps(),
                        e.getRec(), //il recupero è quello del primo esercizio
                        1,
                        supersetExercisePassed.isReps(),
                        e.isHasRecs(),
                        e.getType());

                /*
                Il primo esercizio non ha recupero, altrimenti non sarebbe in superserie
                 */
                e.setRec(0);
                e.setHasRecs(false);

                if(e.getRepetition()==1) {
                    exercises.add(Exercise.copyOf(e));/*rec =0 */
                    exercises.add(Exercise.copyOf(supersetExercise));
                }
                else {
                    String secondOldName = supersetExercise.getName();

                    for (int i=0; i<e.getRepetition(); i++) {
                        e.setHasSets(true);
                        e.setNumberSets(i+1);
                        e.setTotalSets(e.getRepetition());
                        supersetExercise.setName(secondOldName);

                        exercises.add(Exercise.copyOf(e)); //Altrimenti aggiungerei il riferimento allo stesso oggetto
                        exercises.add(Exercise.copyOf(supersetExercise));
                    }
                }
            }


        }
    }

    /**
     *
     */
    private void initializesColoredView() {
        mPrimaryView = findViewById(R.id.primaryView);
        mSecondaryView = findViewById(R.id.secondaryView);
        mPrimaryView.setVisibility(View.VISIBLE);
        mSecondaryView.setVisibility(View.VISIBLE);

        mPrimaryView.setBackground(getLeftColoredView(exercises.get(0)));

        /*
        Imposto il nome dell'esercizio
         */
        mPrimaryTextView.setText(exercises.get(0).getName());

        /*
        Se è un esercizio con più ripetizioni le visualizzo
         */
        if(exercises.get(0).isHasSets()) {
            mPrimarySets.setVisibility(TextView.VISIBLE);
            mPrimarySets.setText(exercises.get(0).getNumberSets() + "/" + exercises.get(0).getTotalSets());
        }

        if(exercises.size()>1) {
            mSecondaryView.setBackground(getRightColoredView(exercises.get(1)));
            /*
            Imposto il nome dell'esercizio
             */
            mSecondaryTextView.setText(exercises.get(1).getName());

            /*
            Se è un esercizio con più ripetizioni le visualizzo
             */
            if(exercises.get(1).isHasSets()) {
                mSecondarySets.setVisibility(TextView.VISIBLE);
                mSecondarySets.setText(exercises.get(1).getNumberSets() + "/" + exercises.get(1).getTotalSets());
            }
        }
        else {
            mSecondaryTextView.setText(getResources().getString(R.string.finish));
            mSecondaryView.setBackground(res.getDrawable(R.drawable.bottom_left_corner_rest, null));
        }

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
                if(isWork && exercises.get(currentSet-1).isHasRecs()) {
                    startRest();
                }
                else if (currentSet >= exercises.size())
                    // Se ho finito il circuito
                    finishCountDown();
                else {
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
        /*
        Qui non si mette il -1 su currentSet perché sto usando effettivamente il currentSet che mi serve
         */
        mPrimarySets.setVisibility(TextView.GONE);
        mSecondarySets.setVisibility(TextView.GONE);

        Exercise exercise = exercises.get(currentSet);
        isRepsExercise=exercise.isReps();

        if (isRepsExercise) {
            workLayoutSettings();
            mRepsButton.setVisibility(ImageButton.VISIBLE);
            mStartButton.setVisibility(Button.INVISIBLE);
            work =exercise.getReps();
            mTimeTextView.setText(work + " " + getResources().getString(R.string.reps));
            mProgressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setProgress(0);
                    mProgressBar.setProgress(maxProgress);
                }
            }, 10);

        }
        else {
            mStartButton.setVisibility(Button.VISIBLE);
            mRepsButton.setVisibility(ImageButton.INVISIBLE);
            work = exercise.getReps();
            remainingTime =work;
            currentDuration = work;
        }

        /*
        Imposto il nome dell'esercizio
         */
        mPrimaryTextView.setText(exercise.getName());

        /*
        Coloro la view
         */
        mPrimaryView.setBackground(getLeftColoredView(exercise));

        /*
        Se è un esercizio con più ripetizioni le visualizzo
         */
        if(exercise.isHasSets()) {
            mPrimarySets.setVisibility(TextView.VISIBLE);
            mPrimarySets.setText(exercise.getNumberSets() + "/" + exercise.getTotalSets());
        }

        if (currentSet < exercises.size() - 1) {
            Exercise nextExercise = exercises.get(currentSet+1);
            /*
            Imposto il nome dell'esercizio
             */
            mSecondaryTextView.setText(nextExercise.getName());
            /*
             Coloro la view
             */
            mSecondaryView.setBackground(getRightColoredView(nextExercise));

            /*
            Se è un esercizio con più ripetizioni le visualizzo
             */
            if(nextExercise.isHasSets()) {
                mSecondarySets.setVisibility(TextView.VISIBLE);
                mSecondarySets.setText(nextExercise.getNumberSets() + "/" + nextExercise.getTotalSets());
            }
        }
        else {
            /*
            Se sono finiti gli esercizi
             */
            mSecondaryTextView.setText(getResources().getString(R.string.finish));
            mSecondaryView.setBackground(res.getDrawable(R.drawable.bottom_left_corner_rest, null));

        }
        rest = exercise.getRec();
        isWork=true;
        currentSet++;
    }




    @Override
    protected void startWork() {
        if (exercises.get(currentSet-1).getType().equals(Exercise.CircuitType.REST)) {
            startRest();
            return;
        }
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
        mRepsButton.setOnClickListener(this.clickableTimer());

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
                            mRepsButton.setVisibility(ImageButton.INVISIBLE);
                            mStartButton.setVisibility(Button.VISIBLE);
                            mStartButton.setBackgroundTintList(ColorStateList.valueOf(res.getColor(R.color.red)));
                            if (exercises.get(currentSet-1).isHasRecs())
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
                            mRepsButton.setVisibility(ImageButton.INVISIBLE);
                            mStartButton.setVisibility(Button.VISIBLE);
                            mStartButton.setText(res.getString(R.string.pause).toUpperCase());
                            mStartButton.setBackgroundTintList(ColorStateList.valueOf(res.getColor(R.color.red)));
                        }
                    }
                    else {
                        timer.cancel();
                        isRunning = false;
                        mRepsButton.setVisibility(ImageButton.INVISIBLE);
                        mStartButton.setVisibility(Button.VISIBLE);
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
            //Se è un riposo
            timer.cancel(); //Annullo il timer
        if(( !isRepsExercise) && (currentSet==0 || isFirstStart))
            return;

        else if (isRepsExercise && isWork) {
            if (currentSet < exercises.size()) {
                if (exercises.get(currentSet-1).isHasRecs()) {
                    mRepsButton.setVisibility(ImageButton.INVISIBLE);
                    mStartButton.setVisibility(Button.VISIBLE);
                    mStartButton.setText(res.getString(R.string.pause).toUpperCase());
                    mStartButton.setBackgroundTintList(ColorStateList.valueOf(res.getColor(R.color.red)));
                    startRest();
                }
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

            else if(exercises.get(currentSet-1).isHasRecs()) {
                mRepsButton.setVisibility(ImageButton.INVISIBLE);
                mStartButton.setVisibility(Button.VISIBLE);
                mStartButton.setText(res.getString(R.string.pause).toUpperCase());
                mStartButton.setBackgroundTintList(ColorStateList.valueOf(res.getColor(R.color.red)));
                startRest();
            }
            else if (currentSet < exercises.size()) {
                // Se ci sono altri elementi
                //mStartButton.setText(res.getString(R.string.pause).toUpperCase());
                //mStartButton.setBackgroundTintList(ColorStateList.valueOf(res.getColor(R.color.red)));
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

        //Carico dialog e animazioni
        final Dialog dialog = new Dialog(CircuitActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        dialog.setContentView(R.layout.exercise_list_dialog);

        //Setto il pulsante per uscire
        ImageButton mDeleteButton = dialog.findViewById(R.id.deleteImageButtonExerciseListDialog);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Carico i dati nella recycler View
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerViewExerciseListDialog);
        ExerciseCardRecyclerViewAdapter mAdapter = new ExerciseCardRecyclerViewAdapter(exerciseSettings);
        mAdapter.setCircuitCreatorActivity(null);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));


        dialog.show();

    }

    /**
     *
     * @param exercise
     * @return
     */
    private Drawable getLeftColoredView(Exercise exercise) {
        Drawable drawable;
        switch (exercise.getType()) {
            case EXERCISE:
                drawable = ResourcesCompat.getDrawable(res, R.drawable.bottom_right_corner_accent, null);
                break;
            case REST:
                drawable = ResourcesCompat.getDrawable(res, R.drawable.bottom_right_corner_rest, null);
                break;
            case SUPERSET:
                drawable = ResourcesCompat.getDrawable(res, R.drawable.bottom_right_corner_superset, null);
                break;
            case TABATA:
                drawable = ResourcesCompat.getDrawable(res, R.drawable.bottom_right_corner_tabata, null);
                break;
            case EMOM:
                drawable = ResourcesCompat.getDrawable(res, R.drawable.bottom_right_corner_emom, null);
                break;
            default:
                try {
                    throw new ExerciseTypeNotCorrectException("Tipo non corretto in getExerciseColor");
                } catch (ExerciseTypeNotCorrectException e) {
                    e.printStackTrace();
                }
                return null;
        }
        return drawable;
    }

    /**
     *
     * @param exercise
     * @return
     */
    private Drawable getRightColoredView(Exercise exercise) {
        Drawable drawable;
        switch (exercise.getType()) {
            case EXERCISE:
                drawable = ResourcesCompat.getDrawable(res, R.drawable.bottom_left_corner_accent, null);
                break;
            case REST:
                drawable = ResourcesCompat.getDrawable(res, R.drawable.bottom_left_corner_rest, null);
                break;
            case SUPERSET:
                drawable = ResourcesCompat.getDrawable(res, R.drawable.bottom_left_corner_superset, null);
                break;
            case TABATA:
                drawable = ResourcesCompat.getDrawable(res, R.drawable.bottom_left_corner_tabata, null);
                break;
            case EMOM:
                drawable = ResourcesCompat.getDrawable(res, R.drawable.bottom_left_corner_emom, null);
                break;
            default:
                try {
                    throw new ExerciseTypeNotCorrectException("Tipo non corretto in getExerciseColor");
                } catch (ExerciseTypeNotCorrectException e) {
                    e.printStackTrace();
                }
                return null;
        }
        return drawable;
    }

    public static Intent getCircuitActivity(Context context, ArrayList<Exercise> exercises) {
        Intent intent = new Intent(context, CircuitActivity.class);
        intent.putExtra(CIRCUIT_KEY, exercises);

        return intent;
    }

}
