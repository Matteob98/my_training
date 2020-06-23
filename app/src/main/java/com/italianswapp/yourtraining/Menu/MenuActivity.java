package com.italianswapp.yourtraining.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;

import com.google.android.material.snackbar.Snackbar;
import com.italianswapp.yourtraining.Timer.CountDownTimers.AmrapActivity;
import com.italianswapp.yourtraining.Chronometer.ChronometerActivity;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitCreatorActivity;
import com.italianswapp.yourtraining.Timer.CountDownTimers.EmomActivity;
import com.italianswapp.yourtraining.HelpActivity;
import com.italianswapp.yourtraining.Timer.CountDownTimers.NegativePhaseActivity;
import com.italianswapp.yourtraining.Utilities;
import com.italianswapp.yourtraining.R;
import com.italianswapp.yourtraining.Timer.CountDownTimers.TabataActivity;
import com.italianswapp.yourtraining.Timer.TimerActivity;

import java.util.Objects;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener, NumberPicker.OnValueChangeListener{

    private final static String MAIN_KEY = "main";
    private CardView circuitCard, chronometerCard, tabataCard,
            timerCard, amrapCard, emomCard, negativeCard;
    private Toolbar toolbar;

    private final String[] TimeInStringForPicker =  Utilities.TIME_IN_STRING;

    /*
       Variabili per le selezioni dei picker
    */
    private int hoursTimer=0, minsTimer=0, secsTimer=0, forText=1, roundOfTabataText =0,
            setsText=1, repetitionNegativePhase=1, positiveText=1, negativeText=1;
    private String everyString= TimeInStringForPicker[0];
    private String restString= TimeInStringForPicker[0];
    private String workString= TimeInStringForPicker[0];
    private String restBetweenSetsString = TimeInStringForPicker[0];
    private String recNegative= TimeInStringForPicker[0];
    private long amrapDuration=0L;

    /**
     * Flag = 0 -> EMOM
     * Flag = 1 -> Tabata1
     * Flag = 2 -> Tabata2
     * Flag = 3 -> Timer/Amrap
     * Flag = 4 -> Negative1
     * Flag = 5 -> Negative2
     */
    private int flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        toolbar = findViewById(R.id.toolbarMainActivity);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(getResources().getColor(R.color.textColor));


        circuitCard = findViewById(R.id.circuitCard);
        circuitCard.setOnClickListener(this);

        chronometerCard = findViewById(R.id.chronometerCard);
        chronometerCard.setOnClickListener(this);

        tabataCard = findViewById(R.id.tabataCard);
        tabataCard.setOnClickListener(this);

        timerCard = findViewById(R.id.timerCard);
        timerCard.setOnClickListener(this);

        amrapCard = findViewById(R.id.amrapCard);
        amrapCard.setOnClickListener(this);

        emomCard = findViewById(R.id.emomCard);
        emomCard.setOnClickListener(this);

        negativeCard = findViewById(R.id.negativeCard);
        negativeCard.setOnClickListener(this);

    }

    @Override
    public void onClick (final View view) {
        int id = view.getId();

        switch (id) {
            case R.id.circuitCard:
                Intent intent=new Intent(this, CircuitCreatorActivity.class);
                startActivity(intent);
                //finish();
                break;
            case R.id.chronometerCard:
                if(! getIntent().getBooleanExtra(MAIN_KEY, false)) //Se non è già attivo un altro cronometro allora creo l'activity
                    startChronometherActivity();
                //finish();
                break;
            case R.id.tabataCard:
                callFirstTabataPicker();
                break;
            case R.id.timerCard:
                callTimerPicker();
                break;
            case R.id.amrapCard:
                callFirstAmrapPicker();
                break;
            case R.id.emomCard:
                callEmomPicker();
                break;
            case R.id.negativeCard:
                callFirstNegativePicker();
                break;
        }

    }

    private void callFirstNegativePicker() {
        flag=4;
        positiveText=1;
        negativeText=1;


        Dialog3PickerBuilder.newBuilder(this, this)
                .setText(0, getResources().getString(R.string.concentric_phase))
                .setText(1, getResources().getString(R.string.reps))
                .setText(2, getResources().getString(R.string.concentric))
                .setText(3, getResources().getString(R.string.eccentric))
                .setPicker(1, 1, 100, this)
                .setPicker(2, 1, 120, this)
                .setPicker(3, 1, 120, this)
                .setPositiveButton(getResources().getString(R.string.Continue),  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callSecondNegativePicker();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void callSecondNegativePicker() {
        flag=5;
        setsText=1;
        recNegative=TimeInStringForPicker[0];

        Dialog2PickerBuilder.newBuilder(this, this)
                .setText(0, getResources().getString(R.string.concentric_phase))
                .setText(1, getResources().getString(R.string.sets))
                .setText(2, getResources().getString(R.string.rest))
                .setPicker(1, 1, 100, this)
                .setPicker(2, 0, TimeInStringForPicker.length - 1,TimeInStringForPicker, this)
                .setPositiveButton(getResources().getString(R.string.Continue),  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = NegativePhaseActivity.getInstance(getApplicationContext(),
                                repetitionNegativePhase,
                                positiveText*1000,
                                negativeText*1000,
                                setsText,
                                Utilities.getMillsFromMinuteString(recNegative));

                        startActivity(intent);
                        ///finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callFirstNegativePicker();
                    }
                })
                .show();
    }

    private void callFirstAmrapPicker() {
        flag = 3;
        hoursTimer=0;
        minsTimer=0;
        secsTimer=0;

        Dialog3PickerBuilder.newBuilder(this, this)
                .setText(0, getResources().getString(R.string.amrap))
                .setText(1, getResources().getString(R.string.hours))
                .setText(2, getResources().getString(R.string.mins))
                .setText(3, getResources().getString(R.string.secs))
                .setPicker(1, 0, 23, this)
                .setPicker(2, 0, 59, this)
                .setPicker(3, 0, 59, this)
                .setPositiveButton(getResources().getString(R.string.Continue),  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (hoursTimer + minsTimer + secsTimer != 0) {
                            dialog.dismiss();

                            //Fa partire l'activity Timer passando il valore selezionato in millisecondi
                            amrapDuration = (((((hoursTimer * 60) + minsTimer)) * 60) + secsTimer) * 1000;

                            callSecondArmapPicker();
                        }
                        else
                            Snackbar.make(findViewById(R.id.menuLinearLayout),
                                    getResources().getString(R.string.invalid_time),
                                    Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    private void callSecondArmapPicker() {
        flag=2;
        roundOfTabataText =1;
        restBetweenSetsString=TimeInStringForPicker[0];

        Dialog2PickerBuilder.newBuilder(this, this)
                .setText(0, getResources().getString(R.string.amrap))
                .setText(1, getResources().getString(R.string.sets))
                .setText(2, getResources().getString(R.string.rest))
                .setPicker(1, 1, 20, this)
                .setPicker(2, 0, TimeInStringForPicker.length - 1,TimeInStringForPicker, this)
                .setPositiveButton(getResources().getString(R.string.Continue),  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent intent = AmrapActivity.getInstance(getApplicationContext(),
                                amrapDuration,
                                roundOfTabataText,
                                Utilities.getMillsFromMinuteString(restBetweenSetsString));

                        startActivity(intent);
                        //finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.back), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callFirstTabataPicker();
                    }
                })
                .show();
    }

    /**
     * Chiama il picker per la selezione dei tempi del Timer
     */
    private void callFirstTabataPicker() {
        flag=1;
        setsText=1;
        workString=TimeInStringForPicker[0];
        restString=TimeInStringForPicker[0];

        Dialog3PickerBuilder.newBuilder(this, this)
                .setText(0, getResources().getString(R.string.tabata))
                .setText(1, getResources().getString(R.string.sets))
                .setText(2, getResources().getString(R.string.work))
                .setText(3, getResources().getString(R.string.rest))
                .setPicker(1, 1, 100, this)
                .setPicker(2, 0, TimeInStringForPicker.length - 1,TimeInStringForPicker, this)
                .setPicker(3, 0, TimeInStringForPicker.length - 1,TimeInStringForPicker, this)
                .setPositiveButton(getResources().getString(R.string.Continue),  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callSecondTabataPicker();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void callSecondTabataPicker() {
        flag=2;
        roundOfTabataText =1;
        restBetweenSetsString=TimeInStringForPicker[0];

        Dialog2PickerBuilder.newBuilder(this, this)
                .setText(0, getResources().getString(R.string.tabata))
                .setText(1, getResources().getString(R.string.sets))
                .setText(2, getResources().getString(R.string.rest))
                .setPicker(1, 1, 20, this)
                .setPicker(2, 0, TimeInStringForPicker.length - 1,TimeInStringForPicker, this)
                .setPositiveButton(getResources().getString(R.string.Continue),  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        //Fa partire l'activity tabata passando il valore selezionato
                        Intent intent = TabataActivity.getInstance(getApplicationContext(),
                                Utilities.getMillsFromMinuteString(workString),
                                Utilities.getMillsFromMinuteString(restString),
                                setsText,
                                roundOfTabataText,
                                Utilities.getMillsFromMinuteString(restBetweenSetsString));

                        startActivity(intent);
                        //finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.back), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callFirstTabataPicker();
                    }
                })
                .show();
    }

    /**
     * Fa partire l'activity cronometro in modalità standard o forTime
     */
    private void startChronometherActivity() {
        Intent intent= ChronometerActivity.getChronometerActivity(getApplicationContext());
        startActivity(intent);
        //finish();
    }

    /**
     * Chiama il picker per la selezione dei tempi per l'Emom
     */
    private void callEmomPicker() {
        flag=0;
        everyString=TimeInStringForPicker[0];
        forText=1;

        Dialog2PickerBuilder.newBuilder(this, this)
                .setText(0, getResources().getString(R.string.emom))
                .setText(1, getResources().getString(R.string.every))
                .setText(2, getResources().getString(R.string.For))
                .setPicker(1, 0, TimeInStringForPicker.length - 1, TimeInStringForPicker,  this)
                .setPicker(2, 1, 100 , this)
                .setPositiveButton(getResources().getString(R.string.Continue),  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        //Fa partire l'activity emom passando il valore selezionato in millisecondi
                        Intent intent = EmomActivity.getInstance(getApplicationContext(),
                                Utilities.getMillsFromMinuteString(everyString)
                                , forText );
                        startActivity(intent);
                        //finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.back), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     *
     * Chiama il picker per la selezione del tempo del timer
     */
    private void callTimerPicker() {
        flag = 3;
        hoursTimer=0;
        minsTimer=0;
        secsTimer=0;

        Dialog3PickerBuilder.newBuilder(this, this)
                .setText(0, getResources().getString(R.string.timer))
                .setText(1, getResources().getString(R.string.hours))
                .setText(2, getResources().getString(R.string.mins))
                .setText(3, getResources().getString(R.string.secs))
                .setPicker(1, 0, 23, this)
                .setPicker(2, 0, 59, this)
                .setPicker(3, 0, 59, this)
                .setPositiveButton(getResources().getString(R.string.Continue),  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (hoursTimer + minsTimer + secsTimer != 0) {
                            dialog.dismiss();

                            //Fa partire l'activity Timer passando il valore selezionato in millisecondi
                            long mills = (((((hoursTimer * 60) + minsTimer)) * 60) + secsTimer) * 1000;
                            Intent intent = TimerActivity.getIntentInstance(getApplicationContext(), mills);
                            startActivity(intent);
                            //finish();
                        }
                        else
                            Snackbar.make(findViewById(R.id.menuLinearLayout),
                                    getResources().getString(R.string.invalid_time),
                                    Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        int id = picker.getId();
        switch (id) {
            case R.id.firstPickerNumberPicker3:
                switch (flag) {
                    case 1:
                        setsText = newVal;
                        break;
                    case 3:
                        hoursTimer = newVal;
                        break;
                    case 4:
                        repetitionNegativePhase=newVal;
                        break;
                }
                break;

            case R.id.secondPickerNumberPicker3:
                switch (flag) {
                    case 1:
                        workString = TimeInStringForPicker[newVal];
                        break;
                    case 3:
                        minsTimer = newVal;
                        break;
                    case 4:
                        positiveText=newVal;
                        break;
                }
                break;

            case R.id.thirdPickerNumberPicker3:
                switch (flag)
                {
                    case 1:
                        restString = TimeInStringForPicker[newVal];
                        break;
                    case 3:
                        secsTimer = newVal;
                        break;
                    case 4:
                        negativeText=newVal;
                        break;
                }
                break;

            case R.id.firstPickerNumberPicker2:
                switch (flag) {
                    case 0:
                        everyString = TimeInStringForPicker[newVal];
                        break;
                    case 2:
                        roundOfTabataText = newVal;
                        break;
                    case 5:
                        setsText = newVal;
                        break;

                }
                break;
            case R.id.secondPickerNumberPicker2:
                switch (flag) {
                    case 0:
                        forText = newVal;
                        break;

                    case 2:
                        restBetweenSetsString= TimeInStringForPicker[newVal];
                        break;
                    case 5:
                        recNegative = TimeInStringForPicker[newVal];
                        break;
                }
                break;
        }
    }

    /**
     * Metodo che viene chiamato dall'activity del cronometro per far si che venga richiamato il cronometro in corso
     * E non creato uno nuovo
     * @param context Il contesto dove ci si trova
     * @return L'activity Menu
     */
    public static Intent getMenuActivityFromChronometer(Context context) {
        Intent intent = new Intent(context, MenuActivity.class);
        intent.putExtra(MAIN_KEY, true);

        return intent;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem mHelpButton = menu.findItem(R.id.helpToolbar);
        mHelpButton.setVisible(true);
        mHelpButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                goToHelpActivity();
                return true;
            }
        });
        return true;
    }

    private void goToHelpActivity() {
        Intent intent=new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

}

