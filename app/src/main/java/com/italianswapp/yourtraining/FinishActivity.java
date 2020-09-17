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

    /*
    todo sono qui il 18/98
    Aggiunto il pulsante per salvare
    Ora deve aprire una dialog in cui inserire il nome dell'allenamento, le sensazioni e la difficoltà, per poi salvare
     */
    private final static String FINISH = "finish";

    private FloatingActionButton mHomeButton;
    private TextView mFinishTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        mFinishTextView = findViewById(R.id.finishTextView);
        mHomeButton = findViewById(R.id.homeButtonFinishActivity);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { goToHome(); }
        });

        String text = getIntent().getStringExtra(FINISH);
        mFinishTextView.setText(text);

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
