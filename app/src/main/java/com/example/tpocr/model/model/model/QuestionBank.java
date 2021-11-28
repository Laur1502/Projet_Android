package com.example.tpocr.model.model.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.tpocr.model.model.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionBank implements Parcelable {

    private List<Question> mQuestionList;
    private int mQuestionIndex;

    public QuestionBank(List<Question> questionList) {
        mQuestionList = questionList;
        mQuestionIndex=0;
        Collections.shuffle(mQuestionList);
    }

    public Question getCurrentQuestion() {
        return mQuestionList.get(mQuestionIndex);
    }

    public Question getNextQuestion() {
        mQuestionIndex++;
        return getCurrentQuestion();
    }

    public void setCurrentIndex(int newIndex) {
        mQuestionIndex = newIndex;
    }

    public  int getCurrentIndex(){
        return mQuestionIndex;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeList(mQuestionList);
        out.writeInt(mQuestionIndex);
    }

    public static final Parcelable.Creator<QuestionBank> CREATOR
            = new Parcelable.Creator<QuestionBank>() {
        public QuestionBank createFromParcel(Parcel in) {
            return new QuestionBank(in);
        }

        public QuestionBank[] newArray(int size) {
            return new QuestionBank[size];
        }
    };

    private QuestionBank(Parcel in) {
        mQuestionList = new ArrayList<Question>();
        in.readList(mQuestionList, Question.class.getClassLoader());
        mQuestionIndex = in.readInt();
    }
}
