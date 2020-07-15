package com.italianswapp.yourtraining.Timer.Circuit;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;
import com.italianswapp.yourtraining.HelpActivity;
import com.italianswapp.yourtraining.R;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.ExerciseSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class CircuitCreatorActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Button mCreateCircuitButton;
    private RecyclerView mExerciseRecyclerView;

    private LinearLayoutManager linearLayoutManager;
    private ExerciseCardRecyclerViewAdapter exerciseCardRecyclerViewAdapter;

    private ArrayList<ExerciseSettings> exerciseList;

    /*
    Servono per il menu espandibile per l'aggiunta di esercizi/tabata ecc.
     */
    private MaterialSheetFab materialSheetFab;
    private View sheetView, overlay;
    private Fab fab;
    private LinearLayout mNewExercise, mNewTabata, mNewEmom, mNewSuperset, mNewRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circuit_creator);

        mToolbar = findViewById(R.id.toolbarCircuitCreator);
        mCreateCircuitButton = findViewById(R.id.createButtonCircuitCreator);
        mExerciseRecyclerView = findViewById(R.id.circuitCreatorRecyclerView);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.toolbar_title));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.textColor));

        /*
        Imposto il manager e l'adapter per la recycler view
         */
        exerciseList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mExerciseRecyclerView.setLayoutManager(linearLayoutManager);
        exerciseCardRecyclerViewAdapter = new ExerciseCardRecyclerViewAdapter(exerciseList);
        mExerciseRecyclerView.setAdapter(exerciseCardRecyclerViewAdapter);

        exerciseList.add(new ExerciseSettings(getResources().getString(R.string.exercise), 1, 10000, 1, true, true, ExerciseSettings.CircuitType.EXERCISE)); //1 ripetizione, 00:10 di recupero
        mExerciseRecyclerView.setAdapter(exerciseCardRecyclerViewAdapter);

        fabMenuInitialize();

        mCreateCircuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exerciseList.size()>0) {
                    for (ExerciseSettings e : exerciseList)
                        if(e.getName().equals(NULL) || e.getName().length()==0) {
                            Snackbar.make(v,getResources().getString(R.string.circuit_creator_error), Snackbar.LENGTH_LONG).show();
                            return;
                        }

                    Intent intent = CircuitActivity.getCircuitActivity(getApplicationContext(), exerciseList);
                    startActivity(intent);
                }
                else
                    Snackbar.make(v, getResources().getString(R.string.add_one_exercise), Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Inizializza il comportamento del menu che si apre premendo sul FAB
     */
    private void fabMenuInitialize(){
        fab = (Fab) findViewById(R.id.fab);
        sheetView = findViewById(R.id.fab_sheet);
        overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.backgroundColor);
        int fabColor = getResources().getColor(R.color.colorAccent);

        // Initialize material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay,
                sheetColor, fabColor);

        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Called when the material sheet's "show" animation starts.
            }

            @Override
            public void onSheetShown() {
                // Called when the material sheet's "show" animation ends.
            }

            @Override
            public void onHideSheet() {
                // Called when the material sheet's "hide" animation starts.
            }

            public void onSheetHidden() {
                // Called when the material sheet's "hide" animation ends.
            }
        });

        /*
        Inizializza i comportamenti alla pressione delle opzioni del menu
         */
        mNewExercise = findViewById(R.id.newExerciseCircuitCreator);
        mNewTabata = findViewById(R.id.newTabataCircuitCreator);
        mNewEmom = findViewById(R.id.newEmomCircuitCreator);
        mNewRest = findViewById(R.id.newRestCircuitCreator);
        mNewSuperset = findViewById(R.id.newSupersetCircuitCreator);

        /* -- */
        mNewExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseList.add(new ExerciseSettings(getResources().getString(R.string.exercise),
                        1,
                        10000,
                        1,
                        true,
                        true,
                        ExerciseSettings.CircuitType.EXERCISE)); //1 ripetizione, 00:10 di recupero
                mExerciseRecyclerView.setAdapter(exerciseCardRecyclerViewAdapter);
                materialSheetFab.hideSheet();
            }
        });

        mNewTabata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseList.add(new ExerciseSettings(getResources().getString(R.string.tabata),
                        10000,
                        10000,
                        1,
                        false,
                        true,
                        ExerciseSettings.CircuitType.TABATA));
                mExerciseRecyclerView.setAdapter(exerciseCardRecyclerViewAdapter);
                materialSheetFab.hideSheet();
            }
        });

        mNewEmom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseList.add(new ExerciseSettings(
                        getResources().getString(R.string.emom),
                        10000,
                        0,
                        1,
                        false,
                        false,
                        ExerciseSettings.CircuitType.EMOM));
                mExerciseRecyclerView.setAdapter(exerciseCardRecyclerViewAdapter);
                materialSheetFab.hideSheet();
            }
        });

        mNewRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseList.add(new ExerciseSettings(
                        getResources().getString(R.string.rest),
                        0,
                        10000,
                        1,
                        false,
                        true,
                        ExerciseSettings.CircuitType.REST));
                mExerciseRecyclerView.setAdapter(exerciseCardRecyclerViewAdapter);
                materialSheetFab.hideSheet();
            }
        });

        mNewSuperset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseSettings superset = new ExerciseSettings(
                        getResources().getString(R.string.superset) + " 1",
                        0,
                        10000,
                        1,
                        false,
                        true,
                        ExerciseSettings.CircuitType.SUPERSET);
                superset.setSupersetExercise(0,false,getResources().getString(R.string.superset) + " 1");
                exerciseList.add(superset);
                mExerciseRecyclerView.setAdapter(exerciseCardRecyclerViewAdapter);
                materialSheetFab.hideSheet();
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem mHelpButton = menu.findItem(R.id.helpToolbar);
        mHelpButton.setVisible(true);
        mHelpButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                goToHelpActivity();
                return true;
            }
        });
        return true;
    }

    private void goToHelpActivity() {
        Intent intent=new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(getResources().getString(R.string.go_out));

            builder.setMessage(getResources().getString(R.string.not_able_to_recovery));

            builder.setPositiveButton(getResources().getString(R.string.exit), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            builder.create().show();
        }


    }
}
