package com.italianswapp.yourtraining.Builders;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.italianswapp.yourtraining.R;

/**
 * Builder di dialog con un campo di selezione (picker)
 */
public class Dialog1PickerBuilder {

    private TextView firstTextView, titleTextView;
    private NumberPicker firstPicker;
    private View colorView;

    private AlertDialog.Builder builder;
    private final View dialogView;

    /**
     * Metodo privato
     * Costruttore del builder di una picker
     * Se chiamato in un activity
     * @param context Il contesto dove richiamare il picker
     * @param activity L'activity dove mostrare l'input
     */
    private Dialog1PickerBuilder(Context context, Activity activity) {
        builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.number_picker1, null);
        builder.setView(dialogView);

        setupDialog();
    }

    /**
     * Impostazioni preliminari
     * Richiama gli elementi dal layout
     */
    private void setupDialog() {
        firstTextView = dialogView.findViewById(R.id.firstTextNumberPicker1);
        titleTextView = dialogView.findViewById(R.id.titleNumberPicker1);

        firstPicker = dialogView.findViewById(R.id.firstPickerNumberPicker1);
        colorView = dialogView.findViewById(R.id.colorViewNumberPicker1);

    }

    /**
     * Crea un nuovo builder di dialog all'interno di un activity
     * Restituisce il builder
     * @param context Il contesto dove richiamare il picker
     * @param activity L'activity dove mostrare l'input
     * @return il builde
     */
    public static Dialog1PickerBuilder newBuilder(Context context, Activity activity) {
        return new Dialog1PickerBuilder(context, activity);
    }

    /**
     * Imposta il testo del titolo del dialog
     * @param text Il testo da impostare
     * @return il Builde
     */
    public Dialog1PickerBuilder setText(int number, String text) {
        switch (number){
            case  0:
                titleTextView.setText(text);
                break;
            case 1:
                firstTextView.setText(text);
                break;
        }

        return this;
    }

    /**
     *
     * @param min
     * @param max
     * @param onChange
     * @return
     */
    public Dialog1PickerBuilder setPicker( int min, int max, NumberPicker.OnValueChangeListener onChange) {
        firstPicker.setMinValue(min);
        firstPicker.setMaxValue(max);
        firstPicker.setOnValueChangedListener(onChange);

        return this;
    }

    /**
     *
     * @param value
     * @return
     */
    public Dialog1PickerBuilder setPickerValue(int value) {
        firstPicker.setValue(value);
        return this;
    }

    /**
     *
     * @param min
     * @param max
     * @param values
     * @param onChange
     * @return
     */
    public Dialog1PickerBuilder setPicker ( int min, int max, String[] values,  NumberPicker.OnValueChangeListener onChange) {
        this.setPicker(min,max,onChange);

        firstPicker.setDisplayedValues(values);

        return this;
    }

    /**
     *
     * @param text
     * @param onClickListener
     * @return
     */
    public Dialog1PickerBuilder setPositiveButton (String text, DialogInterface.OnClickListener onClickListener ) {
        builder.setPositiveButton( text, onClickListener );
        return this;
    }

    /**
     *
     * @param text
     * @param onClickListener
     * @return
     */
    public Dialog1PickerBuilder setNegativeButton ( String text, DialogInterface.OnClickListener onClickListener ) {
        builder.setNegativeButton( text, onClickListener );
        return this;
    }

    /**
     * Mostra la dialog creata dal builder
     */
    public void show() {
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     *
     * @param color
     * @return
     */
    public Dialog1PickerBuilder setDialogColor(int color) {
        colorView.setBackgroundResource(color);
        return this;
    }

    /**
     *
     * @param color
     * @return
     */
    public Dialog1PickerBuilder setTextColor(int color) {
        titleTextView.setTextColor(color);
        return this;
    }
}
