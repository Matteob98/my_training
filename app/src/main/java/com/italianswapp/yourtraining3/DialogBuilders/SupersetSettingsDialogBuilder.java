package com.italianswapp.yourtraining3.DialogBuilders;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputLayout;
import com.italianswapp.yourtraining3.R;

public class SupersetSettingsDialogBuilder {

    /*
    Primo esercizio
     */
    private EditText firstTitleTextView;
    private TextInputLayout firstTitleInputLayout;
    private static NumberPicker firstFirstPicker, firstSecondPicker, firstThirdPicker;
    private RadioGroup firstRadioGroup;
    private RadioButton firstSecsButton, firstRepsButton;
    private ImageButton deleteButton;
    private Button saveButton;

    /*
    Secondo esercizio
     */
    private EditText secondTitleTextView;
    private TextInputLayout secondTitleInputLayout;
    private static NumberPicker secondSecondPicker;
    private RadioGroup secondRadioGroup;
    private RadioButton secondSecsButton, secondRepsButton;


    private static Dialog builder;

    private final View dialogView;
    //private static AlertDialog alertDialog;

    /**
     *
     * @param context
     * @param activity
     */
    private SupersetSettingsDialogBuilder(Context context, Activity activity) {
        builder = new Dialog(activity, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.superset_settings_dialog, null);
        builder.setContentView(dialogView);
        builder.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        setupDialog();
    }

    /**
     *
     */
    private void setupDialog() {
        /*
        Primo esercizio
         */
        firstTitleTextView = dialogView.findViewById(R.id.firstExerciseNameEditTextSupersetDialog);
        firstTitleInputLayout = dialogView.findViewById(R.id.firstExerciseNameTextInputLayoutSupersetDialog);
        firstFirstPicker =dialogView.findViewById(R.id.setsPickerSupersetDialog);
        firstSecondPicker = dialogView.findViewById(R.id.firstWorkPickerSupersetDialog);
        firstThirdPicker = dialogView.findViewById(R.id.restPickerSupersetDialog);
        firstRadioGroup = dialogView.findViewById(R.id.firstRadioGroupSupersetSettingsDialog);
        firstSecsButton = dialogView.findViewById(R.id.firstSecsRadioButtonSupersetSettingsDialog);
        firstRepsButton = dialogView.findViewById(R.id.firstRepsRadioButtonSupersetSettingsDialog);


        /*
        Secondo esercizio
         */

        secondTitleTextView = dialogView.findViewById(R.id.secondExerciseNameEditTextSupersetDialog);
        secondTitleInputLayout = dialogView.findViewById(R.id.secondExerciseNameTextInputLayoutSupersetDialog);
        secondSecondPicker = dialogView.findViewById(R.id.secondWorkPickerSupersetDialog);
        secondRadioGroup = dialogView.findViewById(R.id.secondRadioGroupSupersetSettingsDialog);
        secondSecsButton = dialogView.findViewById(R.id.secondSecsRadioButtonSupersetSettingsDialog);
        secondRepsButton = dialogView.findViewById(R.id.secondRepsRadioButtonSupersetSettingsDialog);

        /*
        Button
         */
        deleteButton = dialogView.findViewById(R.id.deleteImageButtonSupersetDialog);
        saveButton = dialogView.findViewById(R.id.saveButtonSupersetDialog);
    }

    /**
     *
     * @param context
     * @param activity
     * @return
     */
    public static SupersetSettingsDialogBuilder newBuilder(Context context, Activity activity) {
        return new SupersetSettingsDialogBuilder(context, activity);
    }

    /**
     *
     * @param exerciseNumber
     * @param number
     * @param min
     * @param max
     * @param onChange
     * @return
     */
    public SupersetSettingsDialogBuilder setPicker(int exerciseNumber, int number, int min, int max, NumberPicker.OnValueChangeListener onChange) {
        switch ((exerciseNumber)) {
            case 1:
                switch (number) {
                    case 1:
                        firstFirstPicker.setMinValue(min);
                        firstFirstPicker.setMaxValue(max);
                        firstFirstPicker.setOnValueChangedListener(onChange);
                        break;
                    case 2:
                        firstSecondPicker.setMinValue(min);
                        firstSecondPicker.setMaxValue(max);
                        firstSecondPicker.setOnValueChangedListener(onChange);
                        break;
                    case 3:
                        firstThirdPicker.setMinValue(min);
                        firstThirdPicker.setMaxValue(max);
                        firstThirdPicker.setOnValueChangedListener(onChange);
                        break;
                }
                break;
            case 2:
                secondSecondPicker.setMinValue(min);
                secondSecondPicker.setMaxValue(max);
                secondSecondPicker.setOnValueChangedListener(onChange);
                break;
        }

        return this;
    }

