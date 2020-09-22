package com.italianswapp.yourtraining3;

import android.util.Log;

public class ExerciseTypeNotCorrectException extends Exception {
    public ExerciseTypeNotCorrectException(String str) {
        Log.d("CustomException", str);

    }
}
