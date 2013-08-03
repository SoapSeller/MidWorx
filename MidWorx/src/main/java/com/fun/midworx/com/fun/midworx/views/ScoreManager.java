package com.fun.midworx.com.fun.midworx.views;

/**
 * Created by Rotem on 03/08/13.
 */
public class ScoreManager {


    private int mSessionScore;

    public ScoreManager() {
        mSessionScore = 0;
    }

    public Object getSessionScore() {
        return mSessionScore;
    }

    public int guessedWord(String word, int gameNumber) {
        int earnedPoints = calculateWordScore(word, gameNumber);
        mSessionScore += earnedPoints;
        return earnedPoints;
    }

    private int calculateWordScore(String word, int gameNumber) {
        return word.length() * gameNumber;
    }
}
