package com.italianswapp.yourtraining.Timer.CountDownTimers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.italianswapp.yourtraining.FinishActivity;
import com.italianswapp.yourtraining.Utilities;
import com.italianswapp.yourtraining.R;

import java.util.Objects;

public abstract class CountDownActivity extends AppCompatActivity {

    protected final static String WORK_KEY="work";
    protected final static String REST_KEY="rest";
    protected final static String SETS_KEY="sets";
    protected final static long READY_TIMER=3000L;
    
    protected Resources res;

    protected TextView mTimeTextView, mPrimaryTextView, mSecondaryTextView, mOverlinePrimaryTextView, mOverlineSecondaryTextView, mWorkDescriptionTextView, mTimeFromStartTextView;
    protected Button mStartButton;
    protected ImageButton  mRightFloatingButton, mLeftFloatingButton;
    protected Toolbar mToolbar;
    protected ProgressBar mProgressBar;

    protected CountDownTimer timer;

    /*
    isWork indica se si sta facendo un esercizio, se è false si sta facendo un riposo
     */
    protected boolean isFirstStart, isRunning, isWork;
    protected final static long interval=10L;
    /*
    work -> millisecondi di lavoro
    rest -> millisecondi di riposo
    remainingTime -> millisecondi rimanenti attualmente
    currentdDuration -> millisecondi di durata del timer attuale
    millsFromStart -> millisecondi dall'inizio dell'allenamento
     */
    protected long work,rest, remainingTime, currentDuration;
    protected int setsNumber, currentSet;

    /*
    Attributi per la progress bar
     */
    protected final int maxProgress=1000;
    protected Runnable progressBarRun;
    protected Handler progressBarHandler;

    private final static String TEST_ADS ="ca-app-pub-3940256099942544/1033173712";
    private final static String ADS_CODE = "ca-app-pub-8919261416525349/9438301000";
    /**
     * Banner a schermo intero che viene visualizzato appena viene aperta l'applicazione
     */
    private InterstitialAd mInterstitialAd;

    /**
     * Se vero abilita i suoni (tick, fine circuito, ecc.)
     * Se falso disabilita
     */
    private boolean ifSound;

    protected boolean isTablet;

    /*
    Si occupano dei suoni durante il timer
     */
    protected boolean tick1000=true, tick2000=true, tick3000=true, tickHalf=true;

    private Handler timeHandler = new Handler();
    private long startTime=0L, timeSwapBuff=0L, updateTime;

    private Runnable updateTimerThread = new Runnable()
    {
        @Override
        public void run()
        {
            mTimeFromStartTextView.setText(chronoTick());
            timeHandler.postDelayed(this, 0);
        }
    };

    /**
     * Ritorna il tempo dall'inizio del circuito
     * @return Stringa hh:mm:ss
     */
    private String chronoTick() {
        updateTime = timeSwapBuff + SystemClock.uptimeMillis() - startTime;

        String time;
        if(Utilities.getHoursFromMills(updateTime) >0)
            time = Utilities.getStringTimeFromMills(updateTime);
        else
            time = Utilities.getStringTimeWithMillsFromMills(updateTime);

        return time;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_count_down_timer);
        
        res = getResources();
        isTablet = res.getBoolean(R.bool.isTablet);

        /*
        Importo tutti gli oggetti dal layout xml
         */
        layoutImports();

        /*
        Gestisco colori e opzioni della toolbar
         */
        toolbarSettings();

        /*
        Inizializza tutte le variabili
         */
        variableInitialize();

        /*
        Gestisto la progress bar intorno al timer
         */
        progressBarSettings();

        /*
        Carica l'interstitialAds che viene mostrato prima di andare all'activity finish
         */
        loadAds();

