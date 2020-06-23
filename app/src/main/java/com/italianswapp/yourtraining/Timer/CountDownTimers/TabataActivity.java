package com.italianswapp.yourtraining.Timer.CountDownTimers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;


public class TabataActivity extends CountDownActivity {

    private static final String ROUND_KEY="roundTabata";
    private static final String RBR_KEY="rbrTabata";

    private int round, currentRound;
    private long restBetweenRound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timer = createTimer();

        startButtonCreator();

        /*
        Le imposto momentaneamente come invisibili
        Dopo il ready tornano visibili
         */
        mPrimaryTextView.setVisibility(TextView.INVISIBLE);
        mSecondaryTextView.setVisibility(TextView.INVISIBLE);
        mOverlineSecondaryTextView.setVisibility(TextView.INVISIBLE);

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

                if(!isWork) {
                    //se era un riposo
                    if(currentSet < setsNumber) {
                        //Se ci sono altri set da fare
                        currentSet++;
                        startWork();
                    }
                    else if (currentRound < round) {
                        /*
                        Imposto il set corrente a 0
                        Al prossimo giro entrerà nell'if del !isWork e imposterà il currentSet a 1
                         */
                        currentSet=0;
                        currentRound++;
                        remainingTime =restBetweenRound;
                        currentDuration = restBetweenRound;

                        timer = createTimer();
                        timer.start();
                        restSound();
                        isRunning = true;
                        isWork=false;
                        mSecondaryTextView.setText(setSetsText(round, currentRound));
                        restLayoutSettings();
                        tick1000=true; tick2000=true; tick3000=true; tickHalf=true;
                    }
                    else {
                        //Se ho finito il tabata
                        finishCountDown();
                    }

                }
                else if (isWork) {
                    // se era un work fa partire il recupero
                    startRest();
                }
            }
        };
    }

    @Override
    protected void initializesTimer() {
        work = getIntent().getLongExtra(WORK_KEY, 0L);
        rest = getIntent().getLongExtra(REST_KEY, 0L);
        setsNumber = getIntent().getIntExtra(SETS_KEY, 1);

        round = getIntent().getIntExtra(ROUND_KEY, 1);
        restBetweenRound = getIntent().getLongExtra(RBR_KEY, 0L);

        remainingTime =work;
        currentDuration = work;
        currentRound=1;
        isWork=true;
        setTimeText();

        mPrimaryTextView.setText(setSetsText(setsNumber, currentSet));
        if(round>1) {
            mSecondaryTextView.setText(setSetsText(round, currentRound));
            mSecondaryTextView.setVisibility(TextView.VISIBLE);
            mOverlineSecondaryTextView.setVisibility(TextView.VISIBLE);
        }

        mPrimaryTextView.setVisibility(TextView.VISIBLE);

    }

    public static Intent getInstance(Context context, long work, long rest,int sets, int round, long restBetweenSets)
    {
        Intent intent = new Intent(context, TabataActivity.class);

        intent.putExtra(SETS_KEY, sets);
        intent.putExtra(WORK_KEY,work);
        intent.putExtra(REST_KEY, rest);
        intent.putExtra(ROUND_KEY,round);
        intent.putExtra(RBR_KEY, restBetweenSets);
        return intent;

    }
}
