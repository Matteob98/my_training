package com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings;

import android.content.res.Resources;
import android.os.Parcel;

import com.italianswapp.yourtraining.R;

/**
 * Classe che consente di aggiungere riposi all'interno dell'activity
 */
class RestSettings extends ExerciseSettings {

    private final static String REST_STRING = Resources.getSystem().getString(R.string.rest);

    /**
     * Imposta una fase di riposo all'interno del circuito
     * @param rec Il riposo
     */
    public RestSettings(long rec) {
        super(REST_STRING, 0, rec, 0, false, true, CircuitType.REST);
        this.setType(CircuitType.REST);
    }

    protected RestSettings(Parcel in) {
        super(in);
    }

    /*
    Blocca le funzioni della superclasse che su rest non vanno utilizzate
     */

    /**
     * Funzione non utilizzabile in restSettings
     * @param name Il nome
     */
    @Override
    public void setName(String name) {
        /*
        Non fare nulla
         */
    }

    /**
     * Funzione non utilizzabile in restSettings
     * @param reps Le ripetizioni
     */
    @Override
    public void setReps(int reps) {
         /*
        Non fare nulla
         */
    }

    /**
     * Funzione non utilizzabile in restSettings
     * @param repetition Le ripetizioni
     */
    @Override
    public void setRepetition(int repetition) {
         /*
        Non fare nulla
         */
    }

    /**
     * Funzione non utilizzabile in restSettings
     * @param reps Se è a ripetizioni
     */
    @Override
    public void setReps(boolean reps) {
         /*
        Non fare nulla
         */
    }

    /**
     * Funzione non utilizzabile in restSettings
     * @param hasRecs Se ha riposo
     */
    @Override
    public void setHasRecs(boolean hasRecs) {
         /*
        Non fare nulla
         */
    }
}
