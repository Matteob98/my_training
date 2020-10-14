package com.italianswapp.yourtraining.Menu;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.italianswapp.yourtraining.WorkoutProposed.ProposedWorkoutsActivity;
import com.italianswapp.yourtraining.R;
import com.italianswapp.yourtraining.WorkoutProposed.Workout.Workout;

public class WorkoutsProposedFragment extends Fragment implements View.OnClickListener {

    private View view;
    private CardView mbodybuildingButton, mFunctionalTrainingButton, mFreeBodyButton, mStretchingButton;

    public WorkoutsProposedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_workouts_proposed, container, false);

        mbodybuildingButton = view.findViewById(R.id.bodybuildingButton);
        mFreeBodyButton = view.findViewById(R.id.freeBodyButton);
        mStretchingButton = view.findViewById(R.id.stretchingButton);
        mFunctionalTrainingButton = view.findViewById(R.id.functionalTrainingButton);

        mbodybuildingButton.setOnClickListener(this);
        mFunctionalTrainingButton.setOnClickListener(this);
        mFreeBodyButton.setOnClickListener(this);
        mStretchingButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        Workout.WorkoutCategory workoutCategory = null;
        switch (id) {
            case R.id.bodybuildingButton:
                workoutCategory = Workout.WorkoutCategory.BODYBUILDING;
                break;
            case R.id.freeBodyButton:
                workoutCategory = Workout.WorkoutCategory.FREEBODY;
                break;
            case R.id.stretchingButton:
                workoutCategory = Workout.WorkoutCategory.STRETCHING;
                break;
            case R.id.functionalTrainingButton:
                workoutCategory = Workout.WorkoutCategory.FUNCTIONALTRAINING;
                break;
            default:
                Log.d("levelSelection", "Selezionato un tipo non valido");
                return;


        }

        Intent intent = ProposedWorkoutsActivity.getInstance(getContext(), workoutCategory);
        startActivity(intent);

    }
}