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
public class Dialog3PickerBuilder
{
    private TextView firstTextView, secondTextView, thirdTextView, titleTextView;
    private NumberPicker firstPicker, secondPicker, thirdPicker;

    private AlertDialog.Builder builder;
    private final View dialogView;

    /**
     * Metodo privato
     * Costruttore del builder di una picker
     * Se chiamato in un activity
     * @param context Il contesto
     * @param activity L'activity dove mostrare il picker
     */
    private Dialog3PickerBuilder (Context context, Activity activity)
    {
        builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.number_picker3, null);
        builder.setView(dialogView);

        setupDialog();
    }

    /**
     * Impostazioni preliminari
     * Richiama gli elementi dal layout
     */
    private void setupDialog(){
        firstTextView = dialogView.findViewById(R.id.firstTextNumberPicker3);
        secondTextView = dialogView.findViewById(R.id.secondTextNumberPicker3);
        thirdTextView = dialogView.findViewById(R.id.thirdTextNumberPicker3);
        titleTextView = dialogView.findViewById(R.id.titleNumberPicker3);

        firstPicker = dialogView.findViewById(R.id.firstPickerNumberPicker3);
        secondPicker = dialogView.findViewById(R.id.secondPickerNumberPicker3);
        thirdPicker = dialogView.findViewById(R.id.thirdPickerNumberPicker3);
    }

    /**
     * Crea un nuovo builder di dialog all'interno di un activity
     * Restituisce un builder
     * @param context Il contesto
     * @param activity L'activity dove mostrare il picker
     * @return il Builder
     */
    public static Dialog3PickerBuilder newBuilder(Context context, Activity activity) {
        return new Dialog3PickerBuilder(context, activity);
    }

    /**
     * Imposta il testo ad una textView
     * La textView è indicata dal numero passato come primo parametro
     * @param number  number = 0 -> titolo
     *      * number = 1 -> testo del primo picker
     *      * number = 2 -> testo del secondo picker
     *      * number = 3 -> testo del terzo picker
     * @param text Il testo da mostrare sotto il picker
     * @return il Builder
     */
    public Dialog3PickerBuilder setText ( int number, String text ) {
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
            case 3:
                thirdTextView.setText(text);
                break;
        }
        return this;
    }

    /**
     * Setta le impostazioni di un picker
     * Il picker è indicato dal numero passato come primo parametro
     * @param number number = 1 -> primo picker
     *      * number = 2 -> secondo picker
     *      * number = 3 -> terzo picker
     * @param min Il valore minimo che può assumere
     * @param max Il valore massimo che può assumere
     * @param onChange Il comportamento quando cambia la selezione
     * @return il Builder
     */
    public Dialog3PickerBuilder setPicker ( int number, int min, int max, NumberPicker.OnValueChangeListener onChange) {
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
            case 3:
                thirdPicker.setMinValue(min);
                thirdPicker.setMaxValue(max);
                thirdPicker.setOnValueChangedListener(onChange);
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
     *      * number = 3 -> terzo picker
     * @param min Il valore minimo che può assumere
     * @param max Il valore massimo che può assumere
     * @param values L'array di stringhe che contiene i valori selezionabili dal picker
     * @param onChange Il comportamento quando cambia la selezione
     * @return il Builder
     */
    public Dialog3PickerBuilder setPicker ( int number, int min, int max, String[] values,  NumberPicker.OnValueChangeListener onChange) {
        this.setPicker(number,min,max,onChange);

        switch(number) {
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
     * Imposta il button positivo del dialog
     * @param text Il testo del button
     * @param onClickListener Il comportamento alla pressione
     * @return il Builder
     */
    public Dialog3PickerBuilder setPositiveButton (String text, DialogInterface.OnClickListener onClickListener ) {
        builder.setPositiveButton( text, onClickListener );
        return this;
    }

    /**
     * Imposta il button negativo del dialog
     * @param text Il testo del button
     * @param onClickListener Il comportamento alla pressione
     * @return il Builder
     */
    public Dialog3PickerBuilder setNegativeButton ( String text, DialogInterface.OnClickListener onClickListener ) {
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
