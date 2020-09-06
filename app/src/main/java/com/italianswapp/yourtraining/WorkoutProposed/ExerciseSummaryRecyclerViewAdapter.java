package com.italianswapp.yourtraining.WorkoutProposed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.italianswapp.yourtraining.ExerciseTypeNotCorrectException;
import com.italianswapp.yourtraining.R;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings.Exercise;
import com.italianswapp.yourtraining.Utilities;

import java.util.ArrayList;

/**
 * Adapter della RecyclerView contenuta nella Card di riepilogo dell'esercizio
 */
public class ExerciseSummaryRecyclerViewAdapter extends RecyclerView.Adapter<ExerciseSummaryRecyclerViewAdapter.ViewHolder> {

    /**
    Lista che popolerà la recyclerView
     */
    private ArrayList<Exercise> exerciseList;

    private Context context;

    public ExerciseSummaryRecyclerViewAdapter(ArrayList<Exercise> exerciseList, Context context) {
        this.exerciseList = exerciseList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.exercise_summary_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseSummaryRecyclerViewAdapter.ViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);

        /*
        Se non è SUPERSET imposto normalmente il nome, altrimenti imposto entrambi i nomi degli esercizi in superserie
         */
        if (exercise.getType() != Exercise.CircuitType.SUPERSET)
            holder.mName.setText(exercise.getName());
        else {
            try {
                holder.mName.setText(exercise.getName() + " + " + exercise.getSupersetExercise().getName());
            } catch (ExerciseTypeNotCorrectException e) {
                e.printStackTrace();
            }
        }
        holder.mReps.setText(getRepsString(exercise));
        //holder.mOperator.setText(getOperatorOfExercise(exercise));

        /*
        Se non è REST imposto normalmente le serie, altrimenti se è rest oscuro sia sets che operator
         */
        if (exercise.getType()!= Exercise.CircuitType.REST)
            holder.mSets.setText(exercise.getTotalSets());
        else {
            holder.mSets.setText("");
            holder.mOperator.setText("");
        }
        holder.mColoredView.setBackground(Utilities.getColorOfExercise(context.getResources(), exercise.getType()));
    }

    /**
     * Ritorna una stringa nel formato giusto per essere inserita come stringa dato l'esercizio passato in input
     * @param exercise Esercizio
     * @return Stringa
     */
    private String getRepsString(Exercise exercise) {
        String reps = "";
        switch (exercise.getType()) {
            case EXERCISE:
                if (exercise.isReps())
                    /*
                    3 X 15
                     */
                    reps = String.valueOf(exercise.getReps());
                else
                    /*
                    00:30 x 15
                     */
                    reps = Utilities.getStringTimeFromMillsWithoutHours(exercise.getReps());
                break;
            case REST:
                /*
                00:30
                 */
                    reps = String.valueOf(exercise.getRec());
                break;
            case SUPERSET:
                /*
                00:30 + 15 reps x 5
                 */
                try {
                    /*
                    Try catch per il getSupersetExercise che potrebbe dare errore
                     */
                    reps =
                            exercise.isReps() ?
                                    String.valueOf(exercise.getReps()) :
                                    Utilities.getStringTimeFromMillsWithoutHours(exercise.getReps());

                    reps += " + ";

                    reps +=
                            exercise.getSupersetExercise().isReps() ?
                                    exercise.getSupersetExercise().getReps():
                                    Utilities.getStringTimeFromMillsWithoutHours(exercise.getReps());

                } catch (ExerciseTypeNotCorrectException e) {
                    e.printStackTrace();
                }
                break;
            case TABATA:
                /*
                00:30 work 00:10 rest x 10
                 */
                reps = Utilities.getStringTimeFromMillsWithoutHours(exercise.getReps()) + " " +
                        context.getResources().getString(R.string.work) + " " +
                        Utilities.getStringTimeFromMillsWithoutHours((int) exercise.getRec()) + " " +
                        context.getResources().getString(R.string.rest);
                break;
            case EMOM:
                /*
                00:30 x 15
                 */
                reps = Utilities.getStringTimeFromMillsWithoutHours(exercise.getReps());
                break;
            default:
                try {
                    throw new ExerciseTypeNotCorrectException("Tipo non corretto in getExerciseColor");
                } catch (ExerciseTypeNotCorrectException e) {
                    e.printStackTrace();
                }
                return null;
        }
        return reps;
    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mColoredView;
        private TextView mName, mReps, mOperator, mSets;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mColoredView = itemView.findViewById(R.id.colorBarExerciseSummary);
            mName = itemView.findViewById(R.id.exerciseNameExerciseSummary);
            mReps = itemView.findViewById(R.id.exerciseRepsExerciseSummary);
            mOperator = itemView.findViewById(R.id.operatorExerciseSummary);
            mSets = itemView.findViewById(R.id.exerciseSetsExerciseSummary);

        }
    }


}
