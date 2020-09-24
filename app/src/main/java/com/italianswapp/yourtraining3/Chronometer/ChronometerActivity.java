package com.italianswapp.yourtraining3.Chronometer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.italianswapp.yourtraining3.FinishActivity;
import com.italianswapp.yourtraining3.Menu.MenuActivity;
import com.italianswapp.yourtraining3.OfflineDatabase.WorkoutSaved;
import com.italianswapp.yourtraining3.Timer.Circuit.CircuitSettings.ExerciseBuilder;
import com.italianswapp.yourtraining3.Utilities.Utilities;
import com.italianswapp.yourtraining3.R;
import com.italianswapp.yourtraining3.WorkoutProposed.Workout.Workout;
import com.italianswapp.yourtraining3.WorkoutProposed.Workout.WorkoutBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ChronometerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button startButton, resetButton, lapButton;
    private TextView textTime, lapTimeText;
    private RecyclerView roundRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RoundRecyclerViewAdapter roundRecyclerViewAdapter;
    private Handler timeHandler = new Handler();

    private long startTime=0L, timeInMilliseconds=0L, timeSwapBuff=0L, updateTime=0L;
    private List<Lap> lapList;

    private boolean isRunning = false;
    private long lastLap=0L;
    private long bestLap=0L;
    /**
     * Indice del miglior lap all'interno della lista lapList
     */
    private int indexOfBestLap=0;

    private long thisLap=0L, currentLapTime=0L;

    private Runnable updateTimerThread = new Runnable()
    {
        @Override
        public void run()
        {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapBuff + timeInMilliseconds;
            currentLapTime = updateTime - thisLap;

            String time;
            if(Utilities.getHoursFromMills(updateTime) >0)
                time = Utilities.getStringTimeFromMills(updateTime);
            else
                time = Utilities.getStringTimeWithMillsFromMills(updateTime);

            String lap;
            if(Utilities.getHoursFromMills(currentLapTime) >0)
                lap = Utilities.getStringTimeFromMills(currentLapTime);
            else
                lap = Utilities.getStringTimeWithMillsFromMills(currentLapTime);

            textTime.setText(time);
            lapTimeText.setText(lap);

            timeHandler.postDelayed(this, 0);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometer);

        /*
        Importa gli oggetti dall'xml
         */
        layoutImports();

        /*
        Setta la toolbar
         */
        toolbarSettings();


        /*
        Imposto il manager e l'adapter per la recycler view
         */
        recyclerViewInitialize();

        /*
        Imposta i comportamenti dei vari button
         */
        onClickStartButton();
        onClickResetButton();
        onClickLapButton();

    }

    /**
     * Imposta il comportamento del lap button
     */
    private void onClickLapButton() {
        lapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                thisLap = updateTime;

                /*
                Salvo il lap se è diverso dall'ultimo (evita che se il cronometro è in pausa continua a salvare
                lo stesso lap)
                E se è diverso da 0
                 */
                if  (! (thisLap==lastLap || updateTime==0))
                {
                    long millsOfThisLap = updateTime - lastLap;

                    lastLap = thisLap;

                    long totalTime =  SystemClock.uptimeMillis() - startTime + timeSwapBuff;

                    Collections.reverse(lapList);
                    lapList.add(new Lap(lapList.size()+1, totalTime, millsOfThisLap));
                    Collections.reverse(lapList);



                    if(!isRunning)
                        lapTimeText.setText(Utilities.getStringTimeFromMills(0));


                    roundRecyclerView.setAdapter(roundRecyclerViewAdapter);
                }

            }
        });
    }

    /**
     * Imposta il comportamento del reset button
     */
    private void onClickResetButton() {
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRunning) {
                    timeHandler.removeCallbacks(updateTimerThread);
                    startTime = 0L;
                    timeInMilliseconds = 0L;
                    timeSwapBuff = 0L;
                    updateTime = 0L;
                    lastLap=0L;
                    thisLap=0;
                    textTime.setText(Utilities.getStringTimeFromMills(0));
                    lapTimeText.setText(Utilities.getStringTimeFromMills(0));
                    lapList.clear();
                    roundRecyclerView.setAdapter(roundRecyclerViewAdapter);
                }
            }
        });
    }

    /**
     * Imposta il comportamento dello start button
     */
    private void onClickStartButton() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRunning) {

                    startTime = SystemClock.uptimeMillis();
                    timeHandler.postDelayed(updateTimerThread, 10);
                    isRunning=true;


                    resetButton.setVisibility(Button.GONE);
                    lapButton.setVisibility(Button.VISIBLE);

                    startButton.setText(getResources().getString(R.string.pause));
                    //startButton.setBackground(getResources().getDrawable(R.drawable.ripple_red));
                    startButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                }
                else {
                    timeSwapBuff += timeInMilliseconds;
                    timeHandler.removeCallbacks(updateTimerThread);
                    isRunning=false;

                    resetButton.setVisibility(Button.VISIBLE);
                    lapButton.setVisibility(Button.GONE);

                    startButton.setText(getResources().getString(R.string.start));
                    //startButton.setBackground(getResources().getDrawable(R.drawable.ripple_blue));
                    startButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                }

            }
        });
    }

    /**
     * Si occupa di importare tutti gli oggetti dall'xml a partire dal findViewById
     */
    private void layoutImports() {
        toolbar = findViewById(R.id.toolbarChronometerActivity);
        startButton = findViewById(R.id.startButtonChronometerActivity);
        resetButton = findViewById(R.id.resetButtonChronometerActivity);
        lapButton = findViewById(R.id.lapButtonChronometerActivity);
        textTime = findViewById(R.id.textChronometerActivity);
        lapTimeText = findViewById(R.id.lapTimeTextChronometerActivity);
        roundRecyclerView = findViewById(R.id.listRoundChronometerActivity);
    }

    /**
     * Si occupa di settare le impostazioni per la toolbar
     */
    private void toolbarSettings() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark)); //Colora la barra delle notifiche
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Schermo sempre acceso
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.chronometer)); //Gestisce l'eccezione
        toolbar.setTitleTextColor(getResources().getColor(R.color.textColor));
    }

    /**
     * Si occupa di inizializzare il manager e l'adapter della recycler view
     */
    private void recyclerViewInitialize() {
        lapList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        roundRecyclerView.setLayoutManager(linearLayoutManager);
        roundRecyclerViewAdapter = new RoundRecyclerViewAdapter(lapList);
        roundRecyclerView.setAdapter(roundRecyclerViewAdapter);

        roundRecyclerViewAdapter.setOnItemClickListener(new RoundRecyclerViewAdapter.OnItemClickListener() {

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
    }

    /**
     * Richiama l'activity del cronometro
     * Se la variabile booleana in input è vera, fa partire l'allenamento forTime
     * @param context Il contesto
     * @return L'intent dell'activity cronometro
     */
    public static Intent getChronometerActivity(Context context) {
        return new Intent(context, ChronometerActivity.class);
    }

    public void removeItem(int position) {
        lapList.remove(position);
        Collections.reverse(lapList);
        /*
        Se sto eliminando il miglior Lap
         */
        if(indexOfBestLap==position) {
            bestLap=0;
            for(Lap l : lapList)
                if(l.getLapTime()<bestLap || bestLap==0) {
                    bestLap=l.getLapTime();
                    indexOfBestLap=l.getNumberLap()-1;
                }
        }
        for(int i=0; i<lapList.size(); i++)
            lapList.get(i).setNumberLap(i+1);
        Collections.reverse(lapList);
        roundRecyclerView.setAdapter(roundRecyclerViewAdapter);

        roundRecyclerViewAdapter.notifyItemRemoved(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem mFinishButton = menu.findItem(R.id.finishTrainingToolbar);
        mFinishButton.setVisible(true);

        mFinishButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                dialogFinishTraining();
                return true;
            }
        });
        return true;
    }

    private void dialogFinishTraining() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getResources().getString(R.string.workout_completed));
        builder.setMessage(getResources().getString(R.string.not_able_to_recovery));

        builder.setPositiveButton(getResources().getString(R.string.finished), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishTraining();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.not_yet), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }

    private void finishTraining() {
        MediaPlayer.create(this, R.raw.timer_sound).start();

        String workoutTime = (Utilities.getHoursFromMills(updateTime) >0 ?
                Utilities.getStringTimeFromMills(updateTime) :
                Utilities.getStringTimeNoHour(updateTime));
        String text = getResources().getString(R.string.workout_completed_in) + " " +
                (Utilities.getHoursFromMills(updateTime) >0 ?
                Utilities.getStringTimeFromMills(updateTime) + " " + getResources().getString(R.string.hours) :
                Utilities.getStringTimeNoHour(updateTime) + " " + getResources().getString(R.string.minutes));


        /*
        Cronometro contiene un allenamento vuoto perché non si può ripetere lo stesso allenamento,
        al limite si può far partire un nuovo cronometro
         */
        Workout workout = WorkoutBuilder
                .newBuilder()
                .addExercise(ExerciseBuilder
                        .newBuilder()
                        .build())
                .build();

        Intent intent= FinishActivity.getInstance(this, text, workout, WorkoutSaved.WorkoutType.CHRONOMETER, workoutTime);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getResources().getString(R.string.go_out));
        builder.setMessage(getResources().getString(R.string.not_able_to_recovery));

        builder.setPositiveButton(getResources().getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }
}
