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
import com.italianswapp.yourtraining.WorkoutProposed.Workout.Workout;

import java.util.ArrayList;

public class LevelFilterSelectRecyclerViewAdapter extends  RecyclerView.Adapter<LevelFilterSelectRecyclerViewAdapter.ViewHolder>{

    private ArrayList<Workout.WorkoutLevel> levelGroupsList;
    private ArrayList<Workout.WorkoutLevel> levelFilterList;
    private Context context;

    public LevelFilterSelectRecyclerViewAdapter(ArrayList<Workout.WorkoutLevel> levelGroupsList, ArrayList<Workout.WorkoutLevel> levelFilterList, Context context) {
        this.levelGroupsList = levelGroupsList;
        this.levelFilterList = levelFilterList;
        this.context = context;
    }

    @NonNull
    @Override
    public LevelFilterSelectRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.filter_select_workouts_proposed_card, parent, false);
        return new LevelFilterSelectRecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final LevelFilterSelectRecyclerViewAdapter.ViewHolder holder, int position) {
        final Workout.WorkoutLevel level = levelGroupsList.get(position);

        holder.mTextView.setText(level.toString());

        if( levelFilterList.contains(level)){
            holder.mTextView.setTextColor(context.getResources().getColor(R.color.white));
            holder.mCardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! levelFilterList.contains(level)) {
                    levelFilterList.add(level);
                    holder.mTextView.setTextColor(context.getResources().getColor(R.color.white));
                    holder.mCardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

                }
                else {
                    levelFilterList.remove(level);
                    holder.mTextView.setTextColor(context.getResources().getColor(R.color.black));
                    holder.mCardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return levelGroupsList.size();
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
