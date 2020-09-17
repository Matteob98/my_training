package com.italianswapp.yourtraining.Timer.CountDownTimers;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.italianswapp.yourtraining.DialogBuilders.Dialog1PickerBuilder;
import com.italianswapp.yourtraining.R;

public class AmrapActivity extends CountDownActivity {

    /**
     * As many round as possible
     * Conta i round completati
     */
    private int roundAmrap;

    private final static int maxRound=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRightFloatingButton.setVisibility(AppCompatButton.VISIBLE);
        mLeftFloatingButton.setVisibility(AppCompatButton.VISIBLE);
        mOverlinePrimaryTextView.setVisibility(TextView.INVISIBLE);

        roundAmrap=0;

        timer = createTimer();

        startButtonCreator();
        mPrimaryTextView.setText(getRoundString());

        mRightFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roundAmrap<maxRound)
                    roundAmrapManagement(1);
            }
        });
        mLeftFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roundAmrap>0)
                    roundAmrapManagement(-1);

            }
        });

        mRightFloatingButton.setImageResource(R.drawable.ic_add);
        mLeftFloatingButton.setImageResource(R.drawable.ic_minus);

        mSecondaryTextView.setVisibility(TextView.INVISIBLE);
        mOverlineSecondaryTextView.setVisibility(TextView.INVISIBLE);

    }

    private String getRoundString() { return res.getString(R.string.round_completed) + " "+ roundAmrap; }

    /**
     * Metodo sovrascritto per poter mostrare la scritta "Round completati"
     */
    @Override
    protected void startWork() {
        remainingTime =work;
        currentDuration = work;
        timer = createTimer();
        timer.start();
        isRunning = true;
        isWork=true;
        //mPrimaryTextView.setText(setSetsText(setsNumber, currentSet));
        mPrimaryTextView.setText(getRoundString());
        progressBarHandler.post(progressBarRun);
        workLayoutSettings();

        workSound();
        tick1000=true; tick2000=true; tick3000=true; tickHalf=true;
    }


    /**
     * Si occupa di gestire il cambiamento di round amrap
     * Aggiunge la variazione passata in input (se negativa sottrae)
     * E produce, se ifSound, un tic
     * @param delta La variazione
     */
    private void roundAmrapManagement(int delta) {
        roundAmrap=roundAmrap+delta;
        mPrimaryTextView.setText(getRoundString());
        if(isIfSound())
            MediaPlayer.create(this, R.raw.tic).start();
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
                    currentSet++;
                    startWork();
                    mPrimaryTextView.setText(getRoundString());
                }
                else if (currentSet<setsNumber) {
                    //Se era un lavoro e se non ho finito i sets da fare
                    startRest();
                    mPrimaryTextView.setText(getRoundString());
                }
                else {
                   finishCountDown(); //Metodo sovrascritto da questa classe
                }

            }
        };
    }

    @Override
    protected void initializesTimer() {
        work = getIntent().getLongExtra(WORK_KEY, 0L);
        rest = getIntent().getLongExtra(REST_KEY, 0L);
        setsNumber = getIntent().getIntExtra(SETS_KEY, 0);

        remainingTime =work;
        currentDuration = work;
        isWork=true;
        setTimeText();

        /*
        Se devo fare più sets di AMRAP imposto la seconda text view
        Altrimenti la rendo invisibile
         */
        if(setsNumber>1) {
            mSecondaryTextView.setText(setSetsText(setsNumber, currentSet));
            mSecondaryTextView.setVisibility(TextView.VISIBLE);
            mOverlineSecondaryTextView.setVisibility(TextView.VISIBLE);
        }
        else {
            mSecondaryTextView.setVisibility(TextView.INVISIBLE);
            mOverlineSecondaryTextView.setVisibility(TextView.INVISIBLE);
        }

        mPrimaryTextView.setText(getRoundString());
    }

    /**
     * Sovrascrive il metodo della superclasse
     * Inserisce un dialog in cui poter modificare il numero di round completati
     */
    @Override
    protected void finishCountDown() {
        if(isIfSound())
            MediaPlayer.create(this, R.raw.timer_sound).start();

        Dialog1PickerBuilder.newBuilder(this, this)
                .setText(0, res.getString(R.string.many_round_completed))
                .setPicker(0, maxRound, new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        roundAmrap=newVal;
                    }
                })
                .setPickerValue(roundAmrap)
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishCountDown(res.getString(R.string.round_completed) + ": " + roundAmrap);
                        Dialog1PickerBuilder.getAlertDialog().dismiss();
                    }
                })
                .negativeButtonDisabled()
                .setCanExitFromTheDialog(false)
                .show();
    }

    public static Intent getInstance(Context context, long work, int sets, long rest) {
        Intent intent = new Intent(context, AmrapActivity.class);

        intent.putExtra(WORK_KEY, work);
        intent.putExtra(SETS_KEY, sets);
        intent.putExtra(REST_KEY, rest);
        return intent;
    }
}
