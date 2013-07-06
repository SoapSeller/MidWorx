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

        wordsIdxOffset = 4 + numOfWords*4*2;
    }

    public List<String> getWord() throws IOException {
        stream.reset();
        stream.skip(UINT_SIZE); // Num of words

        List<String> words = new ArrayList<String>();

        int idx = random.nextInt(numOfWords);

        stream.skipBytes(idx*UINT_SIZE*2); // skip IDX pairs of uint32

        int pos = swapEndian(stream.readInt());
        int num = swapEndian(stream.readInt());

        stream.reset();
        stream.skip(wordsIdxOffset + pos*4);

        for (int i = 0 ; i < num; ++i) {
            int wordPos = swapEndian(stream.readInt());

            wordsReader.reset();
            wordsReader.skip(wordPos);
            words.add(wordsReader.readLine().toLowerCase());
        }

        return words;
    }


//    private long readUnit32(byte[] data) {
//        return ((data[0]&0xFF)) |
//                ((data[1]&0xFF) << 8) |
//                ((data[2]&0xFF) << 16)  |
//                (data[3]&0xFF) << 24;
//    }

    private int swapEndian(int val) {
        return ((val & 0xFF) << 24)  |
               ((val & 0xFF00) << 8) |
               ((val >> 8) & 0xFF00) |
               ((val >> 24) & 0xFF);
    }
}
