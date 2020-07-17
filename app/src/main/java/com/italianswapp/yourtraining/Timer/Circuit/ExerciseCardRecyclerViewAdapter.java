package com.italianswapp.yourtraining.Timer.Circuit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.italianswapp.yourtraining.ExerciseTypeNotCorrectException;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.ExerciseSettings;
import com.italianswapp.yourtraining.Utilities;
import com.italianswapp.yourtraining.R;

import java.util.List;


public class ExerciseCardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int EXERCISE = 0;
    private static final int EMOM = 1;
    private static final int REST = 2;
    private static final int TABATA = 3;
    private static final int SUPERSET = 4;

    private final String[] TimeInStringForPicker = Utilities.TIME_IN_STRING;

    List<? extends ExerciseSettings> exercises;

    private CircuitCreatorActivity circuitCreatorActivity;

    private String workString, restString;
    private int repetition;

    public void setCircuitCreatorActivity(CircuitCreatorActivity circuitCreatorActivity){
        this.circuitCreatorActivity = circuitCreatorActivity;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case EXERCISE :
                //Esercizio
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_card, parent, false);
                return new ExerciseSettingsViewHolder(v);
            case EMOM:
                //Emom
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.emom_card, parent, false);
                return new EmomSettingsViewHolder(v);
            case REST:
                //Rest
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_card, parent, false);
                return new RestSettingsViewHolder(v);
            case TABATA:
                //Tabata
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tabata_card, parent, false);
                return new TabataSettingsViewHolder(v);
            case SUPERSET:
                //Superset
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.superset_card, parent, false);
                return new SupersetSettingsViewHolder(v);
            default:
                try {
                    throw new ExerciseTypeNotCorrectException();
                } catch (ExerciseTypeNotCorrectException e) {
                    e.printStackTrace();
                }
                return null; //viene lanciata l'eccezione quindi non viene ritornato mai null
        }
    }

    /**
     * Si occupa di ricaricare il contenuto delle card ogni volta che il recycler view viene aggiornato
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        /*
        Prendo l'oggetto in posizione position
         */
        ExerciseSettings exerciseSettings = exercises.get(position);
        /*
        In base al tipo imposta un diverso tipo di exercise (esercizio, tabata, rest, emom, ...)
         */
        switch (holder.getItemViewType()) {
            case EXERCISE:
                /*
                Esercizio:
                    Nome
                    Numero lap
                    Numero ripetizioni (o secondi)
                    Recupero
                 */

                ExerciseSettingsViewHolder exerciseSettingsViewHolder = (ExerciseSettingsViewHolder)holder;
                exerciseSettingsViewHolder.mExerciseName.setText(exerciseSettings.getName());
                exerciseSettingsViewHolder.mLap.setText(String.format("x %s", String.valueOf(
                        exerciseSettings.getRepetition())));

                exerciseSettingsViewHolder.mReps.setText(
                        exerciseSettings.isReps() ?
                                //Se è un esercizio a ripetizioni stampa x reps
                                exerciseSettings.getReps() + " reps" :
                                //Altrimenti stampa xx:yy
                            Utilities.getStringTimeNoHour(
                                    exerciseSettings.getReps()));

                if(exerciseSettings.isHasRecs())
                    exerciseSettingsViewHolder.mRec.setText(
                            Utilities.getStringTimeNoHour(

                                    exerciseSettings.getRec()));
                else {
                    exerciseSettingsViewHolder.mSleepImage.setVisibility(ImageView.INVISIBLE);
                    exerciseSettingsViewHolder.mRec.setVisibility(TextView.INVISIBLE);
                }

                break;
            case EMOM:
                /*
                EMOM:
                    Numero lap
                    Numero secondi per ogni lap
                 */

                EmomSettingsViewHolder emomSettingsViewHolder = (EmomSettingsViewHolder)holder;
                emomSettingsViewHolder.mLap.setText(String.format("x %s", String.valueOf(
                        exerciseSettings.getRepetition())));

                emomSettingsViewHolder.mReps.setText(
                                Utilities.getStringTimeNoHour(
                                        exerciseSettings.getReps()));
                break;
            case REST:
                /*
                Rest:
                    Secondi di recupero
                 */
                RestSettingsViewHolder restSettingsViewHolder = (RestSettingsViewHolder)holder;

                restSettingsViewHolder.mRec.setText(
                        Utilities.getStringTimeNoHour(
                                exerciseSettings.getRec()));
                break;
            case TABATA:
                /*
                Tabata:
                    Numero lap
                    Secondi ripetizioni
                    Secondi recupero
                 */
                TabataSettingsViewHolder tabataSettingsViewHolder = (TabataSettingsViewHolder)holder;
                tabataSettingsViewHolder.mLap.setText(String.format("x %s", String.valueOf(
                        exerciseSettings.getRepetition())));

                tabataSettingsViewHolder.mReps.setText(
                        Utilities.getStringTimeNoHour(
                                exerciseSettings.getReps()));

                tabataSettingsViewHolder.mRec.setText(
                        Utilities.getStringTimeNoHour(
                                exerciseSettings.getRec()));
                break;
            case SUPERSET:
                /*
                Primo esercizio:
                    Nome
                    Numero lap
                    Numero ripetizioni (o secondi)
                    Recupero
                 Secondo esercizio:
                    Nome
                    Ripetizioni (o secondi)
                 */
                SupersetSettingsViewHolder supersetSettingsViewHolder = (SupersetSettingsViewHolder) holder;
                /*
                Primo esercizio
                 */
                supersetSettingsViewHolder.mFirstExerciseName.setText(exerciseSettings.getName());
                supersetSettingsViewHolder.mLap.setText(String.format("x %s", String.valueOf(
                        exerciseSettings.getRepetition())));

                supersetSettingsViewHolder.mFirstExerciseReps.setText(
                        exerciseSettings.isReps() ?
                                //Se è un esercizio a ripetizioni stampa x reps
                                exerciseSettings.getReps() + " reps" :
                                //Altrimenti stampa xx:yy
                                Utilities.getStringTimeNoHour(
                                        exerciseSettings.getReps()));

                supersetSettingsViewHolder.mRec.setText(
                        Utilities.getStringTimeNoHour(
                                exerciseSettings.getRec()));
                /*
                Secondo esercizio
                 */
                ExerciseSettings.SupersetExercise supersetExercise = null;
                try {
                    supersetExercise = exerciseSettings.getSupersetExercise();
                } catch (ExerciseTypeNotCorrectException e) {
                    e.printStackTrace();
                }

                supersetSettingsViewHolder.mSecondExerciseName.setText(supersetExercise.getName());

                supersetSettingsViewHolder.mSecondExerciseReps.setText(
                        supersetExercise.isReps() ?
                                //Se è un esercizio a ripetizioni stampa x reps
                                supersetExercise.getReps() + " reps" :
                                //Altrimenti stampa xx:yy
                                Utilities.getStringTimeNoHour(
                                        supersetExercise.getReps()));

                break;
            default:
                try {
                    throw new ExerciseTypeNotCorrectException();
                } catch (ExerciseTypeNotCorrectException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    /**
     * Ritorna il tipo dell'esercizio di tipo intero
     * La conversione viene fatta dal metodo statico getIntegerItemType della classe ExerciseSettings
     * @param position posizione dell'elemento nella lista
     * @return Il tipo intero dell'elemento
     */
    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        try {
            return ExerciseSettings.getIntegerItemType(exercises.get(position).getType());
        } catch (ExerciseTypeNotCorrectException e) {
            e.printStackTrace();
        }
        return -1; //in realtà non viene mai ritornato perché o ritorna il valore o lancia l'eccezione
    }

    private void removeItem(int position) {
        exercises.remove(position);
        notifyItemRemoved(position);
    }

    public ExerciseCardRecyclerViewAdapter(List<? extends
            ExerciseSettings> exercises) {
        this.exercises = exercises;
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    /*
    *  Classi innestate che gestiscono ognuna un tipo di esercizio diverso
     */


    /**
     * Classe innestata per la gestione della card per la creazione di esercizi
     */
    public class ExerciseSettingsViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView mExerciseName, mLap, mReps, mRec;
        ImageButton mDeleteButton;
        ImageView mSleepImage;

        public ExerciseSettingsViewHolder(@NonNull final View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.exerciseCard);
            mExerciseName = itemView.findViewById(R.id.exerciseNameExerciseCard);
            mLap = itemView.findViewById(R.id.lapExerciseCard);
            mDeleteButton = itemView.findViewById(R.id.deleteExerciseCard);
            mReps = itemView.findViewById(R.id.repsExerciseCard);
            mRec = itemView.findViewById(R.id.recExerciseCard);
            mSleepImage = itemView.findViewById(R.id.sleepImage);

            /*
            Azione alla pressione del tasto delete
             */

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        removeItem(position);
                    }
                }
            });

            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    circuitCreatorActivity.showExerciseDialog(position);
                }
            });
        }

    }

    /**
     * Classe innestata per la gestione della card per la creazione di riposi
     */
    public class RestSettingsViewHolder extends RecyclerView.ViewHolder {

        CardView mCardView;
        TextView  mRec;
        ImageButton mDeleteButton;

        public RestSettingsViewHolder(@NonNull View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.tabataCard);
            mDeleteButton = itemView.findViewById(R.id.deleteRestCard);
            mRec = itemView.findViewById(R.id.recRestCard);

            /*
            Azione alla pressione del tasto delete
             */

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        removeItem(position);
                    }
                }
            });

            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    circuitCreatorActivity.showRestDialog(position);
                }
            });
        }
    }

    /**
     * Classe innestata per la gestione della card per la creazione di Tabata
     */
    public class TabataSettingsViewHolder extends RecyclerView.ViewHolder {

        CardView mCardView;
        TextView mLap, mReps, mRec;
        ImageButton mDeleteButton;

        public TabataSettingsViewHolder(@NonNull View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.tabataCard);
            mLap = itemView.findViewById(R.id.lapTabataCard);
            mDeleteButton = itemView.findViewById(R.id.deleteTabataCard);
            mReps = itemView.findViewById(R.id.repsTabataCard);
            mRec = itemView.findViewById(R.id.recTabataCard);

            /*
            Azione alla pressione del tasto delete
             */

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        removeItem(position);
                    }
                }
            });

            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    circuitCreatorActivity.showTabataDialog(position);
                }
            });

        }
    }


    public class EmomSettingsViewHolder extends RecyclerView.ViewHolder{

        CardView mCardView;
        TextView mLap, mReps;
        ImageButton mDeleteButton;

        public EmomSettingsViewHolder(@NonNull View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.emomCardView);
            mLap = itemView.findViewById(R.id.lapEmomCard);
            mDeleteButton = itemView.findViewById(R.id.deleteEmomCard);
            mReps = itemView.findViewById(R.id.repsEmomCard);

            /*
            Azione alla pressione del tasto delete
             */

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        removeItem(position);
                    }
                }
            });

            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    circuitCreatorActivity.showEmomDialog(position);
                }
            });
        }
    }

    public class SupersetSettingsViewHolder extends RecyclerView.ViewHolder {

        CardView mCardView;
        TextView mFirstExerciseName, mSecondExerciseName, mFirstExerciseReps,
                mSecondExerciseReps, mLap, mRec;
        ImageButton mDeleteButton;

        public SupersetSettingsViewHolder(@NonNull View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.supersetCard);
            mLap = itemView.findViewById(R.id.lapSupersetCard);
            mDeleteButton = itemView.findViewById(R.id.deleteSupersetCard);
            mFirstExerciseName = itemView.findViewById(R.id.firstExerciseSupersetCard);
            mFirstExerciseReps = itemView.findViewById(R.id.firstExerciseRepsSupersetCard);
            mSecondExerciseName = itemView.findViewById(R.id.secondExerciseSupersetCard);
            mSecondExerciseReps = itemView.findViewById(R.id.secondExerciseRepsSupersetCard);
            mRec = itemView.findViewById(R.id.recSupersetCard);

            /*
            Azione alla pressione del tasto delete
             */

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        removeItem(position);
                    }
                }
            });

            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    circuitCreatorActivity.showSupersetDialog(position);
                }
            });
        }
    }

}