    /**
     *
     * @param exerciseNumber
     * @param number
     * @param value
     * @return
     */
    public SupersetSettingsDialogBuilder setPickerValue (int exerciseNumber, int number, int value) {
        switch ((exerciseNumber)) {
            case 1:
                switch (number) {
                    case 1:
                        firstFirstPicker.setValue(value);
                        break;
                    case 2:
                        firstSecondPicker.setValue(value);
                        break;
                    case 3:
                        firstThirdPicker.setValue(value);
                        break;
                }
                break;
            case 2:
                secondSecondPicker.setValue(value);
                break;
        }

        return this;
    }


    /**
     *
     * @param exerciseNumber
     * @param number
     * @param min
     * @param max
     * @param values
     * @param onChange
     * @return
     */
    public SupersetSettingsDialogBuilder setPicker (int exerciseNumber, int number,  int min, int max, String[] values,  NumberPicker.OnValueChangeListener onChange) {
        this.setPicker(exerciseNumber,number,min,max,onChange);

        switch (exerciseNumber) {
            case 1:
                switch (number) {
                    case 1:
                        firstFirstPicker.setDisplayedValues(values);
                        break;
                    case 2:
                        firstSecondPicker.setDisplayedValues(values);
                        break;
                    case 3:
                        firstThirdPicker.setDisplayedValues(values);
                        break;
                }
                break;
            case 2:
                secondSecondPicker.setDisplayedValues(values);
                break;
        }

        return this;
    }

    /**
     *
     * @param exerciseNumber
     * @param number
     * @param onClickListener
     * @return
     */
    public SupersetSettingsDialogBuilder setRadioButtonClick(int exerciseNumber, int number, View.OnClickListener onClickListener) {
        switch (exerciseNumber) {
            case 1:
                switch (number) {
                    case 1:
                        firstRepsButton.setOnClickListener(onClickListener);
                        break;
                    case 2:
                        firstSecsButton.setOnClickListener(onClickListener);
                        break;
                }
                break;
            case 2:
                switch (number) {
                    case 1:
                        secondRepsButton.setOnClickListener(onClickListener);
                        break;
                    case 2:
                        secondSecsButton.setOnClickListener(onClickListener);
                        break;
                }
                break;
        }

        return this;
    }

    /**
     *
     * @param exerciseNumber
     * @param number
     * @return
     */
    public SupersetSettingsDialogBuilder setRadioButtonSelected (int exerciseNumber, int number) {
        switch (exerciseNumber) {
            case 1:
                switch (number) {
                    case 1:
                        firstRepsButton.callOnClick();
                        firstRepsButton.setChecked(true);
                        firstSecsButton.setChecked(false);
                        break;
                    case 2:
                        firstSecsButton.callOnClick();
                        firstSecsButton.setChecked(true);
                        firstRepsButton.setChecked(false);
                        break;
                }
                break;
            case 2:
                switch (number) {
                    case 1:
                        secondRepsButton.callOnClick();
                        secondRepsButton.setChecked(true);
                        secondSecsButton.setChecked(false);
                        break;
                    case 2:
                        secondSecsButton.callOnClick();
                        secondSecsButton.setChecked(true);
                        secondRepsButton.setChecked(false);
                        break;
                }
                break;
        }

        return this;
    }

    /**
     *
     * @param exerciseName
     * @param textWatcher
     * @return
     */
    public SupersetSettingsDialogBuilder setTitleOnTextChange(int exerciseName, TextWatcher textWatcher) {
        switch (exerciseName) {
            case 1:
                firstTitleTextView.addTextChangedListener(textWatcher);
                break;
            case 2:
                secondTitleTextView.addTextChangedListener(textWatcher);
                break;
        }

        return this;
    }

    /**
     *
     * @param exerciseName
     * @param title
     * @return
     */
    public SupersetSettingsDialogBuilder setTitleValue ( int exerciseName, String title) {
        switch (exerciseName) {
            case 1:
                firstTitleTextView.setText(title);
                break;
            case 2:
                secondTitleTextView.setText(title);
                break;
        }

        return this;
    }

    /**
     *
     * @param onClickListener
     * @return
     */
    public SupersetSettingsDialogBuilder setPositiveButton (android.view.View.OnClickListener onClickListener ) {
        saveButton.setOnClickListener(onClickListener);
        return this;
    }

    /**
     *
     */
    public void show() {

        //alertDialog = builder.create();
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        builder.show();
        //alertDialog.show();
    }

    /**
     *
     * @param exerciseNumber
     * @return
     */
    public static NumberPicker getSecondPicker(int exerciseNumber) {
        switch (exerciseNumber) {
            case 1:
                return firstSecondPicker;
            case 2:
                return secondSecondPicker;
            default:
                return null;
        }

    }

    /*
     *
     * @return

    public static AlertDialog getAlertDialog() {
        return alertDialog;
    }

     */

    public static Dialog getBuilder() {
        return builder;
    }
}
