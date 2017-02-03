package com.mush.weirdo.map;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;

import com.mush.weirdo.R;
import com.mush.weirdo.space.ParallaxPositionProjection;
import com.mush.weirdo.space.Point3F;
import com.mush.weirdo.space.PositionProjection;
import com.mush.weirdo.space.SpaceNode;
import com.mush.weirdo.space.SpaceObject;
import com.mush.weirdo.space.TilingPositionProjection;
import com.mush.weirdo.sprites.ImageSpriteShape;
import com.mush.weirdo.sprites.SpriteShape;

import java.util.ArrayList;

/**
 * Separated cosmetic background bits
 *
 * Created by Cic on 1.2.2017.
 */
public class BackgroundObjects {

    private static final int HORIZON_Y = 40;
    private static final int GROUND_Y = HORIZON_Y + 3;

    private PositionProjection parallaxProjection;
    private PositionProjection tileProjection;

    // Sky
    private ArrayList<SpaceObject> fixedBgObjects;
    // Ground texture
    private ArrayList<SpaceObject> tilingBgObjects;
    // Background panorama
    private ArrayList<SpaceObject> parallaxObjects;

    public BackgroundObjects(Resources resources, SpaceNode rootNode, int width, int height) {

        fixedBgObjects = new ArrayList<>();
        parallaxObjects = new ArrayList<>();
        tilingBgObjects = new ArrayList<>();

        int BASE = HORIZON_Y;

        fixedBgObjects.add(createSpaceObject(createSpaceObject(resources, R.drawable.horizon), null, 0, 0, 0));

        tilingBgObjects.add(createSpaceObject(createSpaceObject(resources, R.drawable.ground), rootNode, 0, GROUND_Y, 0));
        tilingBgObjects.add(createSpaceObject(createSpaceObject(resources, R.drawable.ground), rootNode, -width, GROUND_Y, 0));

        parallaxObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.mountains_far), rootNode, 180 * 2, BASE, 40));
        parallaxObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.mountains_mid_far), rootNode, 180 * 1, BASE, 32));
        parallaxObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.mountains_mid_near), rootNode, 180 * 6, BASE, 30));
        parallaxObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.cloud), rootNode, 180 * 2.5f, BASE - 90, 22));
        parallaxObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.cloud), rootNode, 180 * 1, BASE - 90, 20));
        parallaxObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.mountains_near), rootNode, 180 * 1.8f, BASE, 15));
        parallaxObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.trees_far), rootNode, 180, BASE, 10));

        parallaxProjection = new ParallaxPositionProjection(10, new Point3F(0, BASE, 0));
        tileProjection = new TilingPositionProjection(width, height);
    }

    public void draw(Canvas canvas) {
        for (SpaceObject spaceObject : fixedBgObjects) {
            spaceObject.draw(canvas, null);
            spaceObject.drawMarkers(canvas);
        }

        for (SpaceObject spaceObject : tilingBgObjects) {
            spaceObject.draw(canvas, tileProjection);
            spaceObject.drawMarkers(canvas);
        }

        for (SpaceObject spaceObject : parallaxObjects) {
            spaceObject.draw(canvas, parallaxProjection);
            spaceObject.drawMarkers(canvas);
        }
    }

    private SpaceObject createSpaceObject(SpaceObject spaceObject, SpaceNode parent, float x, float y, float z) {
        spaceObject.spaceNode.localPosition.set(x, y, z);

        if (parent != null) {
            spaceObject.spaceNode.addToNode(parent);
        }

        return spaceObject;
    }

    private SpaceObject createSpaceObject(Resources resources, int resourceId) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId);
        shape.setPivot(new Point(0, 0));

        return createSpaceObject(shape);
    }

    private SpaceObject createSpaceObject(SpriteShape shape) {
        SpaceNode node = new SpaceNode();
        SpaceObject spaceObject = new SpaceObject(node);
        spaceObject.shape = shape;

        return spaceObject;
    }

    private SpaceObject createGroundSpaceObject(Resources resources, int resourceId) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId);
        shape.setPivot(new Point(0, shape.getHeight()));

        SpaceNode node = new SpaceNode();
        SpaceObject spaceObject = new SpaceObject(node);
        spaceObject.shape = shape;

        return spaceObject;
    }

}
