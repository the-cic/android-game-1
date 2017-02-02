package com.mush.weirdo.map;

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

    private void createChunk(char[][] chunk, int index, int chunkWidth, int chunkHeight){
        int[] key = getKeyForIndex(index);

        int[] val = processKey(key);
        int trees = val[2];
        for (int i = 0; i < trees; i++) {
            advanceKey(key);
            val = processKey(key);
            int x = val[0] % chunkWidth;
            int y = val[1] % chunkHeight;
            chunk[y][x] = 'T';
        }
    }

    private int[] processKey(int[] k) {
        int x = (k[0] >> 8) / 16; //& 0x0f;
        int y = (k[1] >> 8) / 16; // & 0x0f;
        int g = (k[1] >> 3) & 0x07;
        int e = (k[0] >> 8) & 0x07;

        return new int[]{x, y, g, e};
    }


    private int[] getKeyForIndex(int index){
        int[] key = new int[]{0x5a4a, 0x0248, 0xb753, 0xf8a3, 0xc3759a};
        for (int i = 0; i < index; i++) {
            advanceKey(key);
        }
        return key;
    }

    private void advanceKey(int[] key) {
        int temp = key[0];
        for (int i = 1; i < key.length; i++) {
            temp += key[i];
            key[i-1] = key[i];
        }
        key[key.length-1] = (int) temp & 0xffff;
    }


}
