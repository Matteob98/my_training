package com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings;

import android.os.Parcel;

/**
 * Classe che consente di aggiungere tabata all'interno dell'activity
 */
class TabataSettings extends ExerciseSettings {

    /**
     * Crea un tabata da inserire nel circuito
     * @param name Il nome del tabata
     * @param reps Il numero di secondi di lavoro
     * @param rec Il recupero dopo il lavoro
     * @param repetition Il numero di ripetizioni
     */
    public TabataSettings(String name, int reps, long rec, int repetition) {
        super(name, reps, rec, repetition, false, true);
        this.setType(CircuitType.TABATA);
    }

    protected TabataSettings(Parcel in) {
        super(in);
    }

    /*
    Blocca le funzioni della superclasse che su rest non vanno utilizzate
     */

    /**
     * Funzione non utilizzabile da TabataSettings
     * @param reps Se è a ripetizioni
     */
    @Override
    public void setReps(boolean reps) {
         /*
        Non fare nulla
         */
    }

    /**
     * Funzione non utilizzabile da TabataSettings
     * @param hasRecs Se ha riposo
     */
    @Override
    public void setHasRecs(boolean hasRecs) {
         /*
        Non fare nulla
         */
    }
}
