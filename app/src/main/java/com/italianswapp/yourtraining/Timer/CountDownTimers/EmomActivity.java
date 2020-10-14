package com.italianswapp.yourtraining.Timer.CountDownTimers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.italianswapp.yourtraining.OfflineDatabase.WorkoutSaved;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.ExerciseBuilder;
import com.italianswapp.yourtraining.WorkoutProposed.Workout.Workout;
import com.italianswapp.yourtraining.WorkoutProposed.Workout.WorkoutBuilder;

public class EmomActivity extends CountDownActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSecondaryTextView.setVisibility(TextView.INVISIBLE);
        mOverlineSecondaryTextView.setVisibility(TextView.INVISIBLE);

        timer = createTimer();

        startButtonCreator();
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
                if(currentSet<setsNumber) {
                    currentSet++;
                    startWork();
                }
                else {
                    finishCountDown();
                }
            }
        };
    }

    @Override
    protected void initializesTimer() {
        work = getIntent().getLongExtra(WORK_KEY, 0L);
        setsNumber = getIntent().getIntExtra(SETS_KEY, 1) ;

        remainingTime =work;
        currentDuration = work;
        currentSet=1;
        setTimeText();

        mPrimaryTextView.setText(setSetsText(setsNumber, currentSet));

        mPrimaryTextView.setVisibility(TextView.VISIBLE);
    }

    @Override
    protected WorkoutSaved.WorkoutType getWorkoutType() {
        return WorkoutSaved.WorkoutType.EMOM;
    }

    @Override
    protected Workout getWorkout() {
        return WorkoutBuilder
                .newBuilder()
                .addExercise(ExerciseBuilder
                        .newBuilder()
                        .setReps((int)work)
                        .setIsReps(false)
                        .setHasRecs(false)
                        .setTotalSets(setsNumber)
                        .build())
                .build();
    }


    public static Intent getInstance(Context context, long work, int sets)
    {
        Intent intent = new Intent(context, EmomActivity.class);

        intent.putExtra(WORK_KEY,work);
        intent.putExtra(SETS_KEY, sets);
        return intent;
    }
}
