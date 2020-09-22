package com.italianswapp.yourtraining3.WorkoutProposed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.italianswapp.yourtraining3.R;
import com.italianswapp.yourtraining3.WorkoutProposed.Workout.Workout;

import java.util.ArrayList;

/**
 * Adapter del RecyclerView che andrà nell'activity ProposedWorkoutActivity
 */
public class WorkoutSummaryRecyclerViewAdapter extends  RecyclerView.Adapter<WorkoutSummaryRecyclerViewAdapter.ViewHolder> {

    /**
     * Lista che popolerà la RecyclerView
     */
    private ArrayList<Workout> workoutList;

    private Context context;


    public WorkoutSummaryRecyclerViewAdapter(ArrayList<Workout> workoutList, Context context) {
        this.workoutList = workoutList;
        this.context = context;
        Toast.makeText(context, "Sono qui", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public WorkoutSummaryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_summary_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutSummaryRecyclerViewAdapter.ViewHolder holder, int position) {
        Workout workout = workoutList.get(position);

        holder.mTitleTextView.setText(workout.getTitle());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        holder.mExerciseListRecyclerView.setLayoutManager(linearLayoutManager);
        holder.mExerciseListRecyclerView.setAdapter(new ExerciseSummaryRecyclerViewAdapter(workout.getExerciseList(), context));

    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mColoredView;
        private TextView mTitleTextView;
        private RecyclerView mExerciseListRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mColoredView = itemView.findViewById(R.id.coloredViewWorkoutSummary);
            mTitleTextView = itemView.findViewById(R.id.titleWorkoutSummary);
            mExerciseListRecyclerView = itemView.findViewById(R.id.exerciseSummaryRecyclerView);
        }
    }
}
