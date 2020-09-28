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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        bottomNavigationView = findViewById(R.id.bottomNavigationMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.frameLayoutMenuActivity, new TimerFragment()).commit();
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment selectedFragment = null;

        switch (id){
            case R.id.savedItem:
                selectedFragment = new WorkoutSavedFragment();
                break;
            case R.id.timerItem:
                selectedFragment = new TimerFragment();
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


}

