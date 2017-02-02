package com.mush.weirdo.map;

/**
 * Created by Cic on 2.2.2017.
 */
public interface MapContentProvider {

    public String[] getMapChunk(int index, int chunkWidth, int chunkHeight);
}