        /*
        Il timer è inizializzato con la scritta ready ed un conto alla rovescia a partire da 3
         */
        readyLayoutSettings();

    }

    /**
     * Si occupa di inizializzare tutte le variabili dell'activity
     */
    private void variableInitialize() {
        ifSound=true;
        isFirstStart=true;
        isRunning=false;
        isWork=false;
        work=0;
        rest=0;
        setsNumber=1;
        currentSet=1;
    }

    /**
     * Si occupa di settare le impostazioni per la toolbar
     */
    private void toolbarSettings() {
        getWindow().setStatusBarColor(res.getColor(R.color.colorPrimaryDark)); //Colora barra notifiche
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Schermo sempre acceso
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(res.getString(R.string.toolbar_title));
        mToolbar.setTitleTextColor(res.getColor(R.color.textColor));
    }

    /**
     * Si occupa di importare tutti gli oggetti dall'xml a partire dal findViewById
     */
    private void layoutImports() {
        mTimeTextView = findViewById(R.id.textEmomActivity);
        mPrimaryTextView = findViewById(R.id.primaryBottomTextEmomActivity);
        mSecondaryTextView = findViewById(R.id.secondaryBottomTextEmomActivity);
        mOverlinePrimaryTextView = findViewById(R.id.overlinePrimaryBottomTextEmomActivity);
        mOverlineSecondaryTextView = findViewById(R.id.overlineSecondaryBottomTextEmomActivity);
        mWorkDescriptionTextView = findViewById(R.id.workDescription);
        mTimeFromStartTextView = findViewById(R.id.timeFromStart);
        mStartButton = findViewById(R.id.startButtonCountDownActivity);
        mRightFloatingButton = findViewById(R.id.rightFloatingButtonCountDownActivity);
        mLeftFloatingButton = findViewById(R.id.leftFloatingButtonCountDownActivity);
        mToolbar = findViewById(R.id.toolbarEmomActivity);
        mProgressBar = findViewById(R.id.myProgress);
    }

    /**
     * Inizializza e setta le impostazioni della progress bar circolare
     */
    private void progressBarSettings() {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(0);
        mProgressBar.setMax(maxProgress);
        mProgressBar.setIndeterminate(false);

        progressBarHandler = new Handler();
        progressBarRun = new Runnable() {
            @Override
            public void run() {
                int secRemaining = (int) (remainingTime / interval);
                int secDuration = (int) (currentDuration / interval);
                if(secDuration != 0)
                    mProgressBar.setProgress( maxProgress - ((maxProgress * secRemaining) / secDuration)) ;
            }
        };
    }

    /**
     * Carica l'interstitial Ads che viene mostrato quando l'allenamento finisce, prima di
     * mostrare l'activity finish
     */
    private void loadAds() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(TEST_ADS);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    protected String setSetsText(int total, int current) {
        return current + "/" + total;
    }

    /**
     * Imposta l'onClick Listener al pulsante che si occupa di gestire
     * il play/pause del timer e alla text view che mostra il testo
     */
    protected void startButtonCreator() {
        mStartButton.setOnClickListener(clickableTimer());
        mTimeTextView.setOnClickListener(clickableTimer());
    }

    /**
     * Attiva/disattiva il click sui pulsanti che si occupano di gestire il play/pause del timer
     * @param bool se vero abilita, se falso disabilita
     */
    protected void startButtonEnabled(boolean bool) {
        mStartButton.setEnabled(bool);
        mTimeTextView.setClickable(bool);
    }

    /**
     * Ritorna l'OnClickListener che gestisace il play/pause del timer
     * @return L'azione al click sul timer
     */
    private View.OnClickListener clickableTimer() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTimerAction();
            }
        };
    }

    /**
     * Contiene le operazione che vengono effettuate alla pressione del timer
     */
    private void clickTimerAction() {
        if(isRunning) {
            timer.cancel();
            isRunning=false;
            timeHandler.removeCallbacks(updateTimerThread);
            mStartButton.setText(getResources().getString(R.string.start).toUpperCase());
            //mStartButton.setBackground(getResources().getDrawable(R.drawable.ripple_blue));
            mStartButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        }
        else if(isFirstStart) {
            //Se non è running ed è il first start
            timer = readyTimer();
            timer.start();

        }
        else {
            //E' running
            timer = createTimer();
            timer.start();
            isRunning=true;
            timeHandler.postDelayed(updateTimerThread, 10);
            mStartButton.setText(getResources().getString(R.string.pause).toUpperCase());
            //mStartButton.setBackground(getResources().getDrawable(R.drawable.ripple_red));
            mStartButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));

            //mStartButton.setImageResource(R.drawable.ic_pause);
        }
    }

    /**
     * Fa partire il cronometro
     */
    protected void startChronometer() {
        startTime = SystemClock.uptimeMillis();
        timeHandler.postDelayed(updateTimerThread, 10);
        isFirstStart=false;
    }

    /**
     * Gestisce il tick del timer per ogni sottoclasse
     * Gestisce tempo rimanente, millisecondi dallo start, progress bar.
     * A metà lancia un suono
     * Gli ultimi 3 secondi lancia un suono
     * @param millisUntilFinished millisecondi prima della fine
     */
    protected void tickManagement(long millisUntilFinished) {
        /*
        Se il timer dura più di 10 secondi, a metà lancia un segnale
         */
        if(currentDuration >= 10000L && millisUntilFinished<=currentDuration/2 && tickHalf) {
            if(ifSound)
                MediaPlayer.create(this, R.raw.half_sound).start();
            tickHalf = false;
        }
        /*
        Se il timer dura più di 5 secondi, gli ultimi 3 secondi lancia un suono
         */
        if (currentDuration >= 5000 || isFirstStart)
            if(millisUntilFinished<=1000 && tick1000) {
                tick1000=false;
                tickSecondSound();
            }
            else if (millisUntilFinished<=2000 && tick2000) {
                tick2000=false;
                tickSecondSound();
            }
            else if (millisUntilFinished<=3000 && tick3000) {
                tick3000=false;
                tickSecondSound();
            }

        remainingTime =millisUntilFinished;
        progressBarHandler.post(progressBarRun);
        setTimeText();
        mTimeFromStartTextView.setText(chronoTick());
    }

    protected void tickSecondSound() {
        if(ifSound)
            MediaPlayer.create(this, R.raw.second_tick).start(); }

    /**
     * Imposta la text time del tempo al valore della currentDuration
     * Imposta la text view del tempo totale al tempo totale da inizio allenamento
     * Effettua i controlli sulla presenza o no di ore o minuti
     */
    protected void setTimeText() {
        if (Utilities.getHoursFromMills(remainingTime) > 0)
            mTimeTextView.setText(Utilities.getStringTimeFromMills(remainingTime));
        else if (Utilities.getMinutesFromMills(remainingTime) > 0)
            mTimeTextView.setText(Utilities.getStringTimeWithMillsFromMills(remainingTime));
        else
            mTimeTextView.setText(Utilities.getStringTimeWithMillsWithoutMinuteFromMills(remainingTime));

    }

    /**
     * Imposta e fa partire il work
     */
    protected void startWork() {
        remainingTime =work;
        currentDuration = work;
        timer = createTimer();
        timer.start();
        isRunning = true;
        isWork=true;
        mPrimaryTextView.setText(setSetsText(setsNumber, currentSet));
        progressBarHandler.post(progressBarRun);
        workLayoutSettings();

        workSound();
        tick1000=true; tick2000=true; tick3000=true; tickHalf=true;
    }

    /**
     * Fa partire il suono del work
     */
    protected void workSound() {
        if(ifSound)
            MediaPlayer.create(this, R.raw.go_sound).start();
    }

    /**
     * Imposta e fa partire il riposo
     */
    protected void startRest() {
        remainingTime =rest;
        currentDuration = rest;
        timer = createTimer();
        timer.start();
        isRunning = true;
        isWork=false;
        progressBarHandler.post(progressBarRun);
        restLayoutSettings();

        restSound();
        tick1000=true; tick2000=true; tick3000=true; tickHalf=true;
    }

    /**
     * Fa partire il suono del riposo
     */
    protected void restSound() {
        if(ifSound)
            MediaPlayer.create(this, R.raw.rest_sound).start();
    }

    /**
     * Si occupa di gestire il comportamento dell'activity quando termina l'allenamento
     * Ferma il timer
     * Ferma il cronometro
     * Mostra l'ad se è stato caricato
     * Fa paertire l'activity finish
     * Passa all'activity finish un testo standard
     */
    protected void finishCountDown() {
        finishCountDown(res.getString(R.string.workout_completed_in) + " " +
                (Utilities.getHoursFromMills(updateTime) >0 ?
                        Utilities.getStringTimeFromMills(updateTime) + " " + res.getString(R.string.hours) :
                        Utilities.getStringTimeNoHour(updateTime) + " " + res.getString(R.string.minutes)));

    }

    /**
     * Si occupa di gestire il comportamento dell'activity quando termina l'allenamento
     * Ferma il timer
     * Ferma il cronometro
     * Mostra l'ad se è stato caricato
     * Fa paertire l'activity finish
     * @param text Il testo da mostrare nell'activity finish
     */
    protected void finishCountDown(final String text) {

        /*
        Arresto il timer
         */
        try {
            timer.cancel();
        }
        catch (Exception e) {
            /*
            Non faccio niente -> Da eccezione quando il timer è già finito
            Ma va fatto per quando l'utente decide di uscire dal tasto off sul menu
             */
        }
        /*
        Stoppa il cronometro
         */
        timeHandler.removeCallbacks(updateTimerThread);

        /*
        Se l'ad è stato caricato lo mostro prima di passare alla finishActivity
         */
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        else
            //Se per un qualsiasi motivo l'ads non viene caricato
            openFinishActivity(text);


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                openFinishActivity(text);
            }
        });

    }

    /**
     * Si occupa di lanciare la finishActivty passando il testo ricevuto come parametro
     * @param text
     */
    private void openFinishActivity(String text) {
        Intent intent= FinishActivity.getInstance(getApplicationContext(), text ) ;
        /*
        Se deve suonare suona
         */
        if(ifSound)
            MediaPlayer.create(this, R.raw.timer_sound).start();
        startActivity(intent);
        finish();
    }
    /**
     * Imposta il colore della text view e della progress bar del timer
     */
    protected void workLayoutSettings() {
        mTimeTextView.setTextColor(res.getColor(R.color.colorPrimary));
        mWorkDescriptionTextView.setText(res.getString(R.string.work));
        mWorkDescriptionTextView.setTextColor(res.getColor(R.color.colorPrimary));
        Drawable drawable = res.getDrawable(R.drawable.circle_progress_bar_work);
        mProgressBar.setProgressDrawable(drawable);

    }

    /**
     * Imposta i colori dell'activity quando si è in riposo
     */
    protected void restLayoutSettings() {
        //mTimeTextView.setTextColor(res.getColor(R.color.colorAccent));
        mWorkDescriptionTextView.setText(res.getString(R.string.rest));
        //mWorkDescriptionTextView.setTextColor(res.getColor(R.color.colorAccent));
        Drawable drawable = res.getDrawable(R.drawable.circle_progress_bar_rest);
        mProgressBar.setProgressDrawable(drawable);
    }

    /**
     * Imposta il colore della text view e della progress bar quando sono in ready
     */
    protected void readyLayoutSettings() {
        mTimeTextView.setText(Utilities.getStringTimeFromMillsWithoutHours(3000));
        mTimeTextView.setText(res.getString(R.string.tap_to_start));
        remainingTime=READY_TIMER;
        currentDuration =READY_TIMER;
        //mTimeTextView.setTextColor(res.getColor(R.color.colorAccent));
        //mWorkDescriptionTextView.setTextColor(res.getColor(R.color.colorAccent));

        Drawable drawable = res.getDrawable(R.drawable.circle_progress_bar_ready);
        mProgressBar.setProgressDrawable(drawable);
    }

    @Override
    public void onBackPressed() {

        /*
        Quando l'utente preme il tasto back gli viene mostrato il dialog
        Se al dialog dice che vuole uscire viene impostato userWantsExit a true e viene richiamata questa funzione
        Alla seconda chiamata non passa il primo if ed entra nell'else (da dove viene invocato il super di backPressed
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(res.getString(R.string.go_out));
        builder.setMessage(res.getString(R.string.not_able_to_recovery));

        builder.setPositiveButton(res.getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    timer.cancel();
                }
                catch (Exception e) {
                    /*
                       Non fare nulla
                       Entra qui se provo ad uscrie dal circuito senza aver mai avviato il timer
                       */
                }
                finish();
            }
        });

        builder.setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.create().show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem mSoundButton = menu.findItem(R.id.soundToolbar);
        MenuItem mFinishButton = menu.findItem(R.id.finishTrainingToolbar);
        mSoundButton.setVisible(true);
        mFinishButton.setVisible(true);

        mSoundButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(ifSound){
                    ifSound=false;
                    item.setIcon(R.drawable.ic_audio_off);
                }
                else {
                    ifSound=true;
                    item.setIcon(R.drawable.ic_audio_on);
                }
                return true;
            }
        });

        mFinishButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finishButtonClicked();
                return true;
            }
        });

        return true;
    }

    /**
     * Gestisce il dialog chiamato quando viene cliccato il pulsante per terminare
     * in anticipo l'allenamento
     */
    private void finishButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(res.getString(R.string.workout_completed));
        builder.setMessage(res.getString(R.string.not_able_to_recovery));

        builder.setPositiveButton(res.getString(R.string.finished), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishCountDown();
            }
        });
        builder.setNegativeButton(res.getString(R.string.not_yet), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }

    /**
     * Ritorna il valore id ifSound
     * @return variabile che se true vuole dire che il timer produce i suoni
     */
    protected boolean isIfSound() {
        return ifSound;
    }

    /**
     * Imposta il valore id ifSound
     * @param ifSound  variabile che se true vuole dire che il timer produce i suoni
     */
    protected void setIfSound(boolean ifSound) {
        this.ifSound = ifSound;
    }

    /**
     * Crea un timer che dura 3 secondi per permettere all'utente di prepararsi prima di iniziare un allenamento
     * @return Il timer creato
     */
    protected CountDownTimer readyTimer() {
        startButtonEnabled(false);
        mStartButton.setText(getResources().getString(R.string.pause).toUpperCase());
        //mStartButton.setBackground(getResources().getDrawable(R.drawable.ripple_red));
        mStartButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        mWorkDescriptionTextView.setText(res.getString(R.string.ready));
        progressBarHandler.post(progressBarRun);
        /*
        Ritorna il timer
         */
        return new CountDownTimer(remainingTime, interval) {
            @Override
            public void onTick(long millisUntilFinished) { tickManagement(millisUntilFinished); }

            @Override
            public void onFinish() {
                mTimeFromStartTextView.setVisibility(TextView.VISIBLE);
                startChronometer();
                startButtonEnabled(true);
                isFirstStart=false;
                initializesTimer();
                startWork();
            }
        };
    }

    /**
     * Ogni sottoclasse deve implementare il suo timer
     */
    protected abstract CountDownTimer createTimer();

    /**
     * Comportamento del timer dopo il ready -> 3,2,1
     * Dovrebbe caricare il primo esercizio
     */
    protected abstract void initializesTimer();

}
