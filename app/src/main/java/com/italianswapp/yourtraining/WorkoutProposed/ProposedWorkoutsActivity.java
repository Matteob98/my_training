package com.italianswapp.yourtraining.WorkoutProposed;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.italianswapp.yourtraining.ExerciseTypeNotCorrectException;
import com.italianswapp.yourtraining.HelpActivity;
import com.italianswapp.yourtraining.R;
import com.italianswapp.yourtraining.WorkoutProposed.Filter.FilterRecyclerViewAdapter;
import com.italianswapp.yourtraining.WorkoutProposed.Filter.LevelFilterSelectRecyclerViewAdapter;
import com.italianswapp.yourtraining.WorkoutProposed.Filter.MuscleFilterSelectRecyclerViewAdapter;
import com.italianswapp.yourtraining.WorkoutProposed.Workout.Workout;
import com.italianswapp.yourtraining.WorkoutProposed.WorkoutProposedRecyclerView.WorkoutSummaryRecyclerViewAdapter;
import com.italianswapp.yourtraining.WorkoutProposed.WorkoutProposed.BodybuildingWorkoutProposed;
import com.italianswapp.yourtraining.WorkoutProposed.WorkoutProposed.FreeBodyWorkoutProposed;
import com.italianswapp.yourtraining.WorkoutProposed.WorkoutProposed.FunctionalTrainingWorkoutProposed;
import com.italianswapp.yourtraining.WorkoutProposed.WorkoutProposed.ProposedWorkoutList;
import com.italianswapp.yourtraining.WorkoutProposed.WorkoutProposed.StretchingWorkoutProposed;

import java.util.ArrayList;
import java.util.Arrays;

public class ProposedWorkoutsActivity extends AppCompatActivity{

    private static final String CATEGORY = "CATEGORY";


    private ImageButton mBackButton, mHelpButton;
    private TextView mTitleTextView;

    /**
     * Classe che contiene gli esercizi proposti
     */
    private ProposedWorkoutList proposed;

    private RecyclerView mWorkoutProposedRecyclerView;
    /**
     * I workout che superano i filtri
     */
    private ArrayList<Workout> workoutVisibleList;
    /**
     * Insieme dei filtri di tipo livello (difficoltà)
     */
    private ArrayList<Workout.WorkoutLevel> levelFilter;
    /**
     * Insieme dei filtri di tipo gruppo muscolare
     */
    private ArrayList<Workout.MuscleGroup> muscleFilter;
    private CardView mLevelCard, mMuscleCard;
    private RecyclerView mFilterRecyclerView;

    /**
     * Adapter della recycler view dei filtri
     */
    private FilterRecyclerViewAdapter mFilterAdapter;

    /**
     * Lista che contiene tutti i gruppi muscolari contenuti nella lista di esercizi
     * I gruppi muscolari appaiono solo una volta (come in un insieme), il controllo viene fatto lato codice
     * all'inserimento
     *
     * NOTA: Contiene i gruppi muscolari nello stesso ordine in cui sono posizionate le chips
     */
    private ArrayList<Workout.MuscleGroup> muscleGroupsList;

