package com.italianswapp.yourtraining.Timer.Circuit;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import com.google.android.material.snackbar.Snackbar;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;
import com.italianswapp.yourtraining.Builders.ExerciseSettingsDialogBuilder;
import com.italianswapp.yourtraining.HelpActivity;
import com.italianswapp.yourtraining.Builders.Dialog1PickerBuilder;
import com.italianswapp.yourtraining.Builders.Dialog2PickerBuilder;
import com.italianswapp.yourtraining.Builders.Dialog3PickerBuilder;
import com.italianswapp.yourtraining.R;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.ExerciseSettings;
import com.italianswapp.yourtraining.Utilities;

import java.util.ArrayList;
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

    private final String[] TimeInStringForPicker = Utilities.TIME_IN_STRING;
    private final String[] TimeInStringForPickerWithZero = Utilities.TIME_IN_STRING_WITH_0;

    /*

     */
    String workString, restString, exerciseName;
    int workInteger;
    boolean isReps;
    int repetition;

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
        exerciseCardRecyclerViewAdapter.setCircuitCreatorActivity(this);
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

    /**
     * Viene richiaamto dall'adapter della recycler view
     * Lancia il dialog per impostare il tabata
     * @param position Posizione del tabata nella lista degli esercizi
     */
    public void showTabataDialog(final int position) {
        Resources res = getResources();
        workString=TimeInStringForPicker[0];
        restString=TimeInStringForPickerWithZero[0];
        repetition = 1;

        Dialog3PickerBuilder.newBuilder(this, this)
                .setText(0, res.getString(R.string.tabata))
                .setText(1, res.getString(R.string.sets))
                .setText(2, res.getString(R.string.work))
                .setText(3, res.getString(R.string.rest))
                .setPicker(1, 1, 100, new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        repetition = newVal;
                    }
                })
                .setPicker(2, 0, TimeInStringForPicker.length - 1, TimeInStringForPicker, new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        workString = TimeInStringForPicker[newVal];
                    }
                })
                .setPicker(3, 0, TimeInStringForPickerWithZero.length - 1, TimeInStringForPickerWithZero, new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        restString=TimeInStringForPickerWithZero[newVal];
                    }
                })
                .setPositiveButton(res.getString(R.string.Continue),  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExerciseSettings exerciseSettings = exerciseList.get(position);
                        exerciseSettings.setRepetition(repetition);
                        exerciseSettings.setReps((int)Utilities.getMillsFromMinuteString(workString));
                        exerciseSettings.setRec(Utilities.getMillsFromMinuteString(restString));
                        mExerciseRecyclerView.setAdapter(exerciseCardRecyclerViewAdapter);

                    }
                })
                .setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setDialogColor(R.color.tabataColor)
                .show();
    }

    /**
     *
     * @param position
     */
    public void showExerciseDialog(final int position) {
        Resources res = getResources();
        workString=TimeInStringForPicker[0];
        workInteger = 1;
        isReps=false;
        restString=TimeInStringForPickerWithZero[0];
        repetition=1;
        exerciseName = res.getString(R.string.exercise);

        ExerciseSettingsDialogBuilder.newBuilder(this, this)
                .setPicker(1, 1, 100, new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        repetition = newVal;
                    }
                })
                .setPicker(2, 0, TimeInStringForPicker.length - 1, TimeInStringForPicker, new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        workString = TimeInStringForPicker[newVal];
                    }
                })
                .setPicker(3, 0, TimeInStringForPickerWithZero.length - 1, TimeInStringForPickerWithZero, new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        restString = TimeInStringForPickerWithZero[newVal];
                    }
                })
                .setRadioButtonClick(1, new View.OnClickListener() {
                    /*
                    reps
                     */
                    @Override
                    public void onClick(View v) {
                        NumberPicker secondPicker = ExerciseSettingsDialogBuilder.getSecondPicker();
                        String[] arr = new String[100]; for (int i=0; i<100; i++) arr[i]=String.valueOf(i+1);
                        secondPicker.setDisplayedValues(arr);
                        secondPicker.setValue(0);
                        secondPicker.setMinValue(0);
                        secondPicker.setMaxValue(arr.length-1);
                        workInteger=Integer.parseInt(arr[0]);
                        isReps=true;
                        secondPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                            @Override
                            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                workInteger = newVal+1;
                            }
                        });
                    }
                })
                .setRadioButtonClick(2, new View.OnClickListener() {
                    /*
                    secs
                     */
                    @Override
                    public void onClick(View v) {
                        NumberPicker secondPicker = ExerciseSettingsDialogBuilder.getSecondPicker();
                        secondPicker.setMinValue(0);
                        secondPicker.setMaxValue(TimeInStringForPicker.length - 1);
                        secondPicker.setDisplayedValues(TimeInStringForPicker);
                        secondPicker.setValue(0);
                        workString = TimeInStringForPicker[0];
                        isReps=false;
                        secondPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                            @Override
                            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                workString = TimeInStringForPicker[newVal];
                            }
                        });
                    }
                })
                .setTitleOnTextChange(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        exerciseName = String.valueOf(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                })
                .setPositiveButton(res.getString(R.string.Continue), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExerciseSettings exerciseSettings = exerciseList.get(position);
                        //Sets
                        exerciseSettings.setRepetition(repetition);
                        //Reps
                        if(isReps)
                            exerciseSettings.setReps(workInteger);
                        else
                            exerciseSettings.setReps((int)Utilities.getMillsFromMinuteString(workString));
                        exerciseSettings.setReps(isReps);
                        //Recupero
                        if((int)Utilities.getMillsFromMinuteString(restString)==0)
                            exerciseSettings.setHasRecs(false);
                        else {
                            exerciseSettings.setRec((int)Utilities.getMillsFromMinuteString(restString));
                            exerciseSettings.setHasRecs(true);
                        }
                        //Nome
                        if(exerciseName.length()==0)
                            exerciseName=getResources().getString(R.string.exercise);
                        exerciseSettings.setName(exerciseName);

                        mExerciseRecyclerView.setAdapter(exerciseCardRecyclerViewAdapter);
                    }
                })
                .setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();


    }

    /**
     *
     * @param position
     */
    public void showRestDialog(final int position) {
        Resources res = getResources();
        restString = TimeInStringForPickerWithZero[0];

        Dialog1PickerBuilder.newBuilder(this, this)
                .setText(0, res.getString(R.string.rest))
                .setPicker(0, TimeInStringForPickerWithZero.length - 1, TimeInStringForPickerWithZero, new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        restString = TimeInStringForPickerWithZero[newVal];
                    }
                })
                .setPositiveButton(res.getString(R.string.Continue), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExerciseSettings exerciseSettings = exerciseList.get(position);
                        exerciseSettings.setRec((int)Utilities.getMillsFromMinuteString(restString));
                        mExerciseRecyclerView.setAdapter(exerciseCardRecyclerViewAdapter);
                    }
                })
                .setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     *
     * @param position
     */
    public void showEmomDialog(final int position) {
        Resources res = getResources();
        workString=TimeInStringForPicker[0];
        repetition = 1;

        Dialog2PickerBuilder.newBuilder(this, this)
                .setText(0, res.getString(R.string.emom))
                .setText(1, res.getString(R.string.every))
                .setText(2, res.getString(R.string.For))
                .setPicker(1, 0, TimeInStringForPicker.length - 1, TimeInStringForPicker, new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        workString = TimeInStringForPicker[newVal];
                    }
                })
                .setPicker(2, 1, 100, new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        repetition = newVal;
                    }
                })
                .setPositiveButton(res.getString(R.string.Continue), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExerciseSettings exerciseSettings = exerciseList.get(position);
                        exerciseSettings.setRepetition(repetition);
                        exerciseSettings.setReps((int)Utilities.getMillsFromMinuteString(workString));
                        mExerciseRecyclerView.setAdapter(exerciseCardRecyclerViewAdapter);
                    }
                })
                .setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setDialogColor(R.color.emomColor)
                .show();
    }

    /**
     *
     * @param position
     */
    public void showSupersetDialog(int position) {
        //todo implementare questo
    }

    /**
     *
     * @param menu
     * @return
     */
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

    /**
     *
     */
    private void goToHelpActivity() {
        Intent intent=new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    /**
     *
     */
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

    /**
     *
     * @return
     */
    public Activity getActivity() {
        return this;
    }
}
