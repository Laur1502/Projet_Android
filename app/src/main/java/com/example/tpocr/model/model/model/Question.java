package com.example.tpocr.model.model.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable  {
    private final String mQuestion;
    private final List<String> mChoiceList;
    private final int mAnswerIndex;
    private final String mWikiLink;

    public Question(String question, List<String> choiceList, int answerIndex, String wikiLink) {
        mQuestion = question;
        mChoiceList = choiceList;
        mAnswerIndex = answerIndex;
        mWikiLink = wikiLink;
    }

    public String getWikiLink() {
        return mWikiLink;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public List<String> getChoiceList() {
        return mChoiceList;
    }

    public int getAnswerIndex() {
        return mAnswerIndex;
    }

    protected Question(Parcel in) {
        mQuestion = in.readString();
        mChoiceList = in.createStringArrayList();
        mAnswerIndex = in.readInt();
        mWikiLink = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mQuestion);
        out.writeList(mChoiceList);
        out.writeInt(mAnswerIndex);
        out.writeString(mWikiLink);
    }

}
