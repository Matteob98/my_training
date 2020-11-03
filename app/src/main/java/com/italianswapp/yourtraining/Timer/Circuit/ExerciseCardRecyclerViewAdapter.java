package com.italianswapp.yourtraining.Timer.Circuit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.italianswapp.yourtraining.ExerciseTypeNotCorrectException;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.Exercise;
import com.italianswapp.yourtraining.Utilities.Utilities;
import com.italianswapp.yourtraining.R;

import java.util.List;


public class ExerciseCardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int EXERCISE = 0;
    private static final int EMOM = 1;
    private static final int REST = 2;
    private static final int TABATA = 3;
    private static final int SUPERSET = 4;

    List<? extends Exercise> exercises;

    private CircuitCreatorActivity circuitCreatorActivity = null;

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
                    throw new ExerciseTypeNotCorrectException("Tipo non corretto in RecyclerView.ViewHolder onCreateViewHolder");
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
        Exercise exercise = exercises.get(position);
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
                exerciseSettingsViewHolder.mExerciseName.setText(exercise.getName());
                exerciseSettingsViewHolder.mLap.setText(String.format("x %s", String.valueOf(
                        exercise.getRepetition())));

                exerciseSettingsViewHolder.mReps.setText(
                        exercise.isReps() ?
                                //Se è un esercizio a ripetizioni stampa x reps
                                exercise.getReps() + " reps" :
                                //Altrimenti stampa xx:yy
                            Utilities.getStringTimeNoHour(
                                    exercise.getReps()));

                if(exercise.isHasRecs())
                    exerciseSettingsViewHolder.mRec.setText(
                            Utilities.getStringTimeNoHour(

                                    exercise.getRec()));
                else {
                    exerciseSettingsViewHolder.mSleepImage.setVisibility(ImageView.INVISIBLE);
                    exerciseSettingsViewHolder.mRec.setVisibility(TextView.INVISIBLE);
                }

                break;
            case EMOM:
                /*
                EMOM:
                    Titolo
                    Numero lap
                    Numero secondi per ogni lap
                 */


                EmomSettingsViewHolder emomSettingsViewHolder = (EmomSettingsViewHolder)holder;
                if(!exercise.getName().equals(""))
                    emomSettingsViewHolder.mTitle.setText(exercise.getName());

                emomSettingsViewHolder.mLap.setText(String.format("x %s", String.valueOf(
                        exercise.getRepetition())));

                emomSettingsViewHolder.mReps.setText(
                                Utilities.getStringTimeNoHour(
                                        exercise.getReps()));
                break;
            case REST:
                /*
                Rest:
                    Secondi di recupero
                 */
                RestSettingsViewHolder restSettingsViewHolder = (RestSettingsViewHolder)holder;

                restSettingsViewHolder.mRec.setText(
                        Utilities.getStringTimeNoHour(
                                exercise.getRec()));
                break;
            case TABATA:
                /*
                Tabata:
                    Titolo
                    Numero lap
                    Secondi ripetizioni
                    Secondi recupero
                 */
                TabataSettingsViewHolder tabataSettingsViewHolder = (TabataSettingsViewHolder)holder;
                if(!exercise.getName().equals(""))
                    tabataSettingsViewHolder.mTitle.setText(exercise.getName());
                tabataSettingsViewHolder.mLap.setText(String.format("x %s", String.valueOf(
                        exercise.getRepetition())));

                tabataSettingsViewHolder.mReps.setText(
                        Utilities.getStringTimeNoHour(
                                exercise.getReps()));

                tabataSettingsViewHolder.mRec.setText(
                        Utilities.getStringTimeNoHour(
                                exercise.getRec()));
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
                supersetSettingsViewHolder.mFirstExerciseName.setText(exercise.getName());
                supersetSettingsViewHolder.mLap.setText(String.format("x %s", String.valueOf(
                        exercise.getRepetition())));

                supersetSettingsViewHolder.mFirstExerciseReps.setText(
                        exercise.isReps() ?
                                //Se è un esercizio a ripetizioni stampa x reps
                                exercise.getReps() + " reps" :
                                //Altrimenti stampa xx:yy
                                Utilities.getStringTimeNoHour(
                                        exercise.getReps()));

                supersetSettingsViewHolder.mRec.setText(
                        Utilities.getStringTimeNoHour(
                                exercise.getRec()));
                /*
                Secondo esercizio
                 */
                Exercise.SupersetExercise supersetExercise = null;
                try {
                    supersetExercise = exercise.getSupersetExercise();
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
                    throw new ExerciseTypeNotCorrectException("Tipo non corretto in onBindViewHolder");
                } catch (ExerciseTypeNotCorrectException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    /**
     * Ritorna il tipo dell'esercizio di tipo intero
     * La conversione viene fatta dal metodo statico getIntegerItemType della classe Exercise
     * @param position posizione dell'elemento nella lista
     * @return Il tipo intero dell'elemento
     */
    @Override
    public int getItemViewType(int position) {
        try {
            return Exercise.getIntegerItemType(exercises.get(position).getType());
        } catch (ExerciseTypeNotCorrectException e) {
            e.printStackTrace();
        }
        return -1; //in realtà non viene mai ritornato perché o ritorna il valore o lancia l'eccezione
    }

    private void removeItem(int position) {
        exercises.remove(position);
        notifyItemRemoved(position);
    }

    public ExerciseCardRecyclerViewAdapter(List<? extends Exercise> exercises) {
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
            Disattivo azioni se mi trovo nell exercise list dialog
             */
            if (circuitCreatorActivity==null) {
                mCardView.setClickable(false);
                mDeleteButton.setVisibility(Button.INVISIBLE);
            }

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
                    if(circuitCreatorActivity!=null)
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
            Disattivo azioni se mi trovo nell exercise list dialog
             */
            if (circuitCreatorActivity==null) {
                mCardView.setClickable(false);
                mDeleteButton.setVisibility(Button.INVISIBLE);
            }

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
                    if(circuitCreatorActivity!=null)
                        circuitCreatorActivity.showRestDialog(position);
                }
            });
        }
    }

    /**
     * Classe innestata per la gestione della card per la creazione di Tabata
     */
    public class TabataSettingsViewHolder extends RecyclerView.ViewHolder{

        CardView mCardView;
        TextView mTitle, mLap, mReps, mRec;
        ImageButton mDeleteButton;

        public TabataSettingsViewHolder(@NonNull View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.tabataCard);
            mTitle = itemView.findViewById(R.id.titleEmomCardView);
            mLap = itemView.findViewById(R.id.lapTabataCard);
            mDeleteButton = itemView.findViewById(R.id.deleteTabataCard);
            mReps = itemView.findViewById(R.id.repsTabataCard);
            mRec = itemView.findViewById(R.id.recTabataCard);

            /*
            Disattivo azioni se mi trovo nell exercise list dialog
             */
            if (circuitCreatorActivity==null) {
                mCardView.setClickable(false);
                mDeleteButton.setVisibility(Button.INVISIBLE);
            }

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
                    if(circuitCreatorActivity!=null)
                        circuitCreatorActivity.showTabataDialog(position);
                }
            });

        }
    }


    public class EmomSettingsViewHolder extends RecyclerView.ViewHolder{

        CardView mCardView;
        TextView mTitle, mLap, mReps;
        ImageButton mDeleteButton;

        public EmomSettingsViewHolder(@NonNull View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.emomCardView);
            mTitle = itemView.findViewById(R.id.titleEmomCardView);
            mLap = itemView.findViewById(R.id.lapEmomCard);
            mDeleteButton = itemView.findViewById(R.id.deleteEmomCard);
            mReps = itemView.findViewById(R.id.repsEmomCard);

            /*
            Disattivo azioni se mi trovo nell exercise list dialog
             */
            if (circuitCreatorActivity==null) {
                mCardView.setClickable(false);
                mDeleteButton.setVisibility(Button.INVISIBLE);
            }

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
                    if(circuitCreatorActivity!=null)
                        circuitCreatorActivity.showEmomDialog(position);
                }
            });
        }
    }

    public class SupersetSettingsViewHolder extends RecyclerView.ViewHolder{

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
            Disattivo azioni se mi trovo nell exercise list dialog
             */
            if (circuitCreatorActivity==null) {
                mCardView.setClickable(false);
                mDeleteButton.setVisibility(Button.INVISIBLE);
            }

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
                    if(circuitCreatorActivity!=null) {
                        try {
                            circuitCreatorActivity.showSupersetDialog(position);
                        } catch (ExerciseTypeNotCorrectException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

}
