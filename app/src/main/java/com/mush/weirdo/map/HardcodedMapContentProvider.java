package com.mush.weirdo.map;

/**
 * Created by Cic on 2.2.2017.
 */
public class HardcodedMapContentProvider implements MapContentProvider {

    // 20 x 10
    String[] map = new String[]{
            "T..,....;.T.......T.",
            ".;...,;......,,.T...",
            "....;..T....T....,..",
            "..T...........,,....",
            ".....BBBBBBBB....T..",
            ".T...B......B.,....,",
            "...;.B......B..,;,..",
            "..B..B......B..,.;;.",
            ".B..,BBB.BBBB.,.T..;",
            ".,.T.,,..,;..,,....."
    };

    private String getLineChunk(String line, int index, int chunkLength) {
        int start = index * chunkLength;
        int end = start + chunkLength;
        if (start < 0) {
            if (end < 0) {
                return "";
            }
            start = 0;
        }
        if (start > line.length()) {
            return "";
        }
        if (end > line.length()) {
            end = line.length();
        }
        return line.substring(start, end);
    }

    public String[] getMapChunk(int index, int chunkLength) {
        String[] chunk = new String[map.length];
        for (int i = 0; i < map.length; i++) {
            chunk[i] = getLineChunk(map[i], index, chunkLength);
        }
        return chunk;
    }

}
