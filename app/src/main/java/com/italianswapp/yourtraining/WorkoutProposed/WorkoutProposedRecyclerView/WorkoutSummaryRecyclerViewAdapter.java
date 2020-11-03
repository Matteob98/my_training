package com.italianswapp.yourtraining.WorkoutProposed.WorkoutProposedRecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.italianswapp.yourtraining.ExerciseTypeNotCorrectException;
import com.italianswapp.yourtraining.R;
import com.italianswapp.yourtraining.Timer.Circuit.CircuitCreatorActivity;
import com.italianswapp.yourtraining.WorkoutProposed.Workout.Workout;

import java.util.ArrayList;

/**
 * Adapter del RecyclerView che andrà nell'activity ProposedWorkoutActivity
 */
public class WorkoutSummaryRecyclerViewAdapter extends  RecyclerView.Adapter<WorkoutSummaryRecyclerViewAdapter.ViewHolder> {

    private static final String AD_CODE_TEST = "ca-app-pub-3940256099942544/5224354917";
    private static final String AD_CODE = "ca-app-pub-8919261416525349/5604075110";
    /**
     * Lista che popolerà la RecyclerView
     */
    private ArrayList<Workout> workoutList;

    private Context context;

    /**
     * Ad per accedere agli allenamenti premium
     */
    private RewardedAd rewardedAd;


    public WorkoutSummaryRecyclerViewAdapter(ArrayList<Workout> workoutList, Context context) {
        this.workoutList = workoutList;
        this.context = context;

        loadAd();
    }

    /**
     * Carica l'ad per accedere agli allenamenti premium
     */
    private void loadAd() {
        rewardedAd = new RewardedAd(context, AD_CODE);

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {

            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

    /**
     * Mostra l'ad precedentemente caricato e successivamente fa partire l'allenamento
     * Nel caso in cui l'ad non sia stato correttamente caricato lancia un messaggio di errore
     * @param thisWorkout
     */
    private void showPremiumAd(final Workout thisWorkout) {
        if (rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                @Override
                public void onRewardedAdClosed() {
                    loadAd();
                    // Ad closed.
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    Intent intent = CircuitCreatorActivity.getInstance(context, thisWorkout);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    // Ad failed to display.
                }
            };
            rewardedAd.show((Activity) context, adCallback);
        } else {
            Snackbar.make(((Activity)context).getWindow().getDecorView().getRootView() //prendo la view dal context
                    , context.getResources().getString(R.string.general_error), BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public WorkoutSummaryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_summary_card, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull WorkoutSummaryRecyclerViewAdapter.ViewHolder holder, final int position) {
        Workout workout = workoutList.get(position);

        holder.mTitleTextView.setText(workout.getTitle());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        holder.mExerciseListRecyclerView.setLayoutManager(linearLayoutManager);
        holder.mExerciseListRecyclerView.setAdapter(new ExerciseSummaryRecyclerViewAdapter(workout.getExerciseList(), context));

        /*
        Cliccando sulla card parte l'allenamento
         */
        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Workout thisWorkout = workoutList.get(position);
                if (thisWorkout.isPremium()) {
                    showPremiumAd(thisWorkout);
                }
                else {
                    Intent intent = CircuitCreatorActivity.getInstance(context, thisWorkout);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            }
        });

        /*
        Se è un allenamento premium mostro la stellina
         */
        if(workout.isPremium())
            holder.mPremiumImage.setVisibility(View.VISIBLE);
        else
            holder.mPremiumImage.setVisibility(View.GONE);

        /*
        In base alla difficoltà imposto l'immagine sulla card
         */
        Drawable levelImage = null;
        switch (workout.getLevel()) {
            case BEGINNER:
                levelImage = context.getResources().getDrawable(R.drawable.beginner);
                break;
            case INTERMEDIATE:
                levelImage = context.getResources().getDrawable(R.drawable.intermediate);
                break;
            case ADVANCED:
                levelImage = context.getResources().getDrawable(R.drawable.advanced);
                break;
            default:
                try {
                    throw new ExerciseTypeNotCorrectException("Livello non valido");
                } catch (ExerciseTypeNotCorrectException e) {
                    e.printStackTrace();
                }
        }
        holder.mLevelImage.setBackground(levelImage);
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTextView;
        private RecyclerView mExerciseListRecyclerView;
        private CardView mCard;
        private ImageView mPremiumImage, mLevelImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitleTextView = itemView.findViewById(R.id.titleWorkoutSummary);
            mExerciseListRecyclerView = itemView.findViewById(R.id.exerciseSummaryRecyclerView);
            mCard = itemView.findViewById(R.id.workout_summary_card);
            mPremiumImage = itemView.findViewById(R.id.premiumImageWorkoutSummary);
            mLevelImage = itemView.findViewById(R.id.levelImageWorkoutSummary);
        }
    }
}
