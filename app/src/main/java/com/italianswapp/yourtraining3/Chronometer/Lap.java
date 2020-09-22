package com.italianswapp.yourtraining3.Chronometer;

import com.italianswapp.yourtraining3.Utilities;

/**
 * Lap del cronometro
 * Rappresentato dal numero del lap
 * Dal tempo totale dall'inizio del cronometro
 * E il tempo del lap corrente
 */
public class Lap {

    private int numberLap;
    private long totalTime, lapTime;

    public Lap(int numberLap, long totalTime, long lapTime) {
        this.numberLap = numberLap;
        this.totalTime = totalTime;
        this.lapTime = lapTime;
    }

    /**
     * Ritorna una stringa in formato 00:00:00 rappresentate il tempo totale trascorso
     * In formato hh:mm:ss se è superiore ad un ora
     * MM:ss:mm altrimenti
     * @return le ore
     */
    public String getStringTotalTime() {
        if (Utilities.getHoursFromMills(totalTime) > 0)
            return Utilities.getStringTimeFromMills(totalTime);
        return  Utilities.getStringTimeWithMillsFromMills(totalTime);
    }

    /**
     * Ritorna una stringa in formato 00:00:00 rappresentante il tempo di questo lap
     * In formato hh:mm:ss se è superiore ad un ora
     * MM:ss:mm altrimenti
     * @return i minuti con i millisecondi
     */
    public String getStringLapTime() {
        if (Utilities.getHoursFromMills(lapTime) > 0)
            return  Utilities.getStringTimeFromMills(lapTime);
        return Utilities.getStringTimeWithMillsFromMills(lapTime);
    }

    public int getNumberLap() {
        return numberLap;
    }

    public void setNumberLap(int numberLap) {
        this.numberLap = numberLap;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getLapTime() {
        return lapTime;
    }

    public void setLapTime(long lapTime) {
        this.lapTime = lapTime;
    }
}
