package com.italianswapp.yourtraining.Timer;

import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.italianswapp.yourtraining.OfflineDatabase.WorkoutSaved;
import com.italianswapp.yourtraining.R;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.ExerciseBuilder;
import com.italianswapp.yourtraining.Timer.CountDownTimers.CountDownActivity;
import com.italianswapp.yourtraining.WorkoutProposed.Workout.Workout;
import com.italianswapp.yourtraining.WorkoutProposed.Workout.WorkoutBuilder;

public class TimerActivity extends CountDownActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLeftFloatingButton.setVisibility(AppCompatButton.VISIBLE);
        mRightFloatingButton.setVisibility(AppCompatButton.VISIBLE);
        mOverlinePrimaryTextView.setVisibility(TextView.GONE);
        mOverlineSecondaryTextView.setVisibility(TextView.GONE);
        mPrimaryTextView.setVisibility(TextView.GONE);
        mSecondaryTextView.setVisibility(TextView.GONE);
        mTimeFromStartTextView.setVisibility(TextView.INVISIBLE);

        timer = createTimer();

        startButtonCreator();

        mRightFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyTimer(-10000);
                progressBarHandler.post(progressBarRun);
            }
        });

        mLeftFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyTimer(10000);
                progressBarHandler.post(progressBarRun);
            }
        });

        /*
        Imposto momentaneamente come disabilitati
        Saranno riabilitati dopo il ready
         */
        mLeftFloatingButton.setEnabled(false);
        mRightFloatingButton.setImageResource(R.drawable.ic_forward10);
        mRightFloatingButton.setEnabled(false);
        mLeftFloatingButton.setImageResource(R.drawable.ic_replay);

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
                finishCountDown();
            }
        };
    }

    @Override
    protected void initializesTimer() {
        work = getIntent().getLongExtra(WORK_KEY, 0L);
        remainingTime =work;
        currentDuration = work;
        isWork=true;
        setTimeText();

        mLeftFloatingButton.setEnabled(true);
        mRightFloatingButton.setImageResource(R.drawable.ic_forward10);
        mRightFloatingButton.setEnabled(true);
        mLeftFloatingButton.setImageResource(R.drawable.ic_replay);
        mWorkDescriptionTextView.setVisibility(TextView.INVISIBLE);
    }

    @Override
    protected WorkoutSaved.WorkoutType getWorkoutType() {
        return WorkoutSaved.WorkoutType.TIMER;
    }

    @Override
    protected Workout getWorkout() {
        return WorkoutBuilder
                .newBuilder()
                .addExercise(
                        ExerciseBuilder
                                .newBuilder()
                                .setReps((int) work)
                                .setIsReps(false)
                                .setHasRecs(false)
                                .build())
                .build();
    }

    /**
     * Ritorna l'intent dell'activity Timer Activity
     * Passa come argomento i millisecondi a cui sarà preimpostato il timer
     * @param context Il contesto dove si opera
     * @param mills millisecondi del timer
     * @return Intent
     */
    public static Intent getIntentInstance(Context context, long mills)
    {
        Intent intent = new Intent(context, TimerActivity.class);
        intent.putExtra(WORK_KEY,mills);
        return intent;
    }

    /**
     * Modifica il timer in positivo o in negativo dei millisecondi passati in input
     * @param mills millisecondi da aggiungere/sottrarre (Se va sottratto, valore negativo)
     */
    private void modifyTimer(long mills) {
        remainingTime+=mills;
        if(remainingTime>currentDuration) {
            currentDuration = remainingTime;
            work = currentDuration;
        }

        //Se il timer è in azione, lo distrugge e lo ricrea con il nuovo valore
        if(isRunning){
            timer.cancel();
            timer = createTimer();
            timer.start();
        }
        else
            setTimeText();
    }
}
