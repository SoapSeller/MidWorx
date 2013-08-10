package com.fun.midworx;

import android.content.Context;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Words {
    private static final int UINT_SIZE = 4;

    private static final int MIN_WORD_COUNT = 5;

    private DataInputStream stream;
    private BufferedReader wordsReader;
    private int numOfWords;
    private int wordsIdxOffset;

    private Random random;

    public Words(Context context) throws IOException {
        random = new Random();

        stream = new DataInputStream(context.getAssets().open("words.idx"));
        wordsReader = new BufferedReader(new InputStreamReader(context.getAssets().open("words")));
        wordsReader.mark(Integer.MAX_VALUE);

        numOfWords = swapEndian(stream.readInt());

        wordsIdxOffset = UINT_SIZE + numOfWords*UINT_SIZE*2;
    }

    public List<String> getWords() throws IOException {
        List<String> words = new ArrayList<String>();

        int pos, num;
        do {
            int idx = random.nextInt(numOfWords);

            stream.reset();
            stream.skip(UINT_SIZE + idx * UINT_SIZE * 2); // skip first uint32 and IDX pairs of uint32

            pos = swapEndian(stream.readInt());
            num = swapEndian(stream.readInt());
        } while (num < MIN_WORD_COUNT);

        stream.reset();
        stream.skip(wordsIdxOffset + pos*UINT_SIZE);

        for (int i = 0 ; i < num; ++i) {
            int wordPos = swapEndian(stream.readInt());

            wordsReader.reset();
            wordsReader.skip(wordPos);
            words.add(wordsReader.readLine().toLowerCase());
        }

        return words;
    }

    private int swapEndian(int val) {
        return ((val & 0xFF) << 24)  |
               ((val & 0xFF00) << 8) |
               ((val >> 8) & 0xFF00) |
               ((val >> 24) & 0xFF);
    }
}
