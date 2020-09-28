package com.italianswapp.yourtraining.Menu;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.NumberPicker;

import com.google.android.material.snackbar.Snackbar;
import com.italianswapp.yourtraining.DialogBuilders.Dialog2PickerBuilder;
import com.italianswapp.yourtraining.DialogBuilders.Dialog3PickerBuilder;
import com.italianswapp.yourtraining.Chronometer.ChronometerActivity;
import com.italianswapp.yourtraining.HelpActivity;
import com.italianswapp.yourtraining.R;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitCreatorActivity;
import com.italianswapp.yourtraining.Timer.CountDownTimers.AmrapActivity;
import com.italianswapp.yourtraining.Timer.CountDownTimers.EmomActivity;
import com.italianswapp.yourtraining.Timer.CountDownTimers.NegativePhaseActivity;
import com.italianswapp.yourtraining.Timer.CountDownTimers.TabataActivity;
import com.italianswapp.yourtraining.Timer.TimerActivity;
import com.italianswapp.yourtraining.Utilities.Utilities;

public class TimerFragment extends Fragment implements View.OnClickListener, NumberPicker.OnValueChangeListener{

    private View view;

    private CardView circuitCard, chronometerCard, tabataCard,
            timerCard, amrapCard, emomCard, negativeCard;

    private ImageButton helpButton;

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

