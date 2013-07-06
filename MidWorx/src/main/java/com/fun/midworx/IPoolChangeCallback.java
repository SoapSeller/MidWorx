package com.fun.midworx;

import java.util.List;
import java.util.Set;

/**
 * Created: 7/6/13 1:13 PM
 */
public interface IPoolChangeCallback {
	public void poolChanged(List<Integer> chosenLetterIndexes);
}
