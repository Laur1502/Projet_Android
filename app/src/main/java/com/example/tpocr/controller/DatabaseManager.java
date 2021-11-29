package com.example.tpocr.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Date;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AndroidQuizz.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseManager(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String strSQl = "CREATE TABLE Player ("
                      + "   idUser integer PRIMARY KEY autoincrement,"
                      + "   name text NOT NULL,"
                      + "   dateCreation integer NOT NULL,"
                      + "   accessibilityOn integer NOT NULL CHECK (accessibilityOn IN (0,1)),"
                      + "   wikiOn integer NOT NULL CHECK (wikiOn IN (0,1)))";
        db.execSQL(strSQl);

        //vérifie que la commande est bien appelée une seule fois grâce à un log info
        Log.i("DATABASE", "ONCREATE invoked");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertPlayer(String name){
        name = name.replace("'", "''");
        String strSql = "INSERT INTO Player(name, dateCreation, accessibilityOn, wikiOn) VALUES ('"
                      + name + "', " + new Date().getTime() + ", 0, 0)";
        this.getWritableDatabase().execSQL(strSql);
        Log.i("DATABASE", "INSERT invoked");
    }
}
