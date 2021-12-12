package com.example.tpocr.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tpocr.R;

public class HomeActivity extends AppCompatActivity {

    private Button normalGameBtn, timedGameBtn, scoreBtn;
    private TextView greetingTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


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
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });

        timedGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });

        scoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
                startActivity(intent);
            }
        });


    }
}