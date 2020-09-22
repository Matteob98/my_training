package com.italianswapp.yourtraining3.Menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.italianswapp.yourtraining3.R;

public class WorkoutSavedFragment extends Fragment {

    /*
    Todo mostrare immagine quando la recycler view è vuota
     */

    private View view;

    private Button mFilterButton;
    private RecyclerView mRecyclerView;


    public WorkoutSavedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_workouts_saved, container, false);

        layoutSettings();

        return view;
    }

    private void layoutSettings() {
        mFilterButton = view.findViewById(R.id.filterButtonWorkoutSaved);
        mRecyclerView = view.findViewById(R.id.recyclerViewWorkoutSaved);
    }
}