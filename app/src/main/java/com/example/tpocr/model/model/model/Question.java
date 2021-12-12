package com.example.tpocr.model.model.model;

import java.util.List;

public class Question {
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
}
