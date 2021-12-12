package com.example.tpocr.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tpocr.model.model.model.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AndroidQuizz.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseManager(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // On crée les CREATE TABLE dans des variables que l'on va exécuter dans le onCreate

    private static final String CREATE_TABLE_PLAYER = "CREATE TABLE Player ("
            + "   idUser integer PRIMARY KEY autoincrement,"
            + "   name text NOT NULL,"
            + "   password text NOT NULL,"
            + "   dateCreation datetime default current_timestamp,"
            + "   accessibilityOn integer DEFAULT 0 NOT NULL CHECK (accessibilityOn IN (0,1)),"
            + "   wikiOn integer DEFAULT 0 NOT NULL CHECK (wikiOn IN (0,1)))";

    private static final String CREATE_TABLE_QUESTIONBANK = "CREATE TABLE QuestionBank ("
            + " idQuestion integer PRIMARY KEY autoincrement,"
            + " libelle text NOT NULL,"
            + " answer1 text NOT NULL,"
            + " answer2 text NOT NULL,"
            + " answer3 text NOT NULL,"
            + " answer4 text NOT NULL,"
            + " answerIndex int NOT NULL,"
            + " wikiLink text)";

    private static final String CREATE_TABLE_GAME = "CREATE TABLE Game ("
            + "idGame integer PRIMARY KEY autoincrement,"
            + "namePlayer text NOT NULL,"
            + "modeJeu text NOT NULL,"
            + "score int NOT NULL,"
            + "dateJeu datetime default current_timestamp)";



    @Override
    public void onCreate(SQLiteDatabase db) {

        //Exécute les create table
        db.execSQL(CREATE_TABLE_PLAYER);
        db.execSQL(CREATE_TABLE_QUESTIONBANK);
        db.execSQL(CREATE_TABLE_GAME);

        // Ajoute les questions en BDD
        fillQuestionsTable(db);

        //vérifie que la commande est bien appelée une seule fois grâce à un log info
        Log.i("DATABASE", "ONCREATE invoked");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Player");
        db.execSQL("drop table if exists QuestionBank");

    }



    //---------------------------------------------------------------------------- PARTIE PLAYER ----------------------------------------------------------------------------

    //    public void insertPlayer(String name){
//        name = name.replace("'", "''");
//        String strSql = "INSERT INTO Player(name, dateCreation, accessibilityOn, wikiOn) VALUES ('"
//                      + name + "', " + new Date().getTime() + ", 0, 0)";
//        this.getWritableDatabase().execSQL(strSql);
//        Log.i("DATABASE", "INSERT invoked");
//    }
    public Boolean insertPlayer(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", username);
        contentValues.put("password", password);
        long result = db.insert("Player", null, contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Boolean checkUsername(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Player WHERE name = ?", new String[] {username});
        if(cursor.getCount()>0){
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean checkUsernamepassword(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Player WHERE name = ? and password = ?", new String[] {username, password});
        if(cursor.getCount()>0){
            return true;
        }
        else{
            return false;
        }
    }

    // ----------------------------------------------------------------------- PARTIE GAME ----------------------------------------------------------------------------

    public void insertGame(String namePlayer, String modeJeu, int score){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("namePlayer", namePlayer);
        contentValues.put("modeJeu", modeJeu);
        contentValues.put("score", score);
        db.insert("Game", null, contentValues);

    }

    // ----------------------------------------------------------------------- PARTIE QUESTION BANK ----------------------------------------------------------------------------
    private void fillQuestionsTable(SQLiteDatabase db){
        Question question1 = new Question(
                "Who is the creator of Android?",
                Arrays.asList(
                        "Andy Rubin",
                        "Steve Wozniak",
                        "Jake Wharton",
                        "Paul Smith"
                ),
                0,
                "https://fr.wikipedia.org/wiki/Android");

        Question question2 = new Question(
                "When did the first man land on the moon?",
                Arrays.asList(
                        "1958",
                        "1962",
                        "1967",
                        "1969"
                ),
                3,
                "https://fr.wikipedia.org/wiki/Neil_Armstrong");

        Question question3 = new Question(
                "What is the house number of The Simpsons?",
                Arrays.asList(
                        "42",
                        "101",
                        "666",
                        "742"
                ),
                3,
                "https://fr.wikipedia.org/wiki/Les_Simpson");

        addQuestion(question1, db);
        addQuestion(question2, db);
        addQuestion(question3, db);
    }

    private void addQuestion(Question question, SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put("libelle", question.getQuestion());
        cv.put("answer1", question.getChoiceList().get(0));
        cv.put("answer2", question.getChoiceList().get(1));
        cv.put("answer3", question.getChoiceList().get(2));
        cv.put("answer4", question.getChoiceList().get(3));
        cv.put("answerIndex", question.getAnswerIndex());
        cv.put("wikiLink", question.getWikiLink());
        db.insert("QuestionBank", null, cv);

    }

//    public List<Question> getAllQuestions(){
//        List<Question> questionList = new ArrayList<>();
//        SQLiteDatabase db = this.getWritableDatabase();
//        db = getReadableDatabase();
//        Cursor c = db.rawQuery("SELECT * FROM QuestionBank", null);
//
//        if(c.moveToFirst()){
//            do{
//                Question question = new Question();
//            }while(c.moveToNext());
//        }
//    }
}
