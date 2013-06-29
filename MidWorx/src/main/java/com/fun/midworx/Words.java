package com.fun.midworx;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Words {
    private Map<char[], List<String>> allWordsMap = new HashMap<char[], List<String>>();
    private List<String> sixLetterWords = new ArrayList<String>();

    public Words(Context context) throws IOException {
        Scanner scanner = new Scanner(context.getAssets().open("words"));
        while (scanner.hasNext()) {
            String line = scanner.nextLine().trim().toLowerCase();
            if (!line.matches("^[a-z]+$")) {
                continue;
            }
            char[] arr = line.toCharArray();
            Arrays.sort(arr);
            if (!allWordsMap.containsKey(arr)) {
                allWordsMap.put(arr, new ArrayList<String>());
            }
            allWordsMap.get(arr).add(line);
            if (line.length() == 6) {
                sixLetterWords.add(line);
            }
        }
    }

    public List<String> getWord() {
        int n = new Random().nextInt(sixLetterWords.size());
        String chosenWord = sixLetterWords.get(n);
        char[] arr = chosenWord.toCharArray();
        Arrays.sort(arr);
        return allWordsMap.get(arr);
    }
}
