package com.italianswapp.yourtraining.Timer.CountDownTimers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
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

        mPrimaryTextView.setText(getRoundString());

        timer = createTimer();

        startButtonCreator();

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

        /*
        Disabilito tutto, sarà riabilitato dopo il ready con l'iniziatizes
         */
        mRightFloatingButton.setEnabled(false);
        mRightFloatingButton.setImageResource(R.drawable.ic_add_black);
        mLeftFloatingButton.setEnabled(false);
        mLeftFloatingButton.setImageResource(R.drawable.ic_minus_scuro);

        mSecondaryTextView.setVisibility(TextView.INVISIBLE);
        mOverlineSecondaryTextView.setVisibility(TextView.INVISIBLE);

    }

    private String getRoundString() { return res.getString(R.string.round_completed) + " "+ roundAmrap; }

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
                }
                else if (currentSet<setsNumber) {
                    //Se era un lavoro e se non ho finito i sets da fare
                    startRest();
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
        roundAmrap=0;
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

        mRightFloatingButton.setEnabled(true);
        mRightFloatingButton.setImageResource(R.drawable.ic_add);
        mLeftFloatingButton.setEnabled(true);
        mLeftFloatingButton.setImageResource(R.drawable.ic_minus);
    }

    /**
     * Sovrascrive il metodo della superclasse
     * Inserisce un dialog in cui poter modificare il numero di round completati
     */
    @Override
    protected void finishCountDown() {
        if(isIfSound())
            MediaPlayer.create(this, R.raw.timer_sound).start();
        /*
        Creo costruttore e inflater per il dialog
        */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        /*
       Carico il dialog
       */
        View dialogView = inflater.inflate(R.layout.number_picker_dialog, null);
        builder.setView(dialogView);

        /*
        Imposto il number picker
        Valore minimo = 0
        valore massimo contenuto in maxRound
        Valore attuale quello in roundAmrap
         */
        NumberPicker picker = dialogView.findViewById(R.id.numberPicker1);
        builder.setTitle(res.getString(R.string.many_round_completed));
        picker.setMinValue(0);
        picker.setMaxValue(maxRound);
        picker.setValue(roundAmrap);

        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                roundAmrap=newVal;
            }
        });


        // Set up the buttons
        builder.setPositiveButton(res.getString(R.string.Continue), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishCountDown(res.getString(R.string.round_completed) + ": " + roundAmrap);
            }
        });

        /*
        Imposto che alla pressione del tasto back non deve uscire dal dialog
         */
        builder.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) { return false; }
                return true;
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public static Intent getInstance(Context context, long work, int sets, long rest) {
        Intent intent = new Intent(context, AmrapActivity.class);

        intent.putExtra(WORK_KEY, work);
        intent.putExtra(SETS_KEY, sets);
        intent.putExtra(REST_KEY, rest);
        return intent;
    }
}
