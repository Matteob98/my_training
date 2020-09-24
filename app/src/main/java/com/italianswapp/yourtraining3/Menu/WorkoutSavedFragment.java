package com.italianswapp.yourtraining3.Menu;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.italianswapp.yourtraining3.Chronometer.ChronometerActivity;
import com.italianswapp.yourtraining3.DialogBuilders.Dialog2PickerBuilder;
import com.italianswapp.yourtraining3.FinishActivity;
import com.italianswapp.yourtraining3.OfflineDatabase.WorkoutDatabase;
import com.italianswapp.yourtraining3.OfflineDatabase.WorkoutSaved;
import com.italianswapp.yourtraining3.R;
import com.italianswapp.yourtraining3.Timer.Circuit.CircuitCreatorActivity;
import com.italianswapp.yourtraining3.Timer.Circuit.CircuitSettings.Exercise;
import com.italianswapp.yourtraining3.Timer.CountDownTimers.AmrapActivity;
import com.italianswapp.yourtraining3.Timer.CountDownTimers.EmomActivity;
import com.italianswapp.yourtraining3.Timer.CountDownTimers.NegativePhaseActivity;
import com.italianswapp.yourtraining3.Timer.CountDownTimers.TabataActivity;
import com.italianswapp.yourtraining3.Timer.TimerActivity;
import com.italianswapp.yourtraining3.Utilities.RecyclerItemClickListener;
import com.italianswapp.yourtraining3.Utilities.Utilities;
import com.italianswapp.yourtraining3.WorkoutProposed.Workout.Workout;
import com.italianswapp.yourtraining3.WorkoutProposed.WorkoutSummaryRecyclerViewAdapter;
import com.italianswapp.yourtraining3.WorkoutSaved.WorkoutSavedRecyclerViewAdapter;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class WorkoutSavedFragment extends Fragment {

    /*
    Todo mostrare immagine quando la recycler view è vuota
     */

    private View view;

    private Button mFilterButton;
    private RecyclerView mRecyclerView;

    /**
     * Lista completa degli allenamenti caricata dal dib
     */
    private List<WorkoutSaved> workoutSavedList;
    /**
     * Lista degli allenamenti che hanno superato i filtri
     */
    private List<WorkoutSaved> visibleWorkoutSavedList;

    /*
    Filtri
     */
    private String titleFilter;
    private Date fromDateFilter, toDateFilter;
    private WorkoutSaved.WorkoutSensation sensationFilter;
    private Workout.WorkoutLevel levelFilter;

    /*
    Oggetti XML per i filtri
     */

    private ImageButton mEasySensationButton, mNormalSensationButton, mDifficultSensationButton,
            mBeginnerLevelButton, mIntermediateLevelButton, mAdvancedLevelButton;
    AlertDialog alertDialog;



    public WorkoutSavedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_workouts_saved, container, false);

        layoutSettings();

        WorkoutDatabase db = WorkoutDatabase.getInMemoryDatabase(getActivity());
        workoutSavedList = db.workoutDao().getAll();
        visibleWorkoutSavedList = Utilities.arrayCopy(workoutSavedList);

        recyclerViewSettings();

        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterSettings();
            }
        });

        return view;
    }

    private void layoutSettings() {
        mFilterButton = view.findViewById(R.id.filterButtonWorkoutSaved);
        mRecyclerView = view.findViewById(R.id.recyclerViewWorkoutSaved);
    }

    private void recyclerViewSettings() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(new WorkoutSavedRecyclerViewAdapter(visibleWorkoutSavedList, getActivity()));
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                loadSavedWorkout(visibleWorkoutSavedList.get(position));
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //Non fare niente
            }
        }));
    }

    private void filterSettings() {

        /*
        Creo il builder del dialog
         */
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.filter_workout_saved, null);
        builder.setView(dialogView);

        /*
        Titolo
         */
        final EditText mTitle = dialogView.findViewById(R.id.titleEditTextFilterWorkoutSaved);
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Fai niente
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Fai niente
            }

            @Override
            public void afterTextChanged(Editable s) {
                titleFilter = mTitle.getText().toString();
            }
        });
        /*
        Data
         */
        //Salvo giorno mese e anno corrente
        Calendar calendar = Calendar.getInstance();
        final int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        final int currentMonth = calendar.get(Calendar.MONTH) ;
        final int currentYear = calendar.get(Calendar.YEAR);

        //Richiamo gli oggetti XML e inizializzo la data
        final TextView mFromDateText = dialogView.findViewById(R.id.fromDateTextViewFilterWorkoutSaved);
        final TextView mToDateText = dialogView.findViewById(R.id.toDateTextViewFilterWorkoutSaved);
        final CardView mFromDateCard = dialogView.findViewById(R.id.fromDateCardFilterWorkoutSaved);
        CardView mToDateCard = dialogView.findViewById(R.id.toDateCardFilterWorkoutSaved);

        setDataText(mToDateText, currentDay, currentMonth, currentYear);

        mFromDateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fromDateFilter = new Date(year, month, dayOfMonth);
                        setDataText(mFromDateText, dayOfMonth, month, year);

                        if (toDateFilter==null || fromDateFilter.compareTo(toDateFilter) > 0 ) {
                            toDateFilter = fromDateFilter;
                            setDataText(mToDateText, dayOfMonth, month, year);
                        }
                    }
                },
                        currentYear,
                        currentMonth,
                        currentDay );
                datePickerDialog.show();
            }
        });

        mToDateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        toDateFilter = new Date(year, month, dayOfMonth);
                        setDataText(mToDateText, dayOfMonth, month, year);

                        if (fromDateFilter==null || toDateFilter.compareTo(fromDateFilter) < 0 ) {
                            fromDateFilter = toDateFilter;
                            setDataText(mFromDateText, dayOfMonth, month, year);
                        }
                    }
                },
                        currentYear,
                        currentMonth,
                        currentDay );
                datePickerDialog.show();
            }
        });


        /*
        Sensation
         */
        mEasySensationButton = dialogView.findViewById(R.id.easySensationButtonFilterWorkoutSaved);
        mNormalSensationButton = dialogView.findViewById(R.id.normalSensationButtonFilterWorkoutSaved);
        mDifficultSensationButton = dialogView.findViewById(R.id.difficultSensationButtonFilterWorkoutSaved);
        setSmileImage(false, false, false);

        /*
        Imposto l'onClick delle sensazioni
         */
        mEasySensationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Se clicco due volte su facile deseleziona il filtro
                 */
                if (sensationFilter != WorkoutSaved.WorkoutSensation.EASY) {
                    sensationFilter = WorkoutSaved.WorkoutSensation.EASY;
                    setSmileImage(true, false, false);
                }
                else {
                    sensationFilter = null;
                    setSmileImage(false, false, false);
                }
            }
        });
        mNormalSensationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensationFilter = WorkoutSaved.WorkoutSensation.NORMAL;
                setSmileImage(true, true, false);
            }
        });
        mDifficultSensationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensationFilter = WorkoutSaved.WorkoutSensation.DIFFICULT;
                setSmileImage(true, true, true);
            }
        });
        /*
        Level
         */

        mBeginnerLevelButton = dialogView.findViewById(R.id.beginnerLevelButtonFilterWorkoutSaved);
        mIntermediateLevelButton = dialogView.findViewById(R.id.intermediateLevelButtonFilterWorkoutSaved);
        mAdvancedLevelButton = dialogView.findViewById(R.id.advancedLevelButtonFilterWorkoutSaved);
        setLevelImage(false, false, false);

        /*
        Imposto l'onClick dei livelli
         */
        mBeginnerLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (levelFilter!= Workout.WorkoutLevel.BEGINNER) {
                    levelFilter = Workout.WorkoutLevel.BEGINNER;
                    setLevelImage(true, false, false);
                }
                else {
                    levelFilter = null;
                    setLevelImage(false, false, false);
                }
            }
        });
        mIntermediateLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelFilter = Workout.WorkoutLevel.INTERMEDIATE;
                setLevelImage(true, true, false);
            }
        });
        mAdvancedLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelFilter = Workout.WorkoutLevel.ADVANCED;
                setLevelImage(true, true, true);
            }
        });

        /*
        Save
         */
        Button mSaveButton = dialogView.findViewById(R.id.saveButtonFilterWorkoutSaved);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(titleFilter != null && (!titleFilter.equals("")))
                    applyTitleFilter();
                if(fromDateFilter!=null || toDateFilter!=null)
                    applyDateFilter();
                if(sensationFilter!=null)
                    applySensationFilter();
                if(levelFilter!=null)
                    applyLevelFilter();

                recyclerViewSettings();
                alertDialog.dismiss();
            }
        });
        /*
        Reset
         */
        Button mResetButton = dialogView.findViewById(R.id.resetButtonFilterWorkoutSaved);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Azzera variabili
                 */
                titleFilter = null;
                fromDateFilter = null;
                toDateFilter = null;
                sensationFilter = null;
                levelFilter = null;

                /*
                Azzera valori visibili
                 */
                mTitle.setText("");
                setSmileImage(false, false, false);
                setLevelImage(false, false, false);
                mFromDateText.setText(getResources()
                        .getString(R.string.start_date));

                setDataText(mToDateText, currentDay, currentMonth, currentYear);

                resetFilter();

            }
        });
        /*
        Creo il dialog
         */
        alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    /**
     * Imposta nella textView passata come argomento la data in formato giorno/mese/anno
     * @param dataTextView text view da modificare
     * @param dayOfMonth giorno
     * @param month mese
     * @param year anno
     */
    private void setDataText(TextView dataTextView, int dayOfMonth, int month, int year) {
        month++;
        dataTextView.setText(new StringBuilder()
                .append(dayOfMonth)
                .append("/")
                .append(month)
                .append("/")
                .append(year)
                .toString());
    }

    /**
     * Imposta l'immagine dei button del livello in base ai valori passati. Se true imposta il
     * selezionato, altrimenti imposta il deselezionato
     * @param first Livello principiante
     * @param second Livello intermedio
     * @param third Livello avanzato
     */
    private void setLevelImage(boolean first, boolean second, boolean third) {
        if(first)
            mBeginnerLevelButton.setImageResource(R.drawable.ic_level_filled);
        else
            mBeginnerLevelButton.setImageResource(R.drawable.ic_level_empty);
        if(second)
            mIntermediateLevelButton.setImageResource(R.drawable.ic_level_filled);
        else
            mIntermediateLevelButton.setImageResource(R.drawable.ic_level_empty);
        if(third)
            mAdvancedLevelButton.setImageResource(R.drawable.ic_level_filled);
        else
            mAdvancedLevelButton.setImageResource(R.drawable.ic_level_empty);
    }

    /**
     * Imposta l'immagine dei button delle sensazioni in base ai valori passati, se true imposta
     * Il selezionato, se false imposta il deselezionato
     * @param first sensazione facile
     * @param second sensazione intermedia
     * @param third sensazione difficile
     */
    private void setSmileImage(boolean first, boolean second, boolean third) {
        if(first)
            mEasySensationButton.setImageResource(R.drawable.ic_smile_green);
        else
            mEasySensationButton.setImageResource(R.drawable.ic_smile);
        if(second)
            mNormalSensationButton.setImageResource(R.drawable.ic_smile_green);
        else
            mNormalSensationButton.setImageResource(R.drawable.ic_smile);
        if(third)
            mDifficultSensationButton.setImageResource(R.drawable.ic_smile_green);
        else
            mDifficultSensationButton.setImageResource(R.drawable.ic_smile);
    }

    /**
     * Resetta i filtri e porta la lista come era inizialmente, aggiornando anche la recyclerView
     */
    private void resetFilter() {
        visibleWorkoutSavedList = Utilities.arrayCopy(workoutSavedList);
        recyclerViewSettings();
    }

    /**
     * L'allenamento supera i filtri se contiene il titolo filtrato
     */
    private void applyTitleFilter() {
        ArrayList<WorkoutSaved> newFilteredList = new ArrayList<>();
        for (WorkoutSaved workoutSaved: visibleWorkoutSavedList) {
            if ( titleFilter==null ||
                    titleFilter.equals("") ||
                    workoutSaved.getWorkout().getTitle().contains(titleFilter))
                newFilteredList.add(workoutSaved);
        }
        visibleWorkoutSavedList = new ArrayList<>();
        Collections.copy(visibleWorkoutSavedList, newFilteredList);
    }

    /**
     * L'allenamento super i filtri solo se non sono stati inseriti filtri per la data o se è compreso tra inizio e fine inseriti dall'utente
     */
    private void applyDateFilter() {
        ArrayList<WorkoutSaved> newFilteredList = new ArrayList<>();
        boolean filterPass = true;
        for (WorkoutSaved workoutSaved: visibleWorkoutSavedList) {
            /*
            Se non c'è data di partenza o se la data è precedente alla data di partenza
            E se non c'è data di arrivo o se la data è antecedente alla data di arrivo
            L'allenamento supera il filtro
             */
            filterPass = ( fromDateFilter == null || workoutSaved.getDate().compareTo(fromDateFilter) >= 0 ) &&
                    (toDateFilter == null || workoutSaved.getDate().compareTo(toDateFilter) <= 0);
            if(filterPass)
                newFilteredList.add(workoutSaved);
        }
        visibleWorkoutSavedList = new ArrayList<>();
        Collections.copy(visibleWorkoutSavedList, newFilteredList);
    }

    /**
     * L'allenamento passa i filtri solo se ha sensazioni uguali a quelle inserite dall'utente
     */
    private void applySensationFilter() {
        ArrayList<WorkoutSaved> newFilteredList = new ArrayList<>();
        for (WorkoutSaved workoutSaved: visibleWorkoutSavedList) {
            if (sensationFilter==null || workoutSaved.getSensation().equals(sensationFilter))
                newFilteredList.add(workoutSaved);
        }
        visibleWorkoutSavedList = new ArrayList<>();
        Collections.copy(visibleWorkoutSavedList, newFilteredList);
    }

    /**
     * L'allenamento passa i filtri solo se il livello è uguale a quello inserito dall'utente
     */
    private void applyLevelFilter() {
        ArrayList<WorkoutSaved> newFilteredList = new ArrayList<>();
        for (WorkoutSaved workoutSaved: visibleWorkoutSavedList) {
            if (levelFilter==null || workoutSaved.getWorkout().getLevel().equals(levelFilter))
                newFilteredList.add(workoutSaved);
        }
        visibleWorkoutSavedList = new ArrayList<>();
        Collections.copy(visibleWorkoutSavedList, newFilteredList);
    }

    private void loadSavedWorkout(WorkoutSaved workoutSaved) {
        Intent intent;
        Workout workout = workoutSaved.getWorkout();
        Exercise exercise = workout.getExerciseList().get(0);
        switch (workoutSaved.getType()){
            case CHRONOMETER:
                intent= ChronometerActivity.getChronometerActivity(getActivity());
                break;
            case TABATA:

                Exercise exercise2 = workout.getExerciseList().get(1);
                intent = TabataActivity.getInstance(getActivity(),
                        exercise.getReps(),
                        exercise.getRec(),
                        exercise.getNumberSets(),
                        exercise.getRepetition(),
                        exercise2.getRec());
                break;
            case AMRAP:
                intent = AmrapActivity.getInstance(getActivity(),
                        exercise.getReps(),
                        exercise.getNumberSets(),
                        exercise.getRec());

                break;
            case TIMER:
                intent = TimerActivity.getIntentInstance(getActivity(),
                        exercise.getReps());
                break;
            case EMOM:
                intent = EmomActivity.getInstance(getActivity(),
                        exercise.getReps(),
                        exercise.getTotalSets());
                break;
            case CONCENTRIC_PHASE:
                intent = NegativePhaseActivity.getInstance(getActivity(),
                        exercise.getNumberSets(),
                        exercise.getReps(),
                        exercise.getRepetition(),
                        exercise.getTotalSets(),
                        exercise.getRec());
                break;
            default:
                /*
                case : WORKOUT
                case : BODYBUILDING,
                case : FUNCTIONALTRAINING,
                case : FREEBODY,
                case : STRETCHING
                 */
                intent = CircuitCreatorActivity.getInstance(getActivity(),
                        workout);
                break;
        }

        startActivity(intent);

    }
}