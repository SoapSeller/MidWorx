package com.fun.midworx;

public class GameTimer {

	private long mStartTime;

	private static final long MAX_GAME_SECONDS = 20;

	public void start(){
		mStartTime = System.currentTimeMillis();
	}

	public int getSecondsSinceStart(){
		return (int)((System.currentTimeMillis() - mStartTime) / 1000);
	}

	public boolean isGameFinished(){
		if (getSecondsSinceStart() >= MAX_GAME_SECONDS){
			return true;
		} else {
			return false;
		}
	}

}
