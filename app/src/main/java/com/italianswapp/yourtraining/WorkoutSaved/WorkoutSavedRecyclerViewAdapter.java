package com.italianswapp.yourtraining.WorkoutSaved;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.italianswapp.yourtraining.OfflineDatabase.WorkoutSaved;
import com.italianswapp.yourtraining.R;
import com.italianswapp.yourtraining.WorkoutProposed.Workout.Workout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class WorkoutSavedRecyclerViewAdapter  extends RecyclerView.Adapter<WorkoutSavedRecyclerViewAdapter.WorkoutSavedViewHolder> {

    private final DateFormat DATE_FORMAT =  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private List<WorkoutSaved> workoutSavedList;
    private Context context;

    public WorkoutSavedRecyclerViewAdapter(List<WorkoutSaved> workoutSavedList, Context context) {
        this.workoutSavedList = workoutSavedList;
        this.context = context;
    }

    @NonNull
    @Override
    public WorkoutSavedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_saved_card, parent, false);
        return new WorkoutSavedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutSavedViewHolder holder, int position) {
        WorkoutSaved workoutSaved = workoutSavedList.get(position);

        holder.mImage.setBackgroundResource(getImageByWorkoutType(workoutSaved.getType()));
        holder.mSensation.setBackgroundResource(getSensationImage(workoutSaved.getSensation()));
        holder.mLevel.setBackgroundResource(getLevelImage(workoutSaved.getWorkout().getLevel()));

        holder.mTitle.setText(workoutSaved.getWorkout().getTitle());
        holder.mDate.setText(DATE_FORMAT.format(workoutSaved.getDate()));
        holder.mTime.setText(workoutSaved.getTime());
    }

    @Override
    public int getItemCount() { return workoutSavedList.size(); }

    private int getLevelImage(Workout.WorkoutLevel level) {
        //todo aggiorna immagini nello switch
        int res;
        switch (level) {
            case BEGINNER:
                res = R.drawable.ic_level;
                break;
            case INTERMEDIATE:
                res = R.drawable.ic_level;
                break;
            case ADVANCED:
                res = R.drawable.ic_level;
                break;
            default:
                Log.d("levelImage", "Livello sbagliato");
                throw new IllegalArgumentException();

        }
        return res;
    }

    private int getSensationImage(WorkoutSaved.WorkoutSensation sensation) {
        //todo aggiorna immagini nello switch
        int res;
        switch (sensation) {
            case EASY:
                res = R.drawable.ic_smile;
                break;
            case NORMAL:
                res = R.drawable.ic_smile;
                break;
            case DIFFICULT:
                res = R.drawable.ic_smile;
                break;
            default:
                Log.d("sensationImage", "sensazione sbagliata");
                throw new IllegalArgumentException();

        }
        return res;
    }

    private int getImageByWorkoutType(WorkoutSaved.WorkoutType type) {
        //todo aggiorna immagini nello switch
        int res;
        switch (type) {
            case WORKOUT:
                res = R.drawable.personal_trainer;
                break;
            case CHRONOMETER:
                res = R.drawable.chronometer;
                break;
            case TABATA:
                res = R.drawable.tabata;
                break;
            case AMRAP:
                res = R.drawable.amrap;
                break;
            case TIMER:
                res = R.drawable.timer;
                break;
            case EMOM:
                res = R.drawable.emom;
                break;
            case CONCENTRIC_PHASE:
                res = R.drawable.concentrich_phase;
                break;
            case BODYBUILDING:
                res = R.drawable.ic_smile;
                break;
            case FUNCTIONALTRAINING:
                res = R.drawable.ic_smile;
                break;
            case FREEBODY:
                res = R.drawable.ic_smile;
                break;
            case STRETCHING:
                res = R.drawable.ic_smile;
                break;
            default:
                Log.d("typeImage", "tipo sbagliato");
                throw new IllegalArgumentException();

        }
        return res;
    }




    public class WorkoutSavedViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImage, mSensation, mLevel;
        /*
        Nota: Time si riferisce al tempo impiegato, non all'ora
        Nota 2: Date è della forma dd:mm:yy hh:mm:ss
         */
        private TextView mTitle, mDate, mTime;

        public WorkoutSavedViewHolder(@NonNull View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.imageWorkoutSavedCard);
            mSensation = itemView.findViewById(R.id.sensationWorkoutSavedCard);
            mLevel = itemView.findViewById(R.id.levelWorkoutSavedCard);
            mTitle = itemView.findViewById(R.id.titleTextWorkoutSavedCard);
            mDate = itemView.findViewById(R.id.dateWorkoutSavedCard);
            mTime = itemView.findViewById(R.id.timeWorkoutSavedCard);
        }
    }
}
