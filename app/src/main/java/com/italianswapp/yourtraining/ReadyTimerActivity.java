package com.italianswapp.yourtraining;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.italianswapp.yourtraining.Timer.Circuit.CircuitCreatorActivity;
import com.italianswapp.yourtraining.WorkoutProposed.Workout.Workout;

import java.util.concurrent.TimeUnit;

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
    private ImageButton mButton;

    private static final long READY_TIMER_DURATION = 3000l;
    private static final long READY_TIMER_INTERVAL = 1000l;
    private int readyTimerValue = (int) (READY_TIMER_DURATION/READY_TIMER_INTERVAL);
    private boolean ifSound=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_timer);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark)); //Colora barra notifiche

        mCountDownText = findViewById(R.id.countdownTextViewReadyActivity);
        mButton = findViewById(R.id.buttonReadyActivity);

        /*
        Se l'utente clicca sulla X chiude questa activity e anche quella dell'allenamento
        che stava per iniziare
         */
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                finish();
            }
        });

        mCountDownText.setText(String.valueOf(readyTimerValue));

        CountDownTimer timer = new CountDownTimer(READY_TIMER_DURATION, READY_TIMER_INTERVAL) {
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

        timer.start();


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