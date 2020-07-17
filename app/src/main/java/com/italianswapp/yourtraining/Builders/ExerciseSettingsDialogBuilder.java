package com.italianswapp.yourtraining.Builders;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputLayout;
import com.italianswapp.yourtraining.R;

public class ExerciseSettingsDialogBuilder {

    private EditText titleTextView;
    private TextInputLayout titleInputLayout;
    private static NumberPicker firstPicker, secondPicker, thirdPicker;
    private RadioGroup radioGroup;
    private RadioButton secsButton, repsButton;

    private AlertDialog.Builder builder;
    private final View dialogView;

    /**
     *
     * @param context
     * @param activity
     */
    private ExerciseSettingsDialogBuilder(Context context, Activity activity) {
        builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.exercise_settings_dialog, null);
        builder.setView(dialogView);

        setupDialog();
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
     * @param textWatcher
     * @return
     */
    public ExerciseSettingsDialogBuilder setTitleOnTextChange(TextWatcher textWatcher) {
        titleTextView.addTextChangedListener(textWatcher);
        return this;
    }

    /**
     *
     * @param text
     * @param onClickListener
     * @return
     */
    public ExerciseSettingsDialogBuilder setPositiveButton (String text, DialogInterface.OnClickListener onClickListener ) {
        builder.setPositiveButton( text, onClickListener );
        return this;
    }

    /**
     *
     * @param text
     * @param onClickListener
     * @return
     */
    public ExerciseSettingsDialogBuilder setNegativeButton ( String text, DialogInterface.OnClickListener onClickListener ) {
        builder.setNegativeButton( text, onClickListener );
        return this;
    }

    /*

     */
    public void show() {
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static NumberPicker getSecondPicker() {
        return secondPicker;
    }
}
