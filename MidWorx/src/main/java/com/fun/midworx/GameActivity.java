package com.fun.midworx;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fun.midworx.crouton.Configuration;
import com.fun.midworx.crouton.Crouton;
import com.fun.midworx.crouton.Style;
import com.fun.midworx.views.BackgroundFun;
import com.fun.midworx.views.BoxesContainer;
import com.fun.midworx.views.LetterOrganizer;
import com.fun.midworx.views.Scoring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GameActivity extends MidWorxActivity {

	private Style croutonStyle = new Style.Builder()
			.setConfiguration(new Configuration.Builder()
					.setDuration(300)
					.build())
			.build();

	private GameTimer mGameTimer = new GameTimer();
	private BoxesContainer mBoxesContainer;
	private TextView mScoreText;
	private TextView mTimeText;
	private LetterOrganizer mLetterOrganizer;
	private BackgroundFun mBackground;
	private TextView mLevelText;

	private boolean isSixLetterWordGuessed;
	private int mGameNumber = 0;

	private Scoring mScoring = new Scoring();
	private List<String> mWords;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setupScoreListener();

        setContentView(R.layout.activity_main);

		findViewById(R.id.guess_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				guessWord(getCurrentGuess());
			}
		});

		findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startNewGame();
			}
		});

        mLetterOrganizer = new LetterOrganizer(findViewById(R.id.letters_organizer));
        mBoxesContainer = (BoxesContainer) findViewById(R.id.words_boxes_layout);
        mScoreText = (TextView) findViewById(R.id.score_txt);
        mTimeText = (TextView) findViewById(R.id.time_txt);

        mLevelText = (TextView) findViewById(R.id.level_txt);
        mBackground = (BackgroundFun) findViewById(R.id.bg);
    }


	@Override
    protected void onStart() {
        super.onStart();
        startNewGame();
    }

    private void setupScoreListener() {
		mScoring.registerOnScoreChange(new Scoring.OnScoreChange() {
			@Override
			public void updateScore(int guessScore, int totalScore) {
				GameActivity.this.updateScore(guessScore,totalScore);
			}
		});
	}

	private void startNewGame() {
        mBoxesContainer.clear();
        mGameNumber++;
        mLevelText.setText("Level: " + mGameNumber);
        mBackground.increaseSpeed();

		isSixLetterWordGuessed = false;

		initializeWords();
		initializeWordBoxes();
		initializeLetterOrganizer();
		initializeTimer();

        findViewById(R.id.next_btn).setVisibility(View.GONE);
        findViewById(R.id.guess_btn).setVisibility(View.VISIBLE);

        sendEvent("GameActivity", "startNewGame", "");
    }

	private void initializeTimer() {

		mGameTimer.start();
		mTimeText.post(new Runnable() {
			@Override
			public void run() {
				if (mGameTimer.isGameFinished()){
					mTimeText.setText("Time: " + "--");
					timesUp();
				} else {
					int timeLeft = mGameTimer.getTimeLeft();
					mTimeText.setText("Time: " + timeLeft);
					mTimeText.postDelayed(this, 1000);
				}
			}
		});
	}

	private void initializeLetterOrganizer() {
		List<String> letters = new ArrayList<String>();
		for (String mWord : mWords) {
			if (letters.size() == 0 && mWord.length() == 6){
				for (int i = 0; i < mWord.length(); ++i) {
					letters.add(String.valueOf(mWord.charAt(i)));
				}
			}
		}

		mLetterOrganizer.setLettersPool(letters);
		mLetterOrganizer.show();

	}

	private void initializeWordBoxes() {
		mBoxesContainer.clear();

		ArrayList<ArrayList<String>> wordsByLength = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < 6; ++i) {
			wordsByLength.add(new ArrayList<String>());
		}

		for (String word: mWords) {
			wordsByLength.get(word.length()-1).add(word);
		}

		for (int i = 2; i < 6; ++i) {
			mBoxesContainer.addBox(wordsByLength.get(i));
		}

	}

	private void initializeWords() {
		try {
			this.mWords = Words.getWords(getApplicationContext());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void finishGame(){

	}

    private void timesUp() {

		findViewById(R.id.guess_btn).setVisibility(View.INVISIBLE);
		findViewById(R.id.next_btn).setVisibility(View.VISIBLE);

		mLetterOrganizer.hide();

		mBoxesContainer.showUnguessed();
		mBoxesContainer.permitDefinitions();

		if (isSixLetterWordGuessed){
			((TextView)findViewById(R.id.next_btn)).setText("Next Level...");
		} else {
			finishGame();
			((TextView)findViewById(R.id.next_btn)).setText("New Game");
		}

    }

    private void guessWord(String word) {
        sendEvent("GameActivity", "guessWord", word);
        if (mBoxesContainer.guessWord(word)) {
            sendEvent("GameActivity", "guessWord_success", word);
			if (word.length() == 6) {
				this.isSixLetterWordGuessed = true;
			}
			mScoring.wordGuessed(word, mGameNumber);
        }
    }

	private void updateScore(int guessScore, int totalScore){
		mScoreText.setText("Score: " + totalScore);
		Crouton.makeText(this, guessScore + " points!!!", croutonStyle).show();
	}

    private String getCurrentGuess() {
		return mLetterOrganizer.getCurrentGuessAndReset();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Crouton.cancelAllCroutons();
    }

}
