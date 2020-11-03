package com.italianswapp.yourtraining.WorkoutProposed.Filter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.italianswapp.yourtraining.R;
import com.italianswapp.yourtraining.WorkoutProposed.ProposedWorkoutsActivity;
import com.italianswapp.yourtraining.WorkoutProposed.Workout.Workout;

import java.util.ArrayList;

public class FilterRecyclerViewAdapter extends RecyclerView.Adapter<FilterRecyclerViewAdapter.ViewHolder> {

    private ArrayList<FilterCard> filterList;
    private ArrayList<Workout.WorkoutLevel> levelFilter;
    private ArrayList<Workout.MuscleGroup> muscleFilter;
    private Context context;
    private ProposedWorkoutsActivity activity;

    public FilterRecyclerViewAdapter(ArrayList<Workout.WorkoutLevel> levelFilter, ArrayList<Workout.MuscleGroup> muscleFilter, ProposedWorkoutsActivity activity, Context context) {
        this.filterList = new ArrayList<>();
        this.levelFilter = levelFilter;
        this.muscleFilter = muscleFilter;
        this.activity = activity;
        this.context = context;

        /*
        Aggiungo ai filtri i livelli
         */
        for (Workout.WorkoutLevel level : levelFilter)
            filterList.add(new FilterCard(level));
        /*
        Aggiungo ai filtri i gruppi muscolari
         */
        for (Workout.MuscleGroup muscleGroup: muscleFilter)
            filterList.add(new FilterCard(muscleGroup));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.filter_workout_proposed_card, parent, false);
        return new FilterRecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final FilterCard filterCard = filterList.get(position);

        holder.mTitleTextView.setText(filterCard.getName());
        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterCard.getType()== FilterCard.Type.LEVEL) {
                    levelFilter.remove(filterCard.getWorkoutLevel());
                }
                else {
                    muscleFilter.remove(filterCard.getMuscleGroup());
                }
                activity.updateFilterRecyclerView();
                activity.updateWorkoutList();
            }
        });
    }


    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTextView;
        private CardView mCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitleTextView = itemView.findViewById(R.id.titleFilterWorkoutProposedCard);
            mCard = itemView.findViewById(R.id.cardFilterWorkoutProposedCard);

        }
    }
}
