package com.italianswapp.yourtraining.Timer.Circuit;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.italianswapp.yourtraining.HelpActivity;
import com.italianswapp.yourtraining.R;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.ExerciseSettings;

import java.util.ArrayList;
import java.util.Objects;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class CircuitCreatorActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private FloatingActionButton  mAddExerciseButton, mCreateCircuitButton;
    private RecyclerView mExerciseRecyclerView;

    private LinearLayoutManager linearLayoutManager;
    private ExerciseCardRecyclerViewAdapter exerciseCardRecyclerViewAdapter;

    private ArrayList<ExerciseSettings> exerciseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circuit_creator);

        mToolbar = findViewById(R.id.toolbarCircuitCreator);
        mCreateCircuitButton = findViewById(R.id.createButtonCircuitCreator);
        mAddExerciseButton = findViewById(R.id.addExerciseCircuitCreator);
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

        exerciseList.add(new ExerciseSettings(getResources().getString(R.string.exercise), 1, 10000, 1, true, true)); //1 ripetizione, 00:10 di recupero
        mExerciseRecyclerView.setAdapter(exerciseCardRecyclerViewAdapter);


        mAddExerciseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exerciseList.add(new ExerciseSettings(getResources().getString(R.string.exercise), 1, 10000, 1, true, true)); //1 ripetizione, 00:10 di recupero
                        mExerciseRecyclerView.setAdapter(exerciseCardRecyclerViewAdapter);
                    }
                });

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getResources().getString(R.string.go_out));

        builder.setMessage(getResources().getString(R.string.not_able_to_recovery));

        builder.setPositiveButton(getResources().getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Intent intent=new Intent(getApplicationContext(), MenuActivity.class);
                //startActivity(intent);
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