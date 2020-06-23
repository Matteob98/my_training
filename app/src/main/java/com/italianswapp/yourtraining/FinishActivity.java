package com.italianswapp.yourtraining;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FinishActivity extends AppCompatActivity {

    private final static String FINISH = "finish";
    private final static String TEST_ADS ="ca-app-pub-3940256099942544/1033173712";
    private final static String ADS_CODE = "ca-app-pub-8919261416525349/9438301000";


    private FloatingActionButton mHomeButton;
    private TextView mFinishTextView;

    /**
     * Banner a schermo intero che viene visualizzato appena viene aperta l'applicazione
     */
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

       //loadInterstitialAd();

        mFinishTextView = findViewById(R.id.finishTextView);
        mHomeButton = findViewById(R.id.homeButtonFinishActivity);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { goToHome(); }
        });

        String text = getIntent().getStringExtra(FINISH);
        mFinishTextView.setText(text);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(TEST_ADS);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        //TODO non carica l'ad
        mInterstitialAd.show();
        /*
        if (mInterstitialAd.isLoaded())
            mInterstitialAd.show();
        else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

         */

    }

    /**
     * Carica il banner pubblicitario a schermo intero
     */
    private void loadInterstitialAd() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(TEST_ADS);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        /*
        Ora il banner è solo caricato, viene mostrato alla fine fine di onCreate
         */
    }

    private void goToHome() {
        finish();
    }

    @Override
    public void onBackPressed() { goToHome();}

    public static Intent getInstance (Context context, String testo) {
        Intent intent = new Intent(context, FinishActivity.class);
        intent.putExtra(FINISH,testo);
        return intent;
    }
}
