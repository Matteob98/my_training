package com.italianswapp.yourtraining.Timer.Circuit.CircuitSettings;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Classe che consente di gestire i dati delle card per la creazione di
 * esercizi all'interno del circuito
 */
public class ExerciseSettings implements Parcelable {

    private int reps, repetition;
    private long rec;
    private boolean isReps, hasRecs;
    private String name;

    public enum CircuitType {
        EXERCISE,
        TABATA,
        REST,
        EMOM
    }

    private CircuitType type;


    /**
     * Costruttore della classe
     * @param reps Numero di ripetizioni o numero di secondi di lavoro
     * @param rec Secondi di recupero
     * @param repetition Numero di volte che va ripetuto il round
     * @param isReps se vero è un esercizio a ripetizioni, altrimenti è un esercizio a tempo
     * @param hasRecs se vero dopo l'esercizio c'è recupero, altrimenti no
     */
    public ExerciseSettings(String name, int reps, long rec, int repetition, boolean isReps, boolean hasRecs) {
        this.name=name;
        this.reps = reps;
        this.rec = rec;
        this.repetition = repetition;
        this.isReps = isReps;
        this.hasRecs = hasRecs;
        this.type= CircuitType.EXERCISE;
    }

    protected ExerciseSettings(Parcel in) {
        reps = in.readInt();
        repetition = in.readInt();
        rec = in.readLong();
        isReps = in.readByte() != 0;
        hasRecs = in.readByte() != 0;
        name = in.readString();
        type = CircuitType.valueOf(in.readString());
    }

    public static final Creator<ExerciseSettings> CREATOR = new Creator<ExerciseSettings>() {
        @Override
        public ExerciseSettings createFromParcel(Parcel in) {
            return new ExerciseSettings(in);
        }

        @Override
        public ExerciseSettings[] newArray(int size) {
            return new ExerciseSettings[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public long getRec() {
        return rec;
    }

    public void setRec(long rec) {
        this.rec = rec;
    }

    public int getRepetition() {
        return Math.max(repetition, 1);
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    public boolean isReps() {
        return isReps;
    }

    public void setReps(boolean reps) {
        isReps = reps;
    }

    public boolean isHasRecs() {
        return hasRecs;
    }

    public void setHasRecs(boolean hasRecs) {
        this.hasRecs = hasRecs;
    }

    public CircuitType getType() {
        return type;
    }

    protected void setType(CircuitType type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Permentte di rendere l'oggetto serializzabile
     * @param dest Destinazione della serializzazuibe
     * @param flags intero
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(reps);
        dest.writeInt(repetition);
        dest.writeLong(rec);
        dest.writeByte((byte) (isReps ? 1 : 0));
        dest.writeByte((byte) (hasRecs ? 1 : 0));
        dest.writeString(name);
        dest.writeString(type.name());
    }

    /**
     * Ritorna una copia dell'esercizio passato in input
     * @param e esercizio da copiare
     * @return Una copia ExerciseSettings
     */
    public static ExerciseSettings copyOf(ExerciseSettings e) {
        return new ExerciseSettings(e.name, e.reps, e.rec, e.repetition, e.isReps, e.hasRecs);
    }
}
