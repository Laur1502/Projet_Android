package com.example.tpocr.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;

import com.example.tpocr.R;
import com.example.tpocr.model.model.model.User;

public class MainActivity extends AppCompatActivity {

    private EditText username, password, repassword;
    private Button signup, signin;

    private static final int GAME_ACTIVITY_REQUEST_CODE = 42;
    private DatabaseManager databaseManager;
    private MediaPlayer mediaPlayer;
    private Button mSoundPlayButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        signup = (Button) findViewById(R.id.btnsignup);
        signin = (Button) findViewById(R.id.btnsignin);

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

       
        databaseManager = new DatabaseManager(this);

        databaseManager.close();

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                mPlayButton.setEnabled(!s.toString().isEmpty());
            }
        });

//        mPlayButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//
////                databaseManager.insertPlayer(username.getText().toString());
////                databaseManager.close();
//
//
//                Intent gameActivityIntent = new Intent(MainActivity.this, GameActivity.class);
//                startActivityForResult(gameActivityIntent, GAME_ACTIVITY_REQUEST_CODE);
//
//
//            }
//        });

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                if(user.equals("") || pass.equals("") || repass.equals("")){
                    Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser = databaseManager.checkUsername(user);
                        if(checkuser == false){
                            Boolean insert = databaseManager.insertPlayer(user, pass);
                            if(insert == true){
                                SharedPreferences userDetails = getSharedPreferences("userdetails", MODE_PRIVATE);
                                SharedPreferences.Editor edit = userDetails.edit();
                                edit.putString("username", user);
                                edit.apply();
                                Toast.makeText(MainActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.putExtra("UserName", username.getText().toString());
                                startActivity(intent);
                            }else{
                                Toast.makeText(MainActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.this, "User already exists ! Please sign in", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        signin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }
        });


    }

    public void playSound() {

        mediaPlayer.start();

    }

    public void pauseSound() {

        mediaPlayer.stop();

    }
}