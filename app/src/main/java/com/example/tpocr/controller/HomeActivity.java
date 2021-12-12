package com.example.tpocr.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tpocr.R;

public class HomeActivity extends AppCompatActivity {

    private Button normalGameBtn, timedGameBtn, scoreBtn;
    private TextView greetingTV;
    private String namePlayer;
    private static final int GAME_ACTIVITY_REQUEST_CODE = 42;

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
        setContentView(R.layout.activity_home);

        // On récupère le nom du joueur et on l'affiche
        Intent intent = getIntent();
        if(intent.hasExtra("UserName")){
            namePlayer = intent.getStringExtra("UserName");
        }
        TextView textView = (TextView) findViewById(R.id.home_textview_greeting);
        textView.setText("Welcome " + namePlayer + " !");
//        Log.d("TEST","BLABLAAAAAAA " + user);


        SharedPreferences userDetails = getSharedPreferences("userdetails", MODE_PRIVATE);
        String username = userDetails.getString("username","");
        normalGameBtn = (Button) findViewById(R.id.normal_mode);
        timedGameBtn = (Button) findViewById(R.id.timer_mode);
        scoreBtn = (Button) findViewById(R.id.scores);
        greetingTV = findViewById(R.id.home_textview_greeting);
        greetingTV.setText(greetingTV.getText() + " "+ username);
        normalGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = userDetails.edit();
                edit.putInt("gamemode",0);
                edit.apply();
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("UserName", namePlayer);
                intent.putExtra("GameMode", "NormalMode");
                startActivity(intent);
            }
        });

        timedGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = userDetails.edit();
                edit.putInt("gamemode",1);
                edit.apply();
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("UserName", namePlayer);
                intent.putExtra("GameMode", "TimedMode");
                startActivity(intent);
            }
        });

        scoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
                intent.putExtra("UserName", namePlayer);
                startActivity(intent);
            }
        });


    }
}