package com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings;

import android.os.Parcel;

/**
 * Classe che permette di aggiungere EMOM all'interno di un circuito
 */
class EmomSettings extends ExerciseSettings {

    /**
     * Inserisce un EMOM all'interno del circuito
     * @param name Il nome dell'emom
     * @param reps Il numero di secondi ogni quanto finire un esercizio
     * @param repetition Il numero di ripetizioni
     */
    public EmomSettings(String name, int reps, int repetition) {
        super(name, reps, 0, repetition, false, false, CircuitType.EMOM);
        this.setType(CircuitType.EMOM);
    }

    protected EmomSettings(Parcel in) {
        super(in);
    }

    /*
    Blocca le funzioni della superclasse che su rest non vanno utilizzate
     */

    /**
     * Funzione non utilizzabile  da EmomSettings
     * @param rec Il recupero
     */
    @Override
    public void setRec(long rec) {
        /*
        Non fare nulla
         */
    }

    /**
     * Funzione non utilizzabile da EmomSettings
     * @param reps Se è a ripetizioni
     */
    @Override
    public void setReps(boolean reps) {
         /*
        Non fare nulla
         */
    }

    /**
     * Funzione non utilizzabile da EmomSettings
     * @param hasRecs Se ha riposo
     */
    @Override
    public void setHasRecs(boolean hasRecs) {
         /*
        Non fare nulla
         */
    }
}
