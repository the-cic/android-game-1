package com.mush.weirdo;

import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.widget.Space;

import com.mush.weirdo.space.SpaceNode;
import com.mush.weirdo.space.SpaceObject;
import com.mush.weirdo.sprites.ImageSpriteShape;
import com.mush.weirdo.sprites.SpriteShape;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Cic on 31.1.2017.
 */
public class MapProvider {

    private Resources resources;
    public final int chunkLength = 3;
    public final int objectWidth = 18;
    public final int objectHeight = 5;

    // 20 x 10
    String [] map = new String[]{
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

    public MapProvider(Resources resources1){
        this.resources = resources1;
    }

    private String getLineChunk(String line, int index) {
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

    private String[] getMapChunk(int index){
        String[] chunk = new String[map.length];
        for (int i=0; i < map.length; i++) {
            chunk[i] = getLineChunk(map[i], index);
        }
        return chunk;
    }

    public ArrayList<SpaceObject> getChunk(int index){
        String[] mapChunk = getMapChunk(index);
        ArrayList<SpaceObject> objects = new ArrayList<>();
        for (int j = 0; j < mapChunk.length; j++) {
            String mapLine = mapChunk[j];
            for (int i = 0; i < mapLine.length(); i++) {
                char c = mapLine.charAt(i);
                SpaceObject object = getMapObject(c);
                if (object != null) {
                    object.spaceNode.localPosition.offset(i * objectWidth, 0, j * objectHeight);
                    objects.add(object);
                }
            }
        }
        return objects;
    }

    public SpaceObject getMapObject(char c) {
        SpaceObject object = null;
        switch (c) {
            case 'T' :
                object = getMapObject(R.drawable.tree_fir, 0, 1);
                setupObjectBody(object, 0.2f, -0.1f, 0.7f, 0);
                object.spaceNode.localPosition.offset(3, 0, 0);
                break;
            case 'B' :
                object = getMapObject(R.drawable.block, 0, 1);
                setupObjectBody(object, 0, -0.2f, 1, 0);
                break;
            case ';' :
                object = getMapObject(R.drawable.grass_yellow, 0, 1f);
                object.spaceNode.localPosition.offset(0, 0, -2);
                break;
            case ',' :
                object = getMapObject(R.drawable.grass_green, 0, 1f);
                object.spaceNode.localPosition.offset(0, 0, -2);
                break;
        }
        return object;
    }

    public SpaceObject getMapObject(int resourceId, float fx, float fy) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId);
        shape.setPivot(new Point((int)(shape.getWidth() * fx), (int)(shape.getHeight() * fy)));

        SpaceNode node = new SpaceNode();
        SpaceObject object = new SpaceObject(node);
        object.shape = shape;

        object.spaceNode.localPosition.set(0, 0, 0);

        return object;
    }

    private void setupObjectBody(SpaceObject object, float fl, float ft, float fr, float fb) {
        object.setupBody(new Rect(
                (int) (object.shape.getWidth() * fl),
                (int) (object.shape.getHeight() * ft),
                (int) (object.shape.getWidth() * fr),
                (int) (object.shape.getHeight() * fb)));
    }

}
