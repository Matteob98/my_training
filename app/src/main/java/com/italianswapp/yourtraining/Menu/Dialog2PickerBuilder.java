package com.italianswapp.yourtraining.Menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.italianswapp.yourtraining.R;

import java.util.Objects;

/**
 * Builder di dialog con due campi di selezione (picker)
 */
public class Dialog2PickerBuilder {

    private TextView firstTextView, secondTextView, titleTextView;
    private NumberPicker firstPicker, secondPicker;

    private AlertDialog.Builder builder;
    private final View dialogView;

    /**
     * Metodo privato
     * Costruttore del builder di una picker
     * Se chiamato in un activity
     * @param context Il contesto dove richiamare il picker
     * @param activity L'actitity dove mostrare l'inout
     */
    private Dialog2PickerBuilder (Context context, Activity activity)
    {
        builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.number_picker2, null);
        builder.setView(dialogView);

        setupDialog();
    }

    /**
     * Impostazioni preliminari
     * Richiama gli elementi dal layout
     */
    private void setupDialog(){
        firstTextView = dialogView.findViewById(R.id.firstTextNumberPicker2);
        secondTextView = dialogView.findViewById(R.id.secondTextNumberPicker2);
        titleTextView = dialogView.findViewById(R.id.titleNumberPicker2);

        firstPicker = dialogView.findViewById(R.id.firstPickerNumberPicker2);
        secondPicker = dialogView.findViewById(R.id.secondPickerNumberPicker2);
    }


    /**
     * Crea un nuovo builder di dialog all'interno di un activity
     * Restituisce un builder
     * @return il Builder
     */
    public static Dialog2PickerBuilder newBuilder(Context context, Activity activity) {
        return new Dialog2PickerBuilder(context, activity);
    }

    /**
     * Imposta il testo ad una textView
     * La textView è indicata dal numero passato come primo parametro
     *
     * @param number number = 0 -> titolo
     *      * number = 1 -> testo del primo picker
     *      * number = 2 -> testo del secondo picker
     * @param text Il testo da impostare nel picker
     * @return il Builder
     */
    public Dialog2PickerBuilder setText ( int number, String text ) {
        switch(number) {
            case 0:
                titleTextView.setText(text);
                break;
            case 1:
                firstTextView.setText(text);
                break;
            case 2:
                secondTextView.setText(text);
                break;
        }
        return this;
    }

    /**
     * Setta le impostazioni di un picker
     * Il picker è indicato dal numero passato come primo parametro
     * @param number number = 1 -> primo picker
     *      * number = 2 -> secondo picker
     * @param min Setta la selezione minima
     * @param max La selezione massima
     * @param onChange Imposta il comportamento al cambio di selezione sul picker
     * @return il Builder
     */
    public Dialog2PickerBuilder setPicker ( int number, int min, int max, NumberPicker.OnValueChangeListener onChange) {
        switch(number) {
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
        }
        return this;
    }

    /**
     * Setta le impostazioni di un picker
     * Il picker mostrerà le stringhe passate come parametro values
     * Il picker è indicato dal numero passato come primo parametro
     * @param number number = 1 -> primo picker
     *      * number = 2 -> secondo picker
     * @param min Il minimo valore che può assumere il picker
     * @param max Il massimo valore che può assumere il picker
     * @param values L'array di stringhe che saranno mostrate nel picker
     * @param onChange Il comportamento del picker quando cambia la selezione
     * @return il Builder
     */
    public Dialog2PickerBuilder setPicker ( int number, int min, int max, String[] values,  NumberPicker.OnValueChangeListener onChange) {
        this.setPicker(number,min,max,onChange);

        switch(number) {
            case 1:
                firstPicker.setDisplayedValues(values);
                break;
            case 2:
                secondPicker.setDisplayedValues(values);
                break;
        }
        return this;
    }

    /**
     * Imposta il button positivo del dialog
     * @param text Il testo del button
     * @param onClickListener Il comportamento alla pressione
     * @return il Builder
     */
    public Dialog2PickerBuilder setPositiveButton (String text, DialogInterface.OnClickListener onClickListener ) {
        builder.setPositiveButton( text, onClickListener );
        return this;
    }

    /**
     * Imposta il button negativo del dialog
     * @param text Il testo del button
     * @param onClickListener Il comportamento alla pressione
     * @return il Builder
     */
    public Dialog2PickerBuilder setNegativeButton ( String text, DialogInterface.OnClickListener onClickListener ) {
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
}
