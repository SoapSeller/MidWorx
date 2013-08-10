package com.fun.midworx.views;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rotem on 03/08/13.
 */
public class Scoring {

	public interface OnScoreChange {
		public void updateScore(int guessScore, int totalScore);
	}

	private List<OnScoreChange> listeners = new ArrayList<OnScoreChange>();

	private int lastLevelBonus = 0;
    private int mSessionScore = 0;

    public int getSessionScore() {
        return mSessionScore;
    }

    public void wordGuessed(String word, int gameNumber) {
		int scoreEarned = calculateWordScore(word, gameNumber);
		mSessionScore = mSessionScore + scoreEarned;

    }

	private void notifyListeners(int guessScore, int totalSctore){
		for (OnScoreChange listener : listeners) {
			listener.updateScore(guessScore,totalSctore);
		}
	}

    private int calculateWordScore(String word, int gameNumber) {
        if (word.length() == 6){
			if (lastLevelBonus < gameNumber){
				lastLevelBonus++;
				return word.length() + lastLevelBonus;
			} else {
				return word.length();
			}
		} else {
			return word.length();
		}
    }

	public void registerOnScoreChange(OnScoreChange listener){
		if (listeners.indexOf(listener) == -1){
			listeners.add(listener);
		}
	}

	public void unregisterOnScoreChange(OnScoreChange listener){
		this.listeners.remove(listener);
	}
}
