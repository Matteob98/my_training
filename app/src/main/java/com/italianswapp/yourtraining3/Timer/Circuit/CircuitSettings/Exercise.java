package com.italianswapp.yourtraining3.Timer.Circuit.CircuitSettings;

import android.os.Parcel;
import android.os.Parcelable;

import com.italianswapp.yourtraining3.ExerciseTypeNotCorrectException;

/**
 * Classe che consente di gestire i dati delle card per la creazione di
 * esercizi all'interno del circuito
 */
public class Exercise implements Parcelable {

    private int reps, repetition, numberSets, totalSets;
    private long rec;
    private boolean isReps, hasRecs, hasSets;
    private String name;

    private SupersetExercise supersetExercise;

    public enum CircuitType {
        EXERCISE,
        TABATA,
        REST,
        EMOM,
        SUPERSET
    }

    private CircuitType type;

    public Exercise() {
        reps = 1;
        repetition = 1;
        numberSets = 1;
        totalSets = 1;
        rec = 0;
        isReps = false;
        hasRecs = false;
        hasSets = false;
        name = "";
        type = CircuitType.EXERCISE;

    }

    /**
     * Costruttore della classe
     * @param reps Numero di ripetizioni o numero di secondi di lavoro
     * @param rec Secondi di recupero
     * @param repetition Numero di volte che va ripetuto il round
     * @param isReps se vero è un esercizio a ripetizioni, altrimenti è un esercizio a tempo
     * @param hasRecs se vero dopo l'esercizio c'è recupero, altrimenti no
     */
    public Exercise(String name, int reps, long rec, int repetition, boolean isReps, boolean hasRecs, CircuitType type) {
        this.name=name;
        this.reps = reps;
        this.rec = rec;
        this.repetition = repetition;
        this.isReps = isReps;
        this.hasRecs = hasRecs;
        this.type= type;
        numberSets=0;
        totalSets=0;
        hasSets=false;

        /*
        Se è un esercizio in superserie inizializza il secondo esercizio
         */
        supersetExercise = new SupersetExercise(0, false, "");
    }


    protected Exercise(Parcel in) {
        reps = in.readInt();
        repetition = in.readInt();
        rec = in.readLong();
        isReps = in.readByte() != 0;
        hasRecs = in.readByte() != 0;
        name = in.readString();
        type = CircuitType.valueOf(in.readString());
        supersetExercise = new SupersetExercise(
                in.readInt(),
                in.readByte() != 0,
                in.readString());
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    public int getNumberSets() {
        return numberSets;
    }

    public void setNumberSets(int numberSets) {
        this.numberSets = numberSets;
    }

    public int getTotalSets() {
        return totalSets;
    }

    public void setTotalSets(int totalSets) {
        this.totalSets = totalSets;
    }

    public boolean isHasSets() {
        return hasSets;
    }

    public void setHasSets(boolean hasSets) {
        this.hasSets = hasSets;
    }

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

    /**
     * Ritorna l'esercizio in superserie
     * @return L'esercizio in superserie
     * @throws ExerciseTypeNotCorrectException Se l'esercizio non è di tipo superserie
     */
    public SupersetExercise getSupersetExercise() throws ExerciseTypeNotCorrectException {
        if (! (this.type.equals(CircuitType.SUPERSET))) {
            //Non lancia questa eccezione
            throw new ExerciseTypeNotCorrectException("Tipo non corretto");
        }

        return supersetExercise;
    }

    /**
     * Imposta l'esercizio in superserie
     * Se l'esercizio non è una superserie lo imposta in questo metodo
     * @param reps Numero o tempo di ripetizione
     * @param isReps Se è un esercizio a ripetizioni
     * @param name Nome dell'esercizio in superserie
     */
    public void setSupersetExercise(int reps, boolean isReps, String name) {
        this.type=CircuitType.SUPERSET;
        supersetExercise.setReps(reps);
        supersetExercise.setIsReps(isReps);
        supersetExercise.setName(name);
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
        dest.writeInt(supersetExercise.getReps());
        dest.writeByte((byte) (supersetExercise.isReps() ? 1: 0));
        dest.writeString(supersetExercise.getName());

    }


    /**
     * Ritorna una copia dell'esercizio passato in input
     * @param e esercizio da copiare
     * @return Una copia Exercise
     */
    public static Exercise copyOf(Exercise e) {
        Exercise copy = new Exercise(e.name, e.reps, e.rec, e.repetition, e.isReps, e.hasRecs, e.type);
        copy.setNumberSets(e.getNumberSets());
        copy.setTotalSets(e.getTotalSets());
        copy.setHasSets(e.isHasSets());

        return copy;
    }

    /**
     * Ritorna un intero in base al tipo di esercizio passato in input
     * Exercise = 0
     * Emom = 1
     * Rest = 2
     * Tabata = 3
     * Superset = 4
     * @param type Tipo dell'esercizio
     * @return Un intero che rappresenta il tipo
     * @throws ExerciseTypeNotCorrectException Se viene passato un tipo non gestito dalla funzione
     */
    public static int getIntegerItemType(Exercise.CircuitType type) throws ExerciseTypeNotCorrectException {
        switch (type) {
            case EXERCISE:
                return 0;
            case EMOM:
                return 1;
            case REST:
                return 2;
            case TABATA:
                return 3;
            case SUPERSET:
                return 4;
            default:
                throw new ExerciseTypeNotCorrectException("Tipo non corretto in getIntegerItemType");
        }
    }

    /**
     * Esercizio in superserie con l'esercizio principale
     * Contiene solo il nome e il numero/tempo di ripetizioni
     * Il recupero è sempre quello dell'esercizio principale
     */
    public static class SupersetExercise {
        private int supersetReps;
        private boolean supersetIsReps;
        private String supersetName;

        /**
         * Costruttore privato in modo che possa essere chiamato solo dalla classe madre
         * @param supersetReps
         * @param supersetIsReps
         * @param supersetName
         */
        public SupersetExercise(int supersetReps, boolean supersetIsReps, String supersetName) {
            this.supersetReps = supersetReps;
            this.supersetIsReps = supersetIsReps;
            this.supersetName = supersetName;
        }

        public int getReps() {
            return supersetReps;
        }

        public void setReps(int supersetReps) {
            this.supersetReps = supersetReps;
        }

        public boolean isReps() {
            return supersetIsReps;
        }

        public void setIsReps(boolean supersetIsReps) {
            this.supersetIsReps = supersetIsReps;
        }

        public String getName() {
            return supersetName;
        }

        public void setName(String supersetName) {
            this.supersetName = supersetName;
        }
    }
}
