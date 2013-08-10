package com.fun.midworx;

public class GameTimer {

	private long mStartTime;

	private static final int MAX_GAME_SECONDS = 20;

	public void start(){
		mStartTime = System.currentTimeMillis();
	}

	public int getTimeLeft(){
		return MAX_GAME_SECONDS - (int)((System.currentTimeMillis() - mStartTime) / 1000);
	}


	public boolean isGameFinished(){
		if (getTimeLeft() <= 0){
			return true;
		} else {
			return false;
		}
	}

}
