package com.italianswapp.yourtraining.Chronometer;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.italianswapp.yourtraining.R;

import java.util.List;

public class RoundRecyclerViewAdapter extends RecyclerView.Adapter<RoundRecyclerViewAdapter.LapViewHolder> {

    List<Lap> laps;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * Viene richiamato quando l'adapter deve essere creato
     */
    @NonNull
    @Override
    public LapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lap_card, parent, false);
        return  new LapViewHolder(v,mListener);
    }

    /**
     * Specifica il contenuto di ciascun elemento della recycler view
     * @param lapViewHolder La vista
     * @param position La posizione all'interno della recycler view
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LapViewHolder lapViewHolder, int position) {
        //android.content.res.Resources res = lapViewHolder.lapNumberText.getResources();
        lapViewHolder.lapNumberText.setText(""+laps.get(position).getNumberLap());
        lapViewHolder.totalTimeText.setText(""+laps.get(position).getStringTotalTime());
        lapViewHolder.lapTimeText.setText(""+laps.get(position).getStringLapTime());
    }

    /**
     * Ritorna il numero di elementi presenti nella lista
     * @return numero intero
     */
    @Override
    public int getItemCount() {
        return laps.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * Classe che serve per creare l'holder della card
     */
    public class LapViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView lapNumberText, totalTimeText, lapTimeText;
        ImageButton deleteButton;

        public LapViewHolder(@NonNull View itemView,  final OnItemClickListener listener) {
            super(itemView);

            cardView = itemView.findViewById(R.id.lap_card);
            lapNumberText = itemView.findViewById(R.id.lapNumberTextCard);
            totalTimeText = itemView.findViewById(R.id.totalTimeTextCard);
            lapTimeText = itemView.findViewById(R.id.lapTimeTextCard);
            deleteButton = itemView.findViewById(R.id.deleteButtonCard);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public RoundRecyclerViewAdapter(List<Lap> laps) {
        this.laps = laps;
    }
}
