package com.italianswapp.yourtraining;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.lang.*;

import static java.lang.System.exit;

public class HelpActivity extends AppCompatActivity {

    private DatabaseReference myRef;
    private final String
            PLAY_STORE_ADDRESS ="https://play.google.com/store/apps/details?id=com.italianswapp.yourtraining";

    private ImageButton mBugButton, mReviewButton, mExitButton, mShareButton;
    private TextView mVersionTextView;

    AlertDialog.Builder builder;
    LayoutInflater inflater;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        mBugButton = findViewById(R.id.bugButtonHelpActivity);
        mReviewButton = findViewById(R.id.reviewButtonHelpActivity);
        mExitButton = findViewById(R.id.exitButtonHelpActivity);
        mShareButton = findViewById(R.id.shareButtonHelpActivity);
        mVersionTextView = findViewById(R.id.versionTextViewHelpActivity);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        mVersionTextView.setText(getResources().getString(R.string.version) + ": " + BuildConfig.VERSION_NAME);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        if (height<480 || width < 800) {
            TextView first = findViewById(R.id.textView5);
            TextView second = findViewById(R.id.textView6);
            TextView third = findViewById(R.id.textView7);

            first.setVisibility(TextView.GONE);
            second.setVisibility(TextView.GONE);
            third.setVisibility(TextView.GONE);
            mVersionTextView.setVisibility(TextView.GONE);
        }

        /*
        Connetto al database
        Serve nel caso in cui l'utente voglia inviare un bug
         */
        myRef = FirebaseDatabase.getInstance().getReference();

        mBugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View thisView = v;
                builder = new AlertDialog.Builder(v.getContext());
                inflater = getLayoutInflater();

                final View dialogView = inflater.inflate(R.layout.bug_dialog, null);
                builder.setView(dialogView);

                final EditText mBugEditText = dialogView.findViewById(R.id.bugEditText);
                final TextInputLayout mBugTextInputLayout = dialogView.findViewById(R.id.bugTextInput);
                builder.setTitle(getResources().getString(R.string.report_bug));

                 // Set up the buttons
                builder.setPositiveButton(getResources().getString(R.string.send), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mBugEditText.getText().toString().isEmpty()) {
                            Snackbar.make(thisView, getResources().getString(R.string.add_error), Snackbar.LENGTH_SHORT).show();
                        }
                        else {//Invia l'errore al database
                            myRef.child(System.currentTimeMillis() + "").setValue(mBugEditText.getText().toString());
                            Snackbar.make(thisView, getResources().getString(R.string.received_bug) , Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                mBugEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus) {
                            if(mBugEditText.getText().toString().isEmpty()) {
                                mBugTextInputLayout.setErrorEnabled(true);
                                mBugTextInputLayout.setError(getResources().getString(R.string.enter_error));
                            }
                            else
                                mBugTextInputLayout.setErrorEnabled(false);
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        mReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(PLAY_STORE_ADDRESS));
                startActivity(viewIntent);
            }
        });

        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit(0);
            }
        });

        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_text) + PLAY_STORE_ADDRESS);
                startActivity(sendIntent);
            }
        });
    }
}
