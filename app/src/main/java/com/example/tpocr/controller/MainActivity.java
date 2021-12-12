package com.example.tpocr.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;

import com.example.tpocr.R;
import com.example.tpocr.model.model.model.User;

public class MainActivity extends AppCompatActivity {

    private TextView mGreetingTextView;
    private EditText mNameEditText;
    private Button mPlayButton;
    private User mUser;
    private static final int GAME_ACTIVITY_REQUEST_CODE = 42;
    private DatabaseManager databaseManager;
    private MediaPlayer mediaPlayer;
    private Button mSoundPlayButton;



    // sauvegarde les données choisies dans un fichier XML (ici le fichier est "SHARED_PREF_USER_INFO")
    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO";
    private static final String SHARED_PREF_USER_INFO_NAME = "SHARED_PREF_USER_INFO_NAME";

    //Récupère le score de gameActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // Fetch the score from the Intent
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mGreetingTextView = findViewById(R.id.main_textview_greeting);
        mNameEditText = findViewById(R.id.main_edittext_name);
        mPlayButton = findViewById(R.id.main_button_play);

        this.mediaPlayer= MediaPlayer.create(getApplicationContext(),R.raw.sound);
        mSoundPlayButton = findViewById(R.id.sound_button_play);

        mSoundPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying())
                {
                    pauseSound();
                    //mSoundPlayButton.setBackgroundResource(R.drawable.play);
                }

                else
                {
                    playSound();
                    //mSoundPlayButton.setBackgroundResource(R.drawable.pause);
                }
            }
        });

        mPlayButton.setEnabled(false);

        String firstName = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getString(SHARED_PREF_USER_INFO_NAME, null);

        databaseManager = new DatabaseManager(this);
//        databaseManager.insertPlayer("Elodie");
//        databaseManager.insertPlayer("Diyé");
//        databaseManager.insertPlayer("Kenza");
        databaseManager.insertPlayer("LAURIIIINE");

        databaseManager.close();

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPlayButton.setEnabled(!s.toString().isEmpty());
            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                databaseManager.insertPlayer(mNameEditText.getText().toString());
                databaseManager.close();

                //stock le nom de l'user dans le fichier XML
                getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                        .edit()
                        .putString(SHARED_PREF_USER_INFO_NAME,mNameEditText.getText().toString())
                        .apply();

                Intent gameActivityIntent = new Intent(MainActivity.this, GameActivity.class);
                startActivityForResult(gameActivityIntent, GAME_ACTIVITY_REQUEST_CODE);


            }
        });

        //initialisation du prénom du joueur
        //mUser = findViewById(R.id.main_edittext_name);

//        mUser.setFirstName(mNameEditText.getText().toString());

    }

    public void playSound() {

        mediaPlayer.start();

    }

    public void pauseSound() {

        mediaPlayer.stop();

    }
}