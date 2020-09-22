package com.italianswapp.yourtraining3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.lang.*;

import static java.lang.System.exit;

//todo al 17/09 helpFragment non viene più usato, controllare se viene usato. sennò cancellarlo
public class HelpActivity extends AppCompatActivity {
    private DatabaseReference myRef;
    private final String
            PLAY_STORE_ADDRESS ="https://play.google.com/store/apps/details?id=com.italianswapp.yourtraining";

    private CardView mBugCard, mReviewCard, mExitCard, mShareCard;
    private TextView mVersionTextView;

    AlertDialog.Builder builder;
    LayoutInflater inflater;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        /*
        Importa gli oggetti dall'xml
         */
        layoutSettings();

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        mVersionTextView.setText(getResources().getString(R.string.version) + ": " + BuildConfig.VERSION_NAME);

        /*
        Connetto al database
        Serve nel caso in cui l'utente voglia inviare un bug
         */
        myRef = FirebaseDatabase.getInstance().getReference();

        /*
        Imposta i comportamenti dei vari pulsanti
         */
        onClickBugButton();
        onClickReviewButton();
        onClickExitButton();
        onClickShareButton();

    }

    /**
     * Imposta il comportamento del pulsante condividi
     */
    private void onClickShareButton() {
        mShareCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, PLAY_STORE_ADDRESS + '\n' + getResources().getString(R.string.share_text) );
                startActivity(sendIntent);
            }
        });
    }

    /**
     * Imposta il comportamento del pulsante esci
     */
    private void onClickExitButton() {
        mExitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit(0);
            }
        });
    }

    /**
     * Imposta il comportamento del pulsante recensione
     */
    private void onClickReviewButton() {
        mReviewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(PLAY_STORE_ADDRESS));
                startActivity(viewIntent);
            }
        });
    }

    /**
     * Imposta il comportamento del pulsante per i bug
     */
    private void onClickBugButton() {
        mBugCard.setOnClickListener(new View.OnClickListener() {
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
    }

    /**
     * Importa tutti gli oggetti dall'xml
     */
    private void layoutSettings() {
        mBugCard = findViewById(R.id.bugCardHelpActivity);
        mReviewCard = findViewById(R.id.reviewCardHelpActivity);
        mExitCard = findViewById(R.id.exitCardHelpActivity);
        mShareCard = findViewById(R.id.shareCardHelpActivity);
        mVersionTextView = findViewById(R.id.versionTextViewHelpActivity);
    }
}
