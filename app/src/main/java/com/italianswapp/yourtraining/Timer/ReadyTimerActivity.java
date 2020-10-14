package com.italianswapp.yourtraining.Timer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.italianswapp.yourtraining.R;

/**
 * Questa activity viene chiamata sempre dall'activity del timer che sta per partire,
 * attraverso il button l'utente può annullare l'inizio dell'allenamento, in quel caso sarà chiusa sia questa activity
 * che l'activity padre.
 * Al contrario, se il timer arriva al termine viene chiusa questa activity e inizia l'allenamento
 */
public class ReadyTimerActivity extends AppCompatActivity {

    private static Activity activity;

    private TextView mCountDownText;
    /**
     * Il pulsante se premuto fa finire questa activity e l'activity padre
     */
    private ImageButton mExitButton;

    /**
     * Pulsante per saltare i 3 secondi prima dell'allenamento
     */
    private Button mSkipButton;

    private static final long READY_TIMER_DURATION = 3000l;
    private static final long READY_TIMER_INTERVAL = 1000l;
    private int readyTimerValue = (int) (READY_TIMER_DURATION/READY_TIMER_INTERVAL);
    private boolean ifSound=true;

    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_timer);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark)); //Colora barra notifiche

        exitButtonSetup();

        skipButtonSetup();

        countDownSetup();

        timer.start();
    }

    /**
     * Imposta il comportamento del timer dei 3 secondi
     */
    private void countDownSetup() {
        mCountDownText = findViewById(R.id.countdownTextViewReadyActivity);
        mCountDownText.setText(String.valueOf(readyTimerValue));

        timer = new CountDownTimer(READY_TIMER_DURATION, READY_TIMER_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                readyTimerValue--;
                mCountDownText.setText(String.valueOf(readyTimerValue+1));
                tickSecondSound();
            }

            @Override
            public void onFinish() {
                workSound();
                finish();
            }
        };
    }

    /**
     * Richiama e imposta il comportamento del pulsante skip
     */
    private void skipButtonSetup() {
        mSkipButton = findViewById(R.id.skipButtonReadyTimer);

        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                finish();
            }
        });
    }

    /**
     * Richiama e imposta il comportamento del pulsante exit
     */
    private void exitButtonSetup() {
        mExitButton = findViewById(R.id.exitButtonReadyActivity);

        /*
        Se l'utente clicca sulla X chiude questa activity e anche quella dell'allenamento
        che stava per iniziare
         */
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                activity.finish();
                finish();
            }
        });
    }

    private void tickSecondSound() {
        if(ifSound)
            MediaPlayer.create(this, R.raw.second_tick).start(); }

    /**
     * Fa partire il suono del work
     */
    protected void workSound() {
        if(ifSound)
            MediaPlayer.create(this, R.raw.go_sound).start();
    }

    public static Intent getInstance (Context context, Activity activityFather) {
        activity = activityFather;
        return new Intent(context, ReadyTimerActivity.class);
    }
}