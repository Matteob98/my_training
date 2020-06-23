package com.italianswapp.yourtraining.Timer.Circuit;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.ExerciseSettings;
import com.italianswapp.yourtraining.Utilities;
import com.italianswapp.yourtraining.R;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;
import com.nex3z.togglebuttongroup.ToggleButtonGroup;

import java.util.Arrays;
import java.util.List;


public class ExerciseCardRecyclerViewAdapter extends RecyclerView.Adapter<ExerciseCardRecyclerViewAdapter.ExerciseSettingsViewHolder> {

    private final String[] TimeInStringForPicker = Utilities.TIME_IN_STRING;

    List<ExerciseSettings> exercises;

    @NonNull
    @Override
    public ExerciseSettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_card, parent, false);
        return new ExerciseSettingsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExerciseSettingsViewHolder exerciseSettingsViewHolder, int position) {

        /*
        Imposta il valore delle ripetizioni al valore salvato
         */
        exerciseSettingsViewHolder.mRepsPicker.setValue(exercises.get(position).getReps());
        /*
        Imposta il valore del recupero al valore salvato
        Prende l'indice del valore in exercise all'interno dell'array TimeInStringForPicker
         */
        exerciseSettingsViewHolder.mRestPicker.setValue(
                Arrays.asList(TimeInStringForPicker)
                        .indexOf(
                                Utilities.getStringTimeNoHour(
                                        exercises.get(position).getRec())));

        /*
        Imposta il nome dell'esercizio nella text view in alto al valore salvato
         */
        exerciseSettingsViewHolder.exerciseName.setText(exercises.get(position).getName());

        /*
        Imposto i tre select al valore memorizzato
         */
        if (exercises.get(position).isReps())
            exerciseSettingsViewHolder.mRepsSecsSelect.setCheckedAt(0, true);
        else
            exerciseSettingsViewHolder.mRepsSecsSelect.setCheckedAt(1, true);

        if (exercises.get(position).isHasRecs())
            exerciseSettingsViewHolder.mRestNoRestSelect.setCheckedAt(0, true);
        else
            exerciseSettingsViewHolder.mRestNoRestSelect.setCheckedAt(1, true);

        int nr = exercises.get(position).getRepetition()-1;
        if (nr < 0 )
            nr = 0;
        exerciseSettingsViewHolder.mRepetitionNumberSelect.setCheckedAt(nr, true);

    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    /**
     * Classe innestata per la gestione della card per la creazione di esercizi
     */
    public class ExerciseSettingsViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        NumberPicker mRepsPicker, mRestPicker;
        SingleSelectToggleGroup mRepsSecsSelect, mRestNoRestSelect, mRepetitionNumberSelect;
        EditText exerciseName;
        TextInputLayout mExerciseTextInputLayout;
        Button deleteButton, arrowBtn;
        ConstraintLayout expandableView;

        public ExerciseSettingsViewHolder(@NonNull final View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.exerciseCard);
            mRepsPicker = itemView.findViewById(R.id.repsPickerExerciseCard);
            mRestPicker = itemView.findViewById(R.id.restPickerExerciseCard);
            mRepsSecsSelect = itemView.findViewById(R.id.repsSecsSingleSelect);
            mRestNoRestSelect = itemView.findViewById(R.id.restNoRestSingleSelect);
            mRepetitionNumberSelect = itemView.findViewById(R.id.repetitionNumberSingleSelect);
            exerciseName = itemView.findViewById(R.id.exerciseNameExerciseCard);
            deleteButton = itemView.findViewById(R.id.deleteExerciseCard);
            expandableView = itemView.findViewById(R.id.expandableViewExerciseCard);
            arrowBtn = itemView.findViewById(R.id.arrowButtonExerciseCard);
            mExerciseTextInputLayout = itemView.findViewById(R.id.textInputLayoutExerciseCard);

            mRepsPicker.setMinValue(1);
            mRepsPicker.setMaxValue(120);

            mRestPicker.setMinValue(0);
            mRestPicker.setMaxValue(TimeInStringForPicker.length-1);
            mRestPicker.setDisplayedValues(TimeInStringForPicker);

            Utilities.changeDividerColor(mRepsPicker, Color.parseColor("#00ffffff"));
            Utilities.changeDividerColor(mRestPicker, Color.parseColor("#00ffffff"));

            arrowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandableView.getVisibility()==View.GONE){
                        TransitionManager.beginDelayedTransition(mCardView, new AutoTransition());
                        expandableView.setVisibility(View.VISIBLE);
                        arrowBtn.setBackgroundResource(R.drawable.ic_arrow_up);
                    } else {
                        TransitionManager.beginDelayedTransition(mCardView, new AutoTransition());
                        expandableView.setVisibility(View.GONE);
                        arrowBtn.setBackgroundResource(R.drawable.ic_arrow_down);
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        removeItem(position);
                    }
                }

            });

            mRepsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    exercises.get(getAdapterPosition()).setReps(newVal);
                }
            });

            /*
             Picker che imposta il recupero dell'esercizio
            */
            mRestPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    exercises.get(getAdapterPosition()).setRec(Utilities.getMillsFromMinuteString(TimeInStringForPicker[newVal]));
                }
            });

            /*
                Edit text che gestisce il nome dell'esercizio
            */
            exerciseName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    exercises.get(getAdapterPosition()).setName(exerciseName.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    exercises.get(getAdapterPosition()).setName(exerciseName.getText().toString());
                }
            });

            /*
                Gestisce il click sul primo single select
                Se 0 è un esercizio a ripetizioni
                Se 1 è un esercizio a tempo
             */
            mRepsSecsSelect.setOnCheckedChangeListener(new ToggleButtonGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChange(int pos, boolean isChecked) {
                    if(pos==0)
                        exercises.get(getAdapterPosition()).setReps(true);
                    else
                        exercises.get(getAdapterPosition()).setReps(false);
                }
            });

             /*
                Gestisce il click sul secondo single select
                Se 0 è un esercizio con riposo
                Se 1 è un esercizio senza riposo
             */
            mRestNoRestSelect.setOnCheckedChangeListener(new ToggleButtonGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChange(int pos, boolean isChecked) {
                    if(pos==0)
                        exercises.get(getAdapterPosition()).setHasRecs(true);
                    else
                        exercises.get(getAdapterPosition()).setHasRecs(false);
                }
            });

            /*
                Gestisce il click sul terzo single select
                Gestisce il numero di volte che va ripetuto un esercizio
                pos contiene il numero di volte -1
             */
            mRepetitionNumberSelect.setOnCheckedChangeListener(new ToggleButtonGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChange(int pos, boolean isChecked) {
                    exercises.get(getAdapterPosition()).setRepetition(pos+1);
                }
            });

            exerciseName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus) {
                        if(exerciseName.getText().toString().isEmpty()) {
                            mExerciseTextInputLayout.setErrorEnabled(true);
                            mExerciseTextInputLayout.setError(v.getResources().getString(R.string.exercise_name_error));
                        }
                        else
                            mExerciseTextInputLayout.setErrorEnabled(false);
                    }
                }
            });
        }

    }

    private void removeItem(int position) {
        exercises.remove(position);
        notifyItemRemoved(position);
    }

    public ExerciseCardRecyclerViewAdapter(List<ExerciseSettings> exercises) {
        this.exercises = exercises;
    }
}
