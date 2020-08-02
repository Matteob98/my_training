package com.italianswapp.yourtraining.Timer.CountDownTimers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.italianswapp.yourtraining.R;

public class NegativePhaseActivity extends CountDownActivity {

    private static final String REPETITION_KEY="repetition";
    private static final String NEGATIVE_KEY="negative";
    private static final String POSITIVE_KEY="positive";

    private int repetition, currentRepetition;
    private long negative;
    /**
     * Prende il valore della fase positiva/negativa, ogni secondo fa suonare il bip
     * in tickManagement()
     */
    private long tickNegativePhase;
    private boolean isPositive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timer = createTimer();
        startButtonCreator();

        /*
        Metto tutto invisible, saranno rese nuovamente visibili dopo il ready
         */
        mPrimaryTextView.setVisibility(TextView.INVISIBLE);
        mSecondaryTextView.setVisibility(TextView.INVISIBLE);
        mOverlineSecondaryTextView.setText(getResources().getString(R.string.current_repetition));
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
                    //Se era un riposo
                    tickNegativePhase=work;
                    currentSet++;
                    currentRepetition=1;
                    startWork();
                    mWorkDescriptionTextView.setText(res.getString(R.string.Concentric));
                }
                else if (isPositive) {
                    //Se era la fase positiva inizia la fase negativa
                    tickNegativePhase=negative;
                    startNegativeWork();
                    mWorkDescriptionTextView.setText(res.getString(R.string.Eccentric));
                }
                else if (!isPositive && currentRepetition < repetition) {
                    //Se era una fase negativa e devo fare altre ripetizioni in questa serie
                    tickNegativePhase=work;
                    isPositive=true;
                    currentRepetition++;
                    mSecondaryTextView.setText(setSetsText(repetition, currentRepetition));
                    startWork();
                    mWorkDescriptionTextView.setText(res.getString(R.string.Concentric));

                }
                else if (currentSet < setsNumber) {
                    //Se era la fase negativa
                    startRest();
                }
                else {
                    finishCountDown();
                }
            }
        };
    }

    @Override
    protected void initializesTimer() {
        repetition = getIntent().getIntExtra(REPETITION_KEY, 1);
        work = getIntent().getLongExtra(POSITIVE_KEY, 0L);
        negative = getIntent().getLongExtra(NEGATIVE_KEY, 0L);
        rest = getIntent().getLongExtra(REST_KEY, 0L);
        setsNumber = getIntent().getIntExtra(SETS_KEY, 0);

        remainingTime =work;
        currentDuration = work;
        tickNegativePhase=work;
        isPositive=true;
        isWork=true;
        setTimeText();
        currentRepetition=1;

        mPrimaryTextView.setText(setSetsText(setsNumber, currentSet));
        mSecondaryTextView.setText(setSetsText(repetition, currentRepetition));

        mPrimaryTextView.setVisibility(TextView.VISIBLE);
        mSecondaryTextView.setVisibility(TextView.VISIBLE);
    }

    @Override
    protected void tickManagement(long millisUntilFinished) {

        if (isWork) {
            // Se ho raggiunto il secondo in cui fare il tick (ogni secondo)
            if (millisUntilFinished <= tickNegativePhase) {
                tickNegativePhase -= 1000; //scalo di un secondo
                if(isIfSound()) //Se devo suonare suono
                    tickSecondSound();
            }

            boolean sound = isIfSound(); //Mi salvo il valore di ifSound
            setIfSound(false);  //Imposto che non deve suonare

            super.tickManagement(millisUntilFinished); //Chiamo il padre di tickManagement per gestire le operazioni

            setIfSound(sound); //reimposto if sound
        }
        else
            super.tickManagement(millisUntilFinished); //Se è un recupero faccio il normale tickmanagement
    }

    private void startNegativeWork() {
        remainingTime =negative;
        currentDuration = negative;
        timer = createTimer();
        timer.start();
        mTimeTextView.setTextColor(getResources().getColor(R.color.red));
        isRunning = true;
        isWork=true;
        isPositive=false;
        workSound();
    }

    public static Intent getInstance(Context context,int repetition, long positive, long negative, int sets, long rest) {
        Intent intent = new Intent(context, NegativePhaseActivity.class);

        intent.putExtra(REPETITION_KEY, repetition);
        intent.putExtra(POSITIVE_KEY, positive);
        intent.putExtra(NEGATIVE_KEY, negative);
        intent.putExtra(SETS_KEY, sets);
        intent.putExtra(REST_KEY, rest);

        return intent;
    }


}
