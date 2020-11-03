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

public class MuscleFilterSelectRecyclerViewAdapter extends RecyclerView.Adapter<MuscleFilterSelectRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Workout.MuscleGroup> muscleGroupsList;
    private ArrayList<Workout.MuscleGroup> muscleFilterList;
    private Context context;

    public MuscleFilterSelectRecyclerViewAdapter(ArrayList<Workout.MuscleGroup> muscleGroupsList, ArrayList<Workout.MuscleGroup> muscleFilterList, Context context) {
        this.muscleGroupsList = muscleGroupsList;
        this.muscleFilterList = muscleFilterList;
        this.context = context;
    }

    @NonNull
    @Override
    public MuscleFilterSelectRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.filter_select_workouts_proposed_card, parent, false);
        return new MuscleFilterSelectRecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MuscleFilterSelectRecyclerViewAdapter.ViewHolder holder, int position) {
        final Workout.MuscleGroup muscleGroup = muscleGroupsList.get(position);

        holder.mTextView.setText(muscleGroup.toString());

        if( muscleFilterList.contains(muscleGroup)){
            holder.mTextView.setTextColor(context.getResources().getColor(R.color.white));
            holder.mCardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! muscleFilterList.contains(muscleGroup)) {
                    muscleFilterList.add(muscleGroup);
                    holder.mTextView.setTextColor(context.getResources().getColor(R.color.white));
                    holder.mCardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

                }
                else {
                    muscleFilterList.remove(muscleGroup);
                    holder.mTextView.setTextColor(context.getResources().getColor(R.color.black));
                    holder.mCardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return muscleGroupsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView mCardView;
        private TextView mTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mCardView =  itemView.findViewById(R.id.cardFilterSelectWorkoutsProposedCard);
            mTextView = itemView.findViewById(R.id.textFilterSelectWorkoutsProposedCard);
        }
    }
}
