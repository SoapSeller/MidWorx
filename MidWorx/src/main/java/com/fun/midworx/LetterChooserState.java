package com.fun.midworx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: 7/6/13 12:46 PM
 */
public class LetterChooserState {

	private List<String> lettersPool;
	private List<Integer> chosenLetterIndexes;


	public void setLettersPool(List<String> lettersPool){
		this.lettersPool = new ArrayList<String>(lettersPool);
		this.chosenLetterIndexes = new ArrayList<Integer>();
		this.resetChoosable();
	}

	public void chooseLetter(int index){
		chosenLetterIndexes.add(index);
		poolChanged();
	}

	private void poolChanged(){

		for (IPoolChangeCallback poolChangeListener : this.poolChangeListeners) {
			poolChangeListener.poolChanged(chosenLetterIndexes);
		}
	}

	public String getLetterForIndex(int index){
		return lettersPool.get(index);
	}

	public void resetChoosable(){
		for (IPoolChangeCallback poolChangeListener : this.poolChangeListeners) {
			poolChangeListener.poolChanged(chosenLetterIndexes);
		}
	}

	private List<IPoolChangeCallback> poolChangeListeners = new ArrayList<IPoolChangeCallback>();

	public void onPoolChange(IPoolChangeCallback callback){
		this.poolChangeListeners.add(callback);
	}

}
