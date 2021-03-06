package com.italianswapp.yourtraining.Menu;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.italianswapp.yourtraining.Chronometer.ChronometerActivity;
import com.italianswapp.yourtraining.OfflineDatabase.WorkoutDatabase;
import com.italianswapp.yourtraining.OfflineDatabase.WorkoutSaved;
import com.italianswapp.yourtraining.R;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitCreatorActivity;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.Exercise;
import com.italianswapp.yourtraining.Timer.CountDownTimers.AmrapActivity;
import com.italianswapp.yourtraining.Timer.CountDownTimers.EmomActivity;
import com.italianswapp.yourtraining.Timer.CountDownTimers.NegativePhaseActivity;
import com.italianswapp.yourtraining.Timer.CountDownTimers.TabataActivity;
import com.italianswapp.yourtraining.Timer.TimerActivity;
import com.italianswapp.yourtraining.Utilities.RecyclerItemClickListener;
import com.italianswapp.yourtraining.Utilities.Utilities;
import com.italianswapp.yourtraining.WorkoutProposed.Workout.Workout;
import com.italianswapp.yourtraining.WorkoutSaved.SwipeToDeleteCallback;
import com.italianswapp.yourtraining.WorkoutSaved.WorkoutSavedRecyclerViewAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class WorkoutSavedFragment extends Fragment {

    private View view;

    private CardView mFilterCardButton;
    private RecyclerView mRecyclerView;
    private WorkoutSavedRecyclerViewAdapter mAdapter;

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

    /**
     * Indica se sto effettuando filtri sulla lista
     */
    boolean isFilter;

    /*
    Oggetti XML per i filtri
     */

    private ImageButton mSensationButton, mLevelButton;
    AlertDialog alertDialog;

    /**
     * Database al quale si collega il fragment
     */
    private WorkoutDatabase db;



    public WorkoutSavedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_workouts_saved, container, false);

        layoutSettings();

        db = WorkoutDatabase.getDatabase(getActivity());
        workoutSavedList = db.workoutDao().getAll();
        visibleWorkoutSavedList = Utilities.arrayCopy(workoutSavedList);

        recyclerViewSettings();

        mFilterCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterSettings();
            }
        });

        return view;
    }

    private void layoutSettings() {
        mFilterCardButton = view.findViewById(R.id.filterCardWorkoutSaved);
        mRecyclerView = view.findViewById(R.id.recyclerViewWorkoutSaved);
    }

    private void recyclerViewSettings() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new WorkoutSavedRecyclerViewAdapter(visibleWorkoutSavedList, getActivity());
        mRecyclerView.setAdapter(mAdapter);
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

        /*
        Se la recyclerView è vuota mostro un immagine
         */
        if(mAdapter.getItemCount()==0) {
            if(!isFilter) {
                /*
                Carico l'immagine di sfondo nenal recyclerView
                 */
                mRecyclerView.setBackground(getResources().getDrawable(R.drawable.empty_recycler_view));
                /*
                Ridimensiono la recyclerView
                 */
                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 208, getResources().getDisplayMetrics());
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 181, getResources().getDisplayMetrics());

                mRecyclerView.getLayoutParams().width = width;
                mRecyclerView.getLayoutParams().height = height;
                mRecyclerView.requestLayout();

                /*
                Rendo visibile la text view che consiglia di salvare nuovi allenamenti
                 */
                TextView emptyTextView = view.findViewById(R.id.emptyTextView);
                emptyTextView.setVisibility(View.VISIBLE);

                mFilterCardButton.setVisibility(View.INVISIBLE);
            }

        }

        /*
        Allo swipe mostro il bottone per eliminare l'allenamento salvato
         */
        enableSwipeToDeleteAndUndo();

    }

    /**
     * Gestisce lo swipe per eliminare un allenamento salvato
     */
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final WorkoutSaved item = mAdapter.getData().get(position);

                mAdapter.removeItem(position);
                db.workoutDao().delete(item);

                Snackbar snackbar = Snackbar
                        .make(view,getResources().getString(R.string.item_removed), Snackbar.LENGTH_SHORT);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mAdapter.restoreItem(item, position);
                        mRecyclerView.scrollToPosition(position);
                        db.workoutDao().insertAll(item);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(mRecyclerView);

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
        if(titleFilter!=null)
            mTitle.setText(titleFilter);
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

        /*
        Se i filtri per le date sono ancora nulli li inizializzo rispettivamente al:
        from: 01/01/2020
        to: data odierna
         */
        if(fromDateFilter==null)
            fromDateFilter = new Date(
                    currentYear>=2020 ? 2020-1900 : currentYear-1900,
                    0,
                    1); //01/01/2020 oppure un anno precedente se l'utente usa un anno precedente
        if(toDateFilter==null) {
            toDateFilter = new Date(
                    currentYear - 1900,
                    currentMonth,
                    currentDay); //data di oggi

            toDateFilter.setHours(23);
            toDateFilter.setMinutes(59);
            toDateFilter.setSeconds(59);
        }

        //Richiamo gli oggetti XML e inizializzo la data
        final TextView mFromDateText = dialogView.findViewById(R.id.fromDateTextViewFilterWorkoutSaved);
        final TextView mToDateText = dialogView.findViewById(R.id.toDateTextViewFilterWorkoutSaved);
        final CardView mFromDateCard = dialogView.findViewById(R.id.fromDateCardFilterWorkoutSaved);
        final CardView mToDateCard = dialogView.findViewById(R.id.toDateCardFilterWorkoutSaved);

        /*
        Imposto il testo del fromDate
         */
        Calendar filterCalendar = Calendar.getInstance();
        filterCalendar.setTime(fromDateFilter);
        setDataText(mFromDateText,
                filterCalendar.get(Calendar.DAY_OF_MONTH),
                filterCalendar.get(Calendar.MONTH),
                filterCalendar.get(Calendar.YEAR));

        /*
        Imposto il testo del toDate
         */
        filterCalendar.setTime(toDateFilter);
        setDataText(mToDateText,
                filterCalendar.get(Calendar.DAY_OF_MONTH),
                filterCalendar.get(Calendar.MONTH),
                filterCalendar.get(Calendar.YEAR));

        /*
        onClick del fromDate
         */
        mFromDateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fromDateFilter = new Date(year-1900, month, dayOfMonth);
                        setDataText(mFromDateText, dayOfMonth, month, year);

                        if (toDateFilter!=null && fromDateFilter.compareTo(toDateFilter) > 0 ) {
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

        /*
        onClick del toDate
         */
        mToDateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        toDateFilter = new Date(year-1900, month, dayOfMonth);
                        setDataText(mToDateText, dayOfMonth, month, year);

                        if (fromDateFilter==null || toDateFilter.compareTo(fromDateFilter) < 0 ) {
                            fromDateFilter = new Date(year-1900, month, dayOfMonth);

                            setDataText(mFromDateText, dayOfMonth, month, year);
                        }

                        /*
                        toDateFilter si riferisce sempre all'ultima ora del giorno (23:59:59), per fare in modo che tutti gli allenamenti
                        di quel giorno vengano inclusi
                         */
                        toDateFilter.setHours(23);
                        toDateFilter.setMinutes(59);
                        toDateFilter.setSeconds(59);
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
        mSensationButton = dialogView.findViewById(R.id.sensationImageButtonFilterWorkoutSaved);
        /*
        Carico l'immagine della sensazione in base alla precedente selezione nei filtri
         */
        if(sensationFilter!=null) {
            switch (sensationFilter) {
                case EASY:
                    mSensationButton.setImageResource(R.drawable.easy);
                    break;
                case NORMAL:
                    mSensationButton.setImageResource(R.drawable.normal);
                    break;
                case DIFFICULT:
                    mSensationButton.setImageResource(R.drawable.difficult);
                    break;

            }
        }
        else
            mSensationButton.setImageResource(R.drawable.ic_circle_empty);
        /*
        Imposto il comportamento alla pressione dell'emoticon
         */
        mSensationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sensationFilter==null) {
                    mSensationButton.setImageResource(R.drawable.easy);
                    sensationFilter = WorkoutSaved.WorkoutSensation.EASY;
                }
                else {
                    switch (sensationFilter) {
                        case EASY:
                            mSensationButton.setImageResource(R.drawable.normal);
                            sensationFilter = WorkoutSaved.WorkoutSensation.NORMAL;
                            break;
                        case NORMAL:
                            mSensationButton.setImageResource(R.drawable.difficult);
                            sensationFilter = WorkoutSaved.WorkoutSensation.DIFFICULT;
                            break;
                        case DIFFICULT:
                            mSensationButton.setImageResource(R.drawable.ic_circle_empty);
                            sensationFilter = null;
                            break;
                    }
                }
            }
        });

        /*
        Level
         */
        mLevelButton = dialogView.findViewById(R.id.levelImageButtonFilterWorkoutSaved);
        /*
        Carico l'immagine del livello in base alla precedente selezione nei filtri
         */
        if(levelFilter != null) {
            switch (levelFilter) {
                case BEGINNER:
                    mLevelButton.setImageResource(R.drawable.beginner_switch);
                    break;
                case INTERMEDIATE:
                    mLevelButton.setImageResource(R.drawable.intermediate_switch);
                    break;
                case ADVANCED:
                    mLevelButton.setImageResource(R.drawable.difficult_switch);
                    break;
            }
        }
        else
            mLevelButton.setImageResource(R.drawable.empty_level);
        /*
        Imposto il comportamento alla pressione del livello
         */
        mLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (levelFilter == null) {
                    mLevelButton.setImageResource(R.drawable.beginner_switch);
                    levelFilter = Workout.WorkoutLevel.BEGINNER;
                }
                else {
                    switch (levelFilter) {
                        case BEGINNER:
                            mLevelButton.setImageResource(R.drawable.intermediate_switch);
                            levelFilter = Workout.WorkoutLevel.INTERMEDIATE;
                            break;
                        case INTERMEDIATE:
                            mLevelButton.setImageResource(R.drawable.difficult_switch);
                            levelFilter = Workout.WorkoutLevel.ADVANCED;
                            break;
                        case ADVANCED:
                            mLevelButton.setImageResource(R.drawable.empty_level);
                            levelFilter = null;
                            break;
                    }
                }
            }
        });

        /*
        Save
         */
        Button mSaveButton = dialogView.findViewById(R.id.saveButtonFilterWorkoutSaved);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Per prima cosa resetto i filtri altrimenti vado solo a sommare filtri su filtri
                 */
                resetFilter();

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
                mLevelButton.setImageResource(R.drawable.empty_level);
                mSensationButton.setImageResource(R.drawable.ic_circle_empty);

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
                    workoutSaved.getWorkout().getTitle().toLowerCase().contains(titleFilter.toLowerCase()))
                newFilteredList.add(workoutSaved);
        }
        updateVisibleWorkoutSavedList(newFilteredList);
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
        updateVisibleWorkoutSavedList(newFilteredList);
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
        updateVisibleWorkoutSavedList(newFilteredList);
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
        updateVisibleWorkoutSavedList(newFilteredList);
    }

    /**
     * Aggiorna la lista degli allenamenti disponibili data la nuova lista di filtri in inout
     * @param newFilteredList
     */
    private void updateVisibleWorkoutSavedList(ArrayList<WorkoutSaved> newFilteredList) {
        /*
        Svuoto l'array visibleWorkoutSavedList e successivamente (nel ciclo) aggiungo dei null per
        far arrivare l'array visibleWorkoutSavedList alla stessa dimensione di newFilteredList, altrimenti darebbe
        eccezione
         */
        visibleWorkoutSavedList = new ArrayList<>();
        for(int ind=0; ind<newFilteredList.size(); ind++)
            visibleWorkoutSavedList.add(null);
        Collections.copy(visibleWorkoutSavedList, newFilteredList);
        isFilter=true;
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