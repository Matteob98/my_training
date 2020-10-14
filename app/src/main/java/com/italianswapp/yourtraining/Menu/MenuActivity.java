package com.italianswapp.yourtraining.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.italianswapp.yourtraining.R;

public class MenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    /**
     * Variabile che indica se l'ultima volta che mi sono spostato di activity mi sono spostato alla home
     */
    private boolean isHome;

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


}

