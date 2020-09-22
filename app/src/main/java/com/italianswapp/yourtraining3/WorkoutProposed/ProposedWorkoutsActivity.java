package com.italianswapp.yourtraining3.WorkoutProposed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.italianswapp.yourtraining3.R;
import com.italianswapp.yourtraining3.Timer.Circuit.CircuitCreatorActivity;
import com.italianswapp.yourtraining3.WorkoutProposed.Workout.Workout;
import com.italianswapp.yourtraining3.WorkoutProposed.WorkoutProposedType.BodybuildingWorkoutProposed;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ProposedWorkoutsActivity extends AppCompatActivity{

    private static final String CATEGORY = "CATEGORY";

    /**
     * Classe che contiene gli esercizi proposti
     * todo adesso per debug si usa solo la bodybuilding, poi dovrà dipendere dalla selezione fatta dall'utente nell'activity precedente
     */
    BodybuildingWorkoutProposed proposed;

    private RecyclerView mWorkoutProposedRecyclerView;
    /**
     * I workout che superano i filtri
     */
    private ArrayList<Workout> workoutVisibleList;
    /**
     * Insieme dei filtri di tipo livello (difficoltà)
     */
    private Set<Workout.WorkoutLevel> levelFilter;
    /**
     * Insieme dei filtri di tipo gruppo muscolare
     */
    private Set<Workout.MuscleGroup> muscleFilter;
    private Chip mBeginnerChip, mIntermediateChip, mAdvancedChip;
    private ChipGroup mMuscleGroupChipGroup;

    /**
     * Lista che contiene tutti i gruppi muscolari contenuti nella lista di esercizi
     * I gruppi muscolari appaiono solo una volta (come in un insieme), il controllo viene fatto lato codice
     * all'inserimento
     *
     * NOTA: Contiene i gruppi muscolari nello stesso ordine in cui sono posizionate li chips
     */
    private ArrayList<Workout.MuscleGroup> muscleGroupsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposed_workouts);
        
        levelFilter = new HashSet<>();
        muscleFilter = new HashSet<>();
        proposed = new BodybuildingWorkoutProposed();

        mWorkoutProposedRecyclerView = findViewById(R.id.workoutProposedListRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mWorkoutProposedRecyclerView.setLayoutManager(linearLayoutManager);

        levelChipsSettings();
        muscleChipsSettings();

        updateWorkoutList();

        /*
        Avvia un allenamento con lo swipe
         */
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Intent intent = CircuitCreatorActivity.getInstance(getApplicationContext(), workoutVisibleList.get(position));
                startActivity(intent);
                updateWorkoutList();
            }
        });
        helper.attachToRecyclerView(mWorkoutProposedRecyclerView);

    }

    /**
     * Crea le chip e ne imposta l'onCheckedChanged
     */
    private void muscleChipsSettings() {

        mMuscleGroupChipGroup = findViewById(R.id.muscleGroupChipGroup);
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
                    /*
                    E poi aggiungo la relativa chip
                     */
                    Chip tempChip = (Chip) this.getLayoutInflater().inflate(R.layout.chip_layout, null, false);
                    tempChip.setText(muscleGroup.toString());
                    tempChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked)
                                muscleFilter.add(muscleGroup);
                            else
                                muscleFilter.remove(muscleGroup);
                            updateWorkoutList();
                        }
                    });
                    mMuscleGroupChipGroup.addView(tempChip);
                }
    }

    /**
     * Richiama le chip dall'xml e ne imposta l'onCheckedChanged
     */
    private void levelChipsSettings() {
        mBeginnerChip = findViewById(R.id.beginnerChipProposedWorkout);
        mIntermediateChip = findViewById(R.id.intermediateChipProposedWorkout);
        mAdvancedChip = findViewById(R.id.advancedChipProposedWorkout);

         /*
        Imposto il listener delle chip

         */

        mBeginnerChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    levelFilter.add(Workout.WorkoutLevel.BEGINNER);
                else
                    levelFilter.remove(Workout.WorkoutLevel.BEGINNER);
                updateWorkoutList();
            }
        });
        mIntermediateChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    levelFilter.add(Workout.WorkoutLevel.INTERMEDIATE);
                else
                    levelFilter.remove(Workout.WorkoutLevel.INTERMEDIATE);
                updateWorkoutList();
            }
        });
        mAdvancedChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    levelFilter.add(Workout.WorkoutLevel.ADVANCED);
                else
                    levelFilter.remove(Workout.WorkoutLevel.ADVANCED);
                updateWorkoutList();
            }
        });
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

}