package com.mush.weirdo.map;

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
        int trees = val[2];
        for (int i = 0; i < grass; i++) {
            key.advance();
            val = processKey(key);
            int x = val[0] % chunkWidth;
            int y = val[1] % chunkHeight;
            chunk[y][x] = val[2] > 3 ? ',' : ';';
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
            int h = val[3] > 3 ? 1 : (val[3] > 1 ? 2 : 3);
            int x = val[0] % (chunkWidth - 2);
            int y = 1 + (val[1] % (chunkHeight - (h+2)));

//            makeWall(chunk, x, y, 3, true, 'W');
//            makeWall(chunk, x, y + 1, 1, true, 'W');
//            makeWall(chunk, x + 2, y + 1, 1, true, 'W');

            makeWall(chunk, x, y, 1, true, (char)('W'+0));
            makeWall(chunk, x+1, y, 1, true, (char)('W'+1));
            makeWall(chunk, x+2, y, 1, true, (char)('W'+2));
            makeWall(chunk, x, y+1, h, false, (char)('W'+3));
            makeWall(chunk, x+1, y+1, h+1, false, (char)('W'+4));
            makeWall(chunk, x+2, y+1, h, false, (char)('W'+5));
            makeWall(chunk, x, y+h+1, 1, true, (char)('W'+6));
//            makeWall(chunk, x+1, y+h+1, 1, true, (char)('W'+7));
            makeWall(chunk, x+2, y+h+1, 1, true, (char)('W'+8));
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

        return new int[]{x, y, g, e};
    }


    private RandomKey getKeyForIndex(int index) {
        RandomKey key = new RandomKey(0x5a4a, 0x0248, 0xb753, 0xf8a3, 0xc3759a);
        key.advance(index);
        return key;
    }

}