    /**
     * Lista che contiene tutti i livelli degli allenamenti, usata per fare i filtri
     */
    private ArrayList<Workout.WorkoutLevel> levelGroupsList =
            new ArrayList<Workout.WorkoutLevel> (Arrays.asList( Workout.WorkoutLevel.BEGINNER, Workout.WorkoutLevel.INTERMEDIATE, Workout.WorkoutLevel.ADVANCED ));


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposed_workouts);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        /*
        Richiamo gli oggetti xml
         */
        initializeActivity();

        /*
        Gestisco la toolbar
         */
        /*
        Gestisco il back button
         */
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*
        Gestisco l'help button
         */
        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), HelpActivity.class);
                startActivity(intent);
            }
        });
        /*
        Imposto il titolo della toolbar con la prima lettera in maiuscolo e il resto in minuscolo
         */
        String title = "";
        switch (Workout.WorkoutCategory.valueOf(getIntent().getStringExtra(CATEGORY))) {
            case BODYBUILDING:
                title = getResources().getString(R.string.bodybuilding);
                break;
            case FREEBODY:
                title = getResources().getString(R.string.free_body);
                break;
            case FUNCTIONALTRAINING:
                title = getResources().getString(R.string.functional_training);
                break;
            case STRETCHING:
                title = getResources().getString(R.string.stretching);
                break;
            default:
                try {
                    throw new ExerciseTypeNotCorrectException("Categoria di allenamento non valida");
                } catch (ExerciseTypeNotCorrectException e) {
                    e.printStackTrace();
                }
        }
        mTitleTextView.setText(
                title.toUpperCase().charAt(0) +
                title.toLowerCase().substring(1, title.length()));

        /*
        RecyclerView per i filtri
         */
        LinearLayoutManager linearLayoutManagerFilter =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mFilterRecyclerView.setLayoutManager(linearLayoutManagerFilter);
        /*
        Adapter per la recyclerView
         */
        mFilterAdapter = new FilterRecyclerViewAdapter(levelFilter, muscleFilter,this, getApplicationContext());
        mFilterRecyclerView.setAdapter(mFilterAdapter);
        /*
        Gestisco il tocco sulle card dei filtri che provoca la rimozione della card e del filtro relativo
         */

        /*
        Carica gli esercizi proposti in base alla categoria passata all'intent quando
        è stata richiamata l'activity (presumibilmente da workoutProposedFragment)
         */
        loadWorkoutProposedList(Workout.WorkoutCategory.valueOf(getIntent().getStringExtra(CATEGORY)));

        /*
        Imposta la recycler view che mostra gli allenamenti proposti
         */
        LinearLayoutManager linearLayoutManagerProposedWorkout = new LinearLayoutManager(this);
        mWorkoutProposedRecyclerView.setLayoutManager(linearLayoutManagerProposedWorkout);

        /*
        Gestisco la card e il dialog per i livelli
         */

        mLevelCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLevelDialog();
            }
        });

        /*
        Gestisco la card e il dialog per i muscoli
         */
        mMuscleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callMuscleDialog();
            }
        });

        loadMuscleGroupsList();

        updateWorkoutList();

    }

    /**
     * Richiama tutti gli oggetti xml dell'activity
     * Inizializza le variabili dell'activity
     */
    private void initializeActivity() {
        mBackButton = findViewById(R.id.backButtonProposedWorkouts);
        mHelpButton = findViewById(R.id.helpButtonProposedWorkout);
        mTitleTextView = findViewById(R.id.titleProposedWorkouts);
        mFilterRecyclerView = findViewById(R.id.filterRecyclerviewProposedWorkouts);
        mWorkoutProposedRecyclerView = findViewById(R.id.workoutProposedListRecyclerView);
        mLevelCard = findViewById(R.id.levelCardProposedWorkouts);
        mMuscleCard = findViewById(R.id.muscleCardProposedWorkouts);

        levelFilter = new ArrayList<>();
        muscleFilter = new ArrayList<>();
    }


    private void loadWorkoutProposedList(Workout.WorkoutCategory category) {
        switch (category) {
            case BODYBUILDING:
                proposed = new BodybuildingWorkoutProposed();
                break;
            case FREEBODY:
                proposed = new FreeBodyWorkoutProposed();
                break;
            case FUNCTIONALTRAINING:
                proposed = new FunctionalTrainingWorkoutProposed();
                break;
            case STRETCHING:
                proposed = new StretchingWorkoutProposed();
                break;
            default:
                throw new IllegalArgumentException("Errore in loadWorkoutProposedList");
        }
    }

    private void callLevelDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.filter_dialog_workouts_proposed, null);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();

        RecyclerView mRecyclerView = dialogView.findViewById(R.id.recyclerViewFilterDialogWorkoutsProposed);
        CardView mApplyCard = dialogView.findViewById(R.id.applyCardFilterDialogWorkoutsProposed);

        LinearLayoutManager linearLayoutManagerProposedWorkout = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManagerProposedWorkout);
        mRecyclerView.setAdapter(new LevelFilterSelectRecyclerViewAdapter(levelGroupsList, levelFilter, getApplicationContext()));

        mApplyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFilterRecyclerView();
                updateWorkoutList();
                alertDialog.dismiss();
            }
        });

        /*
        Creo il dialog
         */

        alertDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }

    private void callMuscleDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.filter_dialog_workouts_proposed, null);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();

        RecyclerView mRecyclerView = dialogView.findViewById(R.id.recyclerViewFilterDialogWorkoutsProposed);
        CardView mApplyCard = dialogView.findViewById(R.id.applyCardFilterDialogWorkoutsProposed);

        LinearLayoutManager linearLayoutManagerProposedWorkout = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManagerProposedWorkout);
        mRecyclerView.setAdapter(new MuscleFilterSelectRecyclerViewAdapter( muscleGroupsList, muscleFilter, getApplicationContext()));

        mApplyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFilterRecyclerView();
                updateWorkoutList();
                alertDialog.dismiss();
            }
        });

         /*
        Creo il dialog
         */

        alertDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    /**
     * Carica la lista contenente tutti i gruppi muscolari compresi negli allenamenti proposti
     */
    private void loadMuscleGroupsList() {

        muscleGroupsList = new ArrayList<>();
        /*
        Per ogni allenamento
         */
        for( Workout w: proposed.getWorkoutList())
            /*
            Per ogni gruppo muscolare di quell'allenamento
             */
            for (final Workout.MuscleGroup muscleGroup : w.getMuscleGroupList())
                /*
                Se quel gruppo muscolare non è già contenuto nella lista dei gruppi muscolari
                 */
                if (! muscleGroupsList.contains(muscleGroup)) {
                    /*
                    Lo aggiungo alla lista
                     */
                    muscleGroupsList.add(muscleGroup);
                }
    }

    /**
     * Restituisce un istanza della classe passando la categoria di allenament proposto che l'utente desidera
     * @param context il contesto in cui ci si trova
     * @param workoutCategory la categoria di allenamento
     * @return l'Intent della classe
     */
    public static Intent getInstance(Context context, Workout.WorkoutCategory workoutCategory)
    {
        Intent intent = new Intent(context, ProposedWorkoutsActivity.class);

        intent.putExtra(CATEGORY, workoutCategory.toString());
        return intent;
    }

    /**
     * Metodo che aggiorna la lista degli allenamenti in base alle modifiche apportate
     * Deve essere chiamata ogni volta che viene effettuata una modifica
     */
    public void updateWorkoutList() {
        workoutVisibleList = proposed.getWorkoutListWithFilter(levelFilter, muscleFilter);
        mWorkoutProposedRecyclerView.setAdapter(new WorkoutSummaryRecyclerViewAdapter(workoutVisibleList, this));
    }

    /**
     * Aggiorna la recyclerView contenente i filtri
     */
    public void updateFilterRecyclerView() {
        mFilterAdapter = new FilterRecyclerViewAdapter(levelFilter, muscleFilter,this, getApplicationContext());
        mFilterRecyclerView.setAdapter(mFilterAdapter);
    }


}