package com.mush.weirdo.map;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;

import com.mush.weirdo.R;
import com.mush.weirdo.space.SpaceNode;
import com.mush.weirdo.space.SpaceObject;
import com.mush.weirdo.sprites.ImageSpriteShape;
import com.mush.weirdo.sprites.SpriteShape;
import com.mush.weirdo.util.BitmapFrames;
import com.mush.weirdo.util.RandomKey;

import java.util.ArrayList;

/**
 * Created by Cic on 31.1.2017.
 */
public class MapProvider {

    public final int chunkWidth = 5;
    public final int chunkHeight = 10;
    public final int objectWidth = 18;
    public final int objectHeight = 5;

    private Resources resources;
    private MapContentProvider contentProvider;
    private SpriteShape[] wallBits;

    public MapProvider(Resources resources1) {
        this.resources = resources1;
//        this.contentProvider = new HardcodedMapContentProvider();
        this.contentProvider = new ProceduralMapContentProvider();

        Bitmap wallsJelly = BitmapFactory.decodeResource(resources, R.drawable.walls);
        BitmapFrames frames = new BitmapFrames(wallsJelly, 18, 20, 9);
        wallBits = new SpriteShape[9];
        for (int i = 0; i < 9; i++) {
            SpriteShape shape = new ImageSpriteShape(frames.getFrame(i));
            if (i >= 0 && i <= 5) {
                shape.setPivot(new Point(0, shape.getHeight() - 4));
            } else {
                shape.setPivot(new Point(0, shape.getHeight()));
            }
            wallBits[i] = shape;
        }

        // Test map
        String[] lines = null;
        for (int i = 0; i < 40; i++) {
            String[] chunkLines = contentProvider.getMapChunk(i, chunkWidth, chunkHeight);
            if (lines == null) {
                lines = chunkLines;
            } else {
                for (int j = 0; j < lines.length; j++) {
                    lines[j] += chunkLines[j];
                }
            }
        }

        System.out.println("Map:");
        for (int j = 0; j < lines.length; j++) {
            System.out.println(lines[j]);
        }
    }

    public ArrayList<SpaceObject> getChunk(int index) {
        RandomKey randomKey = new RandomKey(0x5a4a, 0x0248, 0xb753);
        String[] mapChunk = contentProvider.getMapChunk(index, chunkWidth, chunkHeight);
        ArrayList<SpaceObject> objects = new ArrayList<>();
        for (int j = 0; j < mapChunk.length; j++) {
            String mapLine = mapChunk[j];
            for (int i = 0; i < mapLine.length(); i++) {
                char c = mapLine.charAt(i);
                SpaceObject object = getMapObject(c);
                if (object != null) {
                    object.spaceNode.localPosition.offset(i * objectWidth, 0, j * objectHeight);
                    if (c == 'T' || c == ',' || c == ';') {
                        object.spaceNode.localPosition.offset((randomKey.values[0] % 5) - 2, 0, (randomKey.values[0] % 2) - 1);
                        randomKey.advance();
                    }
                    objects.add(object);
                }
            }
        }
        return objects;
    }

    public SpaceObject getMapObject(char c) {
        SpaceObject object = null;
        switch (c) {
            case 'T':
                object = getMapObject(R.drawable.tree_fir);
                setObjectShapePivotProportional(object, 0, 1);
                object.setupBodyRelativeToShape(0.5f, 0f, 0.5f, 0, true);
                object.body.offsetBounds(-3, -2, 2, 0);
                object.spaceNode.localPosition.offset(3, 0, 0);
                break;
            case 'B':
                object = getMapObject(R.drawable.block_2);
                setObjectShapePivotProportional(object, 0, 1);
                object.setupBodyRelativeToShape(0, -0.2f, 1, 0, true);
                break;

            case 'W'+0:
                object = getMapObject(wallBits[0]);
                object.spaceNode.localPosition.set(0, 0, -4);
                object.setupBodyRelativeToShape(0, 0, 1, 0, true);
                object.body.offsetBounds(0, -1, 0, 4);
                break;
            case 'W'+1:
                object = getMapObject(wallBits[1]);
                object.spaceNode.localPosition.set(0, 0, -4);
                object.setupBodyRelativeToShape(0, 0, 1, 0, true);
                object.body.offsetBounds(0, -1, 0, 1);
                break;
            case 'W'+2:
                object = getMapObject(wallBits[2]);
                object.spaceNode.localPosition.set(0, 0, -4);
                object.setupBodyRelativeToShape(0, 0, 1, 0, true);
                object.body.offsetBounds(0, -1, 0, 4);
                break;

            case 'W'+3:
                object = getMapObject(wallBits[3]);
                object.spaceNode.localPosition.set(0, 0, -4);
                object.setupBodyRelativeToShape(0, 0, 0, 0, true);
                object.body.offsetBounds(0, -1, 5, 4);
                break;
            case 'W'+4:
                object = getMapObject(wallBits[4]);
                object.spaceNode.localPosition.set(0, 0, -4);
                //setupObjectBody(object, 0, -0.2f, 1, 0);
                break;
            case 'W'+5:
                object = getMapObject(wallBits[5]);
                object.spaceNode.localPosition.set(0, 0, -4);
                object.setupBodyRelativeToShape(1, 0, 1, 0, true);
                object.body.offsetBounds(-5, -1, 0, 4);
                break;

            case 'W'+6:
                object = getMapObject(wallBits[6]);
                object.setupBodyRelativeToShape(0, 0, 1, 0, true);
                object.body.offsetBounds(0, -5, 0, 0);
                break;
            case 'W'+7:
                object = getMapObject(wallBits[7]);
                object.setupBodyRelativeToShape(0, 0, 1, 0, true);
                object.body.offsetBounds(0, -5, 0, 0);
                break;
            case 'W'+8:
                object = getMapObject(wallBits[8]);
                object.setupBodyRelativeToShape(0, 0, 1, 0, true);
                object.body.offsetBounds(0, -5, 0, 0);
                break;
            case ';':
                object = getMapObject(R.drawable.grass_yellow);
                setObjectShapePivotProportional(object, 0, 1);
                object.spaceNode.localPosition.offset(0, 0, -1);
                break;
            case ',':
                object = getMapObject(R.drawable.grass_green);
                setObjectShapePivotProportional(object, 0, 1);
                object.spaceNode.localPosition.offset(0, 0, -1);
                break;
        }
        return object;
    }

    public SpaceObject getMapObject(int resourceId/*, float fx, float fy*/) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId);
//        shape.setPivot(new Point((int) (shape.getWidth() * fx), (int) (shape.getHeight() * fy)));

        SpaceNode node = new SpaceNode();
        SpaceObject object = new SpaceObject(node);
        object.shape = shape;

        object.spaceNode.localPosition.set(0, 0, 0);

        return object;
    }

    public SpaceObject getMapObject(SpriteShape shape) {
        SpaceNode node = new SpaceNode();
        SpaceObject object = new SpaceObject(node);
        object.shape = shape;

        object.spaceNode.localPosition.set(0, 0, 0);

        return object;
    }

    public void setObjectShapePivotProportional(SpaceObject object, float fx, float fy){
        SpriteShape shape = object.shape;
        shape.setPivot(new Point((int) (shape.getWidth() * fx), (int) (shape.getHeight() * fy)));
    }

}
