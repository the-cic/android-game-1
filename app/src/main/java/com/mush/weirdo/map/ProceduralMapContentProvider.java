package com.mush.weirdo.map;

import android.util.Log;

import com.mush.weirdo.util.RandomKey;

/**
 * Created by Cic on 2.2.2017.
 */
public class ProceduralMapContentProvider implements MapContentProvider {

    public ProceduralMapContentProvider() {

    }

    @Override
    public String[] getMapChunk(int index, int chunkWidth, int chunkHeight) {
        char[][] chunk = new char[chunkHeight][];
        for (int j = 0; j < chunkHeight; j++) {
            chunk[j] = new char[chunkWidth];
            chunk[j][0] = '|';
            for (int i = 1; i < chunkWidth; i++) {
                chunk[j][i] = '.';
            }
        }

        createChunk(chunk, index, chunkWidth, chunkHeight);

        String[] stringChunk = new String[chunkHeight];
        for (int j = 0; j < chunkHeight; j++) {
            stringChunk[j] = new String(chunk[j]);
        }
        return stringChunk;
    }

    private void createChunk(char[][] chunk, int index, int chunkWidth, int chunkHeight) {
        RandomKey key0 = getKeyForIndex(index);
        RandomKey key = new RandomKey(key0.values[0], key0.values[1], key0.values[2]);

        int[] val = processKey(key);
        int grass = (val[3] + 1) * 2;
        int trees = val[2] * 2;
        for (int i = 0; i < grass; i++) {
            key.advance();
            val = processKey(key);
            int x = val[0] % chunkWidth;
            int y = val[1] % chunkHeight;
            chunk[y][x] = val[2] > 3 ? ',' : (char) (',' + 0x0100);
        }
        for (int i = 0; i < trees; i++) {
            key.advance();
            val = processKey(key);
            int x = val[0] % chunkWidth;
            int y = val[1] % chunkHeight;
            chunk[y][x] = 'T';
        }
        key.advance();
        val = processKey(key);
        if (val[2] > 4) {
            int w = val[4] > 3 ? 1 : (val[4] > 1 ? 2 : 3);
            int h = val[3] > 3 ? 1 : (val[3] > 1 ? 2 : 3);
            int x = val[0] % (chunkWidth - w - 2);
            int y = 1 + (val[1] % (chunkHeight - (h + 2)));

            makeWall(chunk, x, y, 1, true, (char) ('W' + 0x0000));
            makeWall(chunk, x + 1, y, w, true, (char) ('W' + 0x0100));
            makeWall(chunk, x + 1 + w, y, 1, true, (char) ('W' + 0x0200));
            makeWall(chunk, x, y + 1, h, false, (char) ('W' + 0x0300));
            for (int i = 0; i < w; i++) {
                makeWall(chunk, x + 1 + i, y + 1, h + 1-1, false, (char) ('w' + 0x0400));
            }
            makeWall(chunk, x + 1 + w, y + 1, h, false, (char) ('W' + 0x0500));
            makeWall(chunk, x, y + h + 1, 1, true, (char) ('W' + 0x0600));
            makeWall(chunk, x + 1, y + h + 1, w, true, (char)('W' + 0x0700));
            makeWall(chunk, x + 1 + w, y + h + 1, 1, true, (char) ('W' + 0x0800));
            makeWall(chunk, x + 1 + 0, y + h + 1, 1, true, (char) ('w' + 0x0400));
        }
    }

    private void makeWall(char[][] chunk, int x0, int y0, int length, boolean horizontal, char c) {
        int x = x0;
        int y = y0;
        chunk[y][x] = c;
        for (int i = 1; i < length; i++) {
            if (horizontal) {
                x++;
            } else {
                y++;
            }
            if (y < chunk.length && x < chunk[y].length) {
                chunk[y][x] = c;
            }
        }
    }

    private int[] processKey(RandomKey k) {
        int x = (k.values[0] >> 8); //& 0x0f;
        int y = (k.values[1] >> 8); // & 0x0f;
        int g = (k.values[1] >> 3) & 0x07;
        int e = (k.values[0] >> 8) & 0x07;
        int f = (k.values[2] >> 0) & 0x07;

        return new int[]{x, y, g, e, f};
    }


    private RandomKey getKeyForIndex(int index) {
        RandomKey key = new RandomKey(0x5a4a, 0x0248, 0xb753, 0xf8a3, 0xc3759a);
        key.advance(index);
        return key;
    }

}
