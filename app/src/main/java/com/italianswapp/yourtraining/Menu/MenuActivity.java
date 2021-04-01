package com.italianswapp.yourtraining.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.italianswapp.yourtraining.R;
import com.italianswapp.yourtraining.Utilities.Utilities;

public class MenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String REVIEW_PREFERENCE_KEY = "in-app-key";
    private static final String NO_REVIEW_PREFERENCE = "no-review";

    private BottomNavigationView bottomNavigationView;

    /**
     * Variabile che indica se l'ultima volta che mi sono spostato di activity mi sono spostato alla home
     */
    private boolean isHome;

    /*
    Variabili per chiedere la recensione all'utente
     */
    ReviewManager reviewManager;
    ReviewInfo reviewInfo = null;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        bottomNavigationView = findViewById(R.id.bottomNavigationMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        isHome=true;

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.frameLayoutMenuActivity, new HomeFragment()).commit();
        }

        sharedPref = getSharedPreferences(REVIEW_PREFERENCE_KEY, Context.MODE_PRIVATE);
        if (!(sharedPref.getString(REVIEW_PREFERENCE_KEY, "si-review").equals(NO_REVIEW_PREFERENCE))) {
            getReviewInfo();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment selectedFragment = null;
        isHome=false;

        switch (id){
            case R.id.savedItem:
                selectedFragment = new WorkoutSavedFragment();
                break;
            case R.id.timerItem:
                selectedFragment = new HomeFragment();
                isHome=true;
                break;
            case R.id.proposedItem:
                selectedFragment = new WorkoutsProposedFragment();
                break;
            default:
                return false;
        }
        getSupportFragmentManager().beginTransaction().
                replace(R.id.frameLayoutMenuActivity, selectedFragment).commit();
        return true;
    }

    @Override
    public void onBackPressed() {

        if (!isHome) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayoutMenuActivity, new HomeFragment())
                    .commit();
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
            isHome=true;
        }
        else
            System.exit(0);
    }

    private void getReviewInfo() {
        reviewManager = ReviewManagerFactory.create(getApplicationContext());
        Task<ReviewInfo> manager = reviewManager.requestReviewFlow();
        manager.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                reviewInfo = task.getResult();
            }
        });

        showReviewDialog();
    }

    private void showReviewDialog() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        dialog
                .setTitle(getResources().getString(R.string.leave_review))
                .setMessage(getResources().getString(R.string.leave_review_description))
                .setNeutralButton(getResources().getString(R.string.remind_later), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse(Utilities.PLAY_STORE_ADDRESS));
                        startActivity(viewIntent);
                    }
                })
                .show();
    }

    public void startReviewFlow() {
        if (reviewInfo != null) {
            Task<Void> flow = reviewManager.launchReviewFlow(this, reviewInfo);
            flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    setNoReviewPreference();
                }
            });
        }
    }

    public void setNoReviewPreference() {
        sharedPref.edit().putString(REVIEW_PREFERENCE_KEY, NO_REVIEW_PREFERENCE).apply();
    }


}

