package com.italianswapp.yourtraining3.Utilities;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.italianswapp.yourtraining3.ExerciseTypeNotCorrectException;
import com.italianswapp.yourtraining3.OfflineDatabase.WorkoutSaved;
import com.italianswapp.yourtraining3.R;
import com.italianswapp.yourtraining3.Timer.Circuit.CircuitSettings.Exercise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utilities
{

    public static final String[] TIME_IN_STRING =  {"00:05", "00:10","00:15", "00:20", "00:25", "00:30", "00:35", "00:40",
            "00:45", "00:50", "00:55", "01:00", "01:05", "01:10", "01:15", "01:20", "01:30", "01:45" ,"02:00", "02:15", "02:30", "02:45",
            "03:00", "03:30", "04:00",  "04:30", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "20:00", "30:00", "40:00", "45:00", "50:00", };

    public static final String[] TIME_IN_STRING_WITH_0 =  {"00:00", "00:05", "00:10","00:15", "00:20", "00:25", "00:30", "00:35", "00:40",
            "00:45", "00:50", "00:55", "01:00", "01:05", "01:10", "01:15", "01:20", "01:30", "01:45" ,"02:00", "02:15", "02:30", "02:45",
            "03:00", "03:30", "04:00",  "04:30", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "20:00", "30:00", "40:00", "45:00", "50:00", };


    /**
     * Ritorna una stringa in formato hh:mm:ss da un input in millisecondi
     * @param mills millisecondi
     * @return hh:mm:ss
     */
    public static String getStringTimeFromMills(long mills) {

        return getStringTimeFromSecs((int) mills/1000);
    }

    /**
     * Ritorna una stringa in formato mm:ss da un input in millisecondi
     * @param mills millisecondi
     * @return mm:ss
     */
    public static String getStringTimeNoHour(long mills) {

        int secs = (int) (mills/1000);
        int mins = secs / 60;
        mins %= 60;
        secs %= 60;

        String ss;
        if(secs<10)
            ss="0"+secs;
        else
            ss=""+secs;

        String mm;
        if(mins<10)
            mm="0"+mins;
        else
            mm=""+mins;

        return  mm + ":" + ss;
    }

    /**
     * Ritorna una stringa in formato mm:ss:ms da un input in millisecondi
     * @param mills millisecondi
     * @return mm:ss:ms
     */
    public static String getStringTimeWithMillsFromMills(long mills) {
        int secs = (int) (mills/1000);
        int mins = secs / 60;
        mins %= 60;
        secs %= 60;
        int decaseconds = (int) ( (mills /10) % 100 );

        String dsec;
        if(decaseconds<10)
            dsec="0"+decaseconds;
        else
            dsec=""+decaseconds;
        String ss;
        if(secs<10)
            ss="0"+secs;
        else
            ss=""+secs;

        String mm;
        if(mins<10)
            mm="0"+mins;
        else
            mm=""+mins;

        return  mm + ":" + ss + ":" + dsec;
    }

    /**
     * Ritorna una stringa in formato ss:ms da un input in millisecondi
     * @param mills millisecondi
     * @return ss:ms
     */
    public static String getStringTimeWithMillsWithoutMinuteFromMills(long mills) {
        int secs = (int) (mills/1000);
        secs %= 60;
        int decaseconds = (int) ( (mills /10) % 100 );

        String dsec;
        if(decaseconds<10)
            dsec="0"+decaseconds;
        else
            dsec=""+decaseconds;
        String ss;
        if(secs<10)
            ss="0"+secs;
        else
            ss=""+secs;


        return  ss + ":" + dsec;
    }

    /**
     * Ritorna una stringa in formato hh:mm:ss da un input in secondi
     * @param secs secondi
     * @return Stringa in formato hh:mm:ss
     */
    public static String getStringTimeFromSecs(int secs) {
        int mins = secs / 60;
        int hour = mins / 60;
        mins %= 60;
        secs %= 60;

        String ss;
        if(secs<10)
            ss="0"+secs;
        else
            ss=""+secs;

        String mm;
        if(mins<10)
            mm="0"+mins;
        else
            mm=""+mins;

        String hh;

        if(hour<10)
            hh="0"+hour;
        else
            hh=""+hour;

        return hh + ":" + mm + ":" + ss;
    }

    /**
     * Dati i millisecondi ritorna i secondi
     * @param mills millisecondi da convertire
     * @return secondi
     */
    public static int getSecondsFromMills(long mills) {
        return (int) mills/1000;
    }

    /**
     * Dati i millisecondi ritorna i minuti
     * @param mills millisecondi da convertire
     * @return minuti
     */
    public static int getMinutesFromMills(long mills) {
        return (int) mills/60000;
    }

    /**
     * Dati i millisecondi ritorna le ore
     * @param mills
     * @return ore
     */
    public static int getHoursFromMills(long mills) {
        return (int) mills/3600000;
    }

    /**
     * Prende in input una stringa dei minuti nella forma 00:00 e restituisce i millesimi
     * @param minute Stringa in formato 00:00
     * @return millesimi di secondi
     */
    public static long getMillsFromMinuteString(String minute) {

        int minutes = Integer.parseInt(minute.substring(0,2));
        int seconds = Integer.parseInt(minute.substring(3));

        return  ((minutes*60) + seconds ) * 1000;

    }

    /**
     * Data una stringa hh:mm:ss ritorna i millisecondi
     * @param hour hh:mm:ss
     * @return millisecondi
     */
    public static long getMillsFromHourString(String hour) {

        int hours = Integer.parseInt(hour.substring(0,2));
        int minutes = Integer.parseInt(hour.substring(3,5));
        int seconds = Integer.parseInt(hour.substring(6));

        return  ((hours*3600) +(minutes*60) + seconds ) * 1000;
    }

    /**
     * Cambia il colore dei divisori dei numberPicker
     */
    public static void changeDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException | Resources.NotFoundException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     *
     * @param mills
     * @return
     */
    public static String getStringTimeFromMillsWithoutHours(int mills) {
        int secs = (int) (mills/1000);
        int mins = secs / 60;
        mins %= 60;
        secs %= 60;
        int decaseconds = (int) ( (mills /10) % 100 );

        String dsec;
        if(decaseconds<10)
            dsec="0"+decaseconds;
        else
            dsec=""+decaseconds;
        String ss;
        if(secs<10)
            ss="0"+secs;
        else
            ss=""+secs;

        String mm;
        if(mins<10)
            mm="0"+mins;
        else
            mm=""+mins;

        return  mm + ":" + ss;
    }

    /**
     * Prende il tipo di un esercizio e ne ritorna il colore
     * Es. EXERCISE -> Color accent
     * @param res La resource
     * @param circuitType Il tipo dell'esercizio
     * @return Il colore
     */
    public static Drawable getColorOfExercise(Resources res, Exercise.CircuitType circuitType) {
        Drawable drawable;
        switch (circuitType) {
            case EXERCISE:
                drawable = res.getDrawable(R.color.colorAccent);
                break;
            case REST:
                drawable = res.getDrawable(R.color.restColor);
                break;
            case SUPERSET:
                drawable = res.getDrawable(R.color.supersetColor);
                break;
            case TABATA:
                drawable = res.getDrawable(R.color.tabataColor);
                break;
            case EMOM:
                drawable = res.getDrawable(R.color.emomColor);
                break;
            default:
                try {
                    throw new ExerciseTypeNotCorrectException("Tipo non corretto in getExerciseColor");
                } catch (ExerciseTypeNotCorrectException e) {
                    e.printStackTrace();
                }
                return null;
        }
        return drawable;
    }

    /**
     * Ritorna una copia dell'array passato in input
     * @param source Sorgente
     * @param <T> Tipo generico
     * @return La copia dell'array
     */
    public static <T> List<T>  arrayCopy(List<T> source) {
        return new ArrayList<T>(source);
    }
}