    public TimerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_timer, container, false);

        circuitCard = view.findViewById(R.id.circuitCard);
        circuitCard.setOnClickListener(this);

        chronometerCard = view.findViewById(R.id.chronometerCard);
        chronometerCard.setOnClickListener(this);

        tabataCard = view.findViewById(R.id.tabataCard);
        tabataCard.setOnClickListener(this);

        timerCard = view.findViewById(R.id.timerCard);
        timerCard.setOnClickListener(this);

        amrapCard = view.findViewById(R.id.amrapCard);
        amrapCard.setOnClickListener(this);

        emomCard = view.findViewById(R.id.emomCard);
        emomCard.setOnClickListener(this);

        negativeCard = view.findViewById(R.id.negativeCard);
        negativeCard.setOnClickListener(this);

        helpButton = view.findViewById(R.id.helpButtonActivityMain);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpClick(v);
            }
        });

        return view;
    }

    @Override
    public void onClick (final View view) {
        int id = view.getId();

        switch (id) {
            case R.id.circuitCard:
                Intent intent=new Intent(view.getContext(), CircuitCreatorActivity.class);
                startActivity(intent);
                //finish();
                break;
            case R.id.chronometerCard:
                startChronometherActivity();
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


        Dialog3PickerBuilder.newBuilder(view.getContext(), getActivity())
                .setText(0, getResources().getString(R.string.concentric_phase))
                .setText(1, getResources().getString(R.string.reps))
                .setText(2, getResources().getString(R.string.concentric))
                .setText(3, getResources().getString(R.string.eccentric))
                .setPicker(1, 1, 100, this)
                .setPicker(2, 1, 120, this)
                .setPicker(3, 1, 120, this)
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog3PickerBuilder.getAlertDialog().dismiss();
                        callSecondNegativePicker();
                    }
                })
                .show();
    }

    private void callSecondNegativePicker() {
        flag=5;
        setsText=1;
        recNegative=TimeInStringForPicker[0];

        Dialog2PickerBuilder.newBuilder(view.getContext(), getActivity())
                .setText(0, getResources().getString(R.string.concentric_phase))
                .setText(1, getResources().getString(R.string.sets))
                .setText(2, getResources().getString(R.string.rest))
                .setPicker(1, 1, 100, this)
                .setPicker(2, 0, TimeInStringForPicker.length - 1,TimeInStringForPicker, this)
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = NegativePhaseActivity.getInstance(view.getContext(),
                                repetitionNegativePhase,
                                positiveText*1000,
                                negativeText*1000,
                                setsText,
                                Utilities.getMillsFromMinuteString(recNegative));

                        Dialog2PickerBuilder.getAlertDialog().dismiss();
                        startActivity(intent);
                    }
                })
                .show();
    }

    private void callFirstAmrapPicker() {
        flag = 3;
        hoursTimer=0;
        minsTimer=0;
        secsTimer=0;

        Dialog3PickerBuilder.newBuilder(view.getContext(), getActivity())
                .setText(0, getResources().getString(R.string.amrap))
                .setText(1, getResources().getString(R.string.hours))
                .setText(2, getResources().getString(R.string.mins))
                .setText(3, getResources().getString(R.string.secs))
                .setPicker(1, 0, 23, this)
                .setPicker(2, 0, 59, this)
                .setPicker(3, 0, 59, this)
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (hoursTimer + minsTimer + secsTimer != 0) {

                            //Fa partire l'activity Timer passando il valore selezionato in millisecondi
                            amrapDuration = (((((hoursTimer * 60) + minsTimer)) * 60) + secsTimer) * 1000;

                            Dialog3PickerBuilder.getAlertDialog().dismiss();
                            callSecondArmapPicker();
                        }
                        else
                            Snackbar.make(view.findViewById(R.id.menuLinearLayout),
                                    getResources().getString(R.string.invalid_time),
                                    Snackbar.LENGTH_SHORT).show();
                    }
                })
                .show();
        Dialog3PickerBuilder.setSaveButtonClickable(false); //Finchè non viene mosso il selettore non viene abilitato il pulsante salva

    }

    private void callSecondArmapPicker() {
        flag=2;
        roundOfTabataText =1;
        restBetweenSetsString=TimeInStringForPicker[0];

        Dialog2PickerBuilder.newBuilder(view.getContext(), getActivity())
                .setText(0, getResources().getString(R.string.amrap))
                .setText(1, getResources().getString(R.string.sets))
                .setText(2, getResources().getString(R.string.rest))
                .setPicker(1, 1, 20, this)
                .setPicker(2, 0, TimeInStringForPicker.length - 1,TimeInStringForPicker, this)
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = AmrapActivity.getInstance(view.getContext(),
                                amrapDuration,
                                roundOfTabataText,
                                Utilities.getMillsFromMinuteString(restBetweenSetsString));

                        Dialog2PickerBuilder.getAlertDialog().dismiss();
                        startActivity(intent);
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

        Dialog3PickerBuilder.newBuilder(view.getContext(), getActivity())
                .setText(0, getResources().getString(R.string.tabata))
                .setText(1, getResources().getString(R.string.sets))
                .setText(2, getResources().getString(R.string.work))
                .setText(3, getResources().getString(R.string.rest))
                .setPicker(1, 1, 100, this)
                .setPicker(2, 0, TimeInStringForPicker.length - 1,TimeInStringForPicker, this)
                .setPicker(3, 0, TimeInStringForPicker.length - 1,TimeInStringForPicker, this)
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog3PickerBuilder.getAlertDialog().dismiss();
                        callSecondTabataPicker();
                    }
                })
                .show();
    }

    private void callSecondTabataPicker() {
        flag=2;
        roundOfTabataText =1;
        restBetweenSetsString=TimeInStringForPicker[0];

        Dialog2PickerBuilder.newBuilder(view.getContext(), getActivity())
                .setText(0, getResources().getString(R.string.tabata))
                .setText(1, getResources().getString(R.string.sets))
                .setText(2, getResources().getString(R.string.rest))
                .setPicker(1, 1, 20, this)
                .setPicker(2, 0, TimeInStringForPicker.length - 1,TimeInStringForPicker, this)
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Fa partire l'activity tabata passando il valore selezionato
                        Intent intent = TabataActivity.getInstance(view.getContext(),
                                Utilities.getMillsFromMinuteString(workString),
                                Utilities.getMillsFromMinuteString(restString),
                                setsText,
                                roundOfTabataText,
                                Utilities.getMillsFromMinuteString(restBetweenSetsString));

                        Dialog2PickerBuilder.getAlertDialog().dismiss();
                        startActivity(intent);
                    }
                })
                .show();
    }

    /**
     * Fa partire l'activity cronometro in modalità standard o forTime
     */
    private void startChronometherActivity() {
        Intent intent= ChronometerActivity.getChronometerActivity(view.getContext());
        startActivity(intent);
    }

    /**
     * Chiama il picker per la selezione dei tempi per l'Emom
     */
    private void callEmomPicker() {
        flag=0;
        everyString=TimeInStringForPicker[0];
        forText=1;

        Dialog2PickerBuilder.newBuilder(view.getContext(), getActivity())
                .setText(0, getResources().getString(R.string.emom))
                .setText(1, getResources().getString(R.string.every))
                .setText(2, getResources().getString(R.string.For))
                .setPicker(1, 0, TimeInStringForPicker.length - 1, TimeInStringForPicker,  this)
                .setPicker(2, 1, 100 , this)
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Fa partire l'activity emom passando il valore selezionato in millisecondi
                        Intent intent = EmomActivity.getInstance(view.getContext(),
                                Utilities.getMillsFromMinuteString(everyString)
                                , forText );

                        Dialog2PickerBuilder.getAlertDialog().dismiss();
                        startActivity(intent);
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

        Dialog3PickerBuilder.newBuilder(view.getContext(), getActivity())
                .setText(0, getResources().getString(R.string.timer))
                .setText(1, getResources().getString(R.string.hours))
                .setText(2, getResources().getString(R.string.mins))
                .setText(3, getResources().getString(R.string.secs))
                .setPicker(1, 0, 23, this)
                .setPicker(2, 0, 59, this)
                .setPicker(3, 0, 59, this)
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (hoursTimer + minsTimer + secsTimer != 0) {

                            //Fa partire l'activity Timer passando il valore selezionato in millisecondi
                            long mills = (((((hoursTimer * 60) + minsTimer)) * 60) + secsTimer) * 1000;
                            Intent intent = TimerActivity.getIntentInstance(view.getContext(), mills);
                            Dialog3PickerBuilder.getAlertDialog().dismiss();
                            startActivity(intent);
                        }
                        else
                            Snackbar.make(view.findViewById(R.id.menuLinearLayout),
                                    getResources().getString(R.string.invalid_time),
                                    Snackbar.LENGTH_SHORT).show();
                    }
                })
                .show();
        Dialog3PickerBuilder.setSaveButtonClickable(false); //Finchè non viene mosso il selettore non viene abilitato il pulsante salva
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
                        //timer e amrap
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

        /*
        Se è il primo dialog di timer o amrap e la somma tra ore minuti e secondi è 0, disabilita il pulsante salva
         */
        if ( (id == R.id.firstPickerNumberPicker3 || id == R.id.secondPickerNumberPicker3 || id == R.id.thirdPickerNumberPicker3) && (flag == 3))
            if (hoursTimer + minsTimer + secsTimer == 0)
                Dialog3PickerBuilder.setSaveButtonClickable(false);
            else
                Dialog3PickerBuilder.setSaveButtonClickable(true);
    }


    public void helpClick(View view) {
        Intent intent=new Intent(view.getContext(), HelpActivity.class);
        startActivity(intent);
    }
}