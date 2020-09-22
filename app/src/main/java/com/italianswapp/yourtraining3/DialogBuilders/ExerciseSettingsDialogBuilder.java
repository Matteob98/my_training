package com.italianswapp.yourtraining3.DialogBuilders;

import android.app.Activity;
import android.app.AlertDialog;
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

public class ExerciseSettingsDialogBuilder {

    private EditText titleTextView;
    private TextInputLayout titleInputLayout;
    private static NumberPicker firstPicker, secondPicker, thirdPicker;
    private RadioGroup radioGroup;
    private RadioButton secsButton, repsButton;
    private ImageButton deleteButton;
    private Button saveButton;
    private static AlertDialog alertDialog;

    //private AlertDialog.Builder builder;
    private static Dialog builder;
    private final View dialogView;

    /**
     *
     * @param context
     * @param activity
     */
    private ExerciseSettingsDialogBuilder(Context context, Activity activity) {
        //builder = new AlertDialog.Builder(context);
        builder = new Dialog(activity, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.exercise_settings_dialog, null);
        builder.setContentView(dialogView);
        builder.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        setupDialog();
        /*
        builder.setView(dialogView);
        setupDialog();

         */
    }

    /**
     *
     */
    private void setupDialog() {
        titleTextView = dialogView.findViewById(R.id.exerciseNameEditTextExerciseDialog);
        titleInputLayout = dialogView.findViewById(R.id.exerciseNameTextInputLayoutExerciseDialog);

        firstPicker = dialogView.findViewById(R.id.firstPickerExerciseDialog);
        secondPicker = dialogView.findViewById(R.id.secondPickerExerciseDialog);
        thirdPicker = dialogView.findViewById(R.id.thirdPickerExerciseDialog);

        radioGroup = dialogView.findViewById(R.id.radioGroupExerciseSettingsDialog);
        secsButton = dialogView.findViewById(R.id.secsRadioButtonExerciseSettingsDialog);
        repsButton = dialogView.findViewById(R.id.repsRadioButtonExerciseSettingsDialog);

        deleteButton = dialogView.findViewById(R.id.deleteImageButtonExerciseDialog);
        saveButton = dialogView.findViewById(R.id.saveButtonExerciseDialog);
    }

    /**
     *
     * @param context
     * @param activity
     * @return
     */
    public static ExerciseSettingsDialogBuilder newBuilder(Context context, Activity activity) {
        return new ExerciseSettingsDialogBuilder(context, activity);
    }

    /**
     *
     * @param number
     * @param min
     * @param max
     * @param onChange
     * @return
     */
    public ExerciseSettingsDialogBuilder setPicker(int number, int min, int max, NumberPicker.OnValueChangeListener onChange) {
        switch (number) {
            case 1:
                firstPicker.setMinValue(min);
                firstPicker.setMaxValue(max);
                firstPicker.setOnValueChangedListener(onChange);
                break;
            case 2:
                secondPicker.setMinValue(min);
                secondPicker.setMaxValue(max);
                secondPicker.setOnValueChangedListener(onChange);
                break;
            case 3:
                thirdPicker.setMinValue(min);
                thirdPicker.setMaxValue(max);
                thirdPicker.setOnValueChangedListener(onChange);
                break;
        }


        return this;
    }

    /**
     *
     * @param number
     * @param value
     * @return
     */
    public ExerciseSettingsDialogBuilder setPickerValue ( int number, int value) {
        switch(number) {
            case 1:
                firstPicker.setValue(value);
                break;
            case 2:
                secondPicker.setValue(value);
                break;
            case 3:
                thirdPicker.setValue(value);
                break;
        }
        return this;
    }

    /**
     *
     * @param number
     * @param min
     * @param max
     * @param values
     * @param onChange
     * @return
     */
    public ExerciseSettingsDialogBuilder setPicker (int number,  int min, int max, String[] values,  NumberPicker.OnValueChangeListener onChange) {
        this.setPicker(number,min,max,onChange);

        switch (number) {
            case 1:
                firstPicker.setDisplayedValues(values);
                break;
            case 2:
                secondPicker.setDisplayedValues(values);
                break;
            case 3:
                thirdPicker.setDisplayedValues(values);
                break;
        }

        return this;
    }


    /**
     *
     * @param number 1 = reps, 2 = secs
     * @param onClickListener
     * @return
     */
    public ExerciseSettingsDialogBuilder setRadioButtonClick(int number, View.OnClickListener onClickListener) {
        switch (number) {
            case 1:
                repsButton.setOnClickListener(onClickListener);
                break;
            case 2:
                secsButton.setOnClickListener(onClickListener);
                break;
        }
        return this;
    }

    /**
     *
     * @param number
     * @return
     */
    public ExerciseSettingsDialogBuilder setRadioButtonSelected(int number) {
        switch (number) {
            case 1:
                repsButton.callOnClick();
                repsButton.setChecked(true);
                secsButton.setChecked(false);
                break;
            case 2:
                secsButton.callOnClick();
                secsButton.setChecked(true);
                repsButton.setChecked(false);
                break;
        }
        return this;
    }

    /**
     *
     * @param textWatcher
     * @return
     */
    public ExerciseSettingsDialogBuilder setTitleOnTextChange(TextWatcher textWatcher) {
        titleTextView.addTextChangedListener(textWatcher);
        return this;
    }

    public ExerciseSettingsDialogBuilder setTitleValue(String title) {
        titleTextView.setText(title);
        return this;
    }

    /**
     *
     * @return
     */
    public static Dialog getBuilder() {
        return builder;
    }

    /**
     *
     * @param onClickListener
     * @return
     */
    public ExerciseSettingsDialogBuilder setPositiveButton (View.OnClickListener onClickListener ) {
        saveButton.setOnClickListener(onClickListener);
        return this;
    }

    /**
     *
     */
    public void show() {
        //alertDialog = builder.create();
        //alertDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        builder.show();
    }

    /**
     *
     * @return
     */
    public static NumberPicker getSecondPicker() {
        return secondPicker;
    }
}
