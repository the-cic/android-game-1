package com.mush.weirdo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.mush.weirdo.sprites.AnimatedSpriteShape;
import com.mush.weirdo.sprites.ImageSpriteShape;
import com.mush.weirdo.sprites.SpriteShape;
import com.mush.weirdo.sprites.ThreePartSpriteShape;
import com.mush.weirdo.util.AnimatedValue;
import com.mush.weirdo.space.SpaceObjectZComparator;
import com.mush.weirdo.space.IsometricPositionProjection;
import com.mush.weirdo.space.ParallaxPositionProjection;
import com.mush.weirdo.space.Point3F;
import com.mush.weirdo.space.PositionProjection;
import com.mush.weirdo.space.SpaceNode;
import com.mush.weirdo.space.SpaceObject;
import com.mush.weirdo.space.InputSpaceObjectBodyController;
import com.mush.weirdo.space.SpaceObjectBody;
import com.mush.weirdo.space.TilingPositionProjection;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mirko on 03/03/2016.
 */
public class GameContent {
    public static final int WIDTH = 180;
    public static final int HEIGHT = 100;

    private static final int HORIZON_Y = 40;
    private static final int GROUND_Y = HORIZON_Y + 3;
//    private static final int BOTTOM_Y = 50;

    private static final int MAP_TOP_Z = 0;
    private static final int MAP_BOTTOM_Z = 55;

    private GameControls controls;

    private SpaceNode rootNode;
    private PositionProjection parallaxProjection;
    private PositionProjection tileProjection;
    private PositionProjection isometricProjection;

    // Sky
    private ArrayList<SpaceObject> fixedBgObjects;
    // Ground texture
    private ArrayList<SpaceObject> tilingBgObjects;
    // Background panorama
    private ArrayList<SpaceObject> parallaxObjects;
    // Foreground objects
    private ArrayList<SpaceObject> foregroundObjects;
    // Foreground blocking objects' bodies
    private ArrayList<SpaceObjectBody> objectBodies;
    private SpaceObject playerObject;
    private SpaceObjectZComparator zComparator;

    private MapProvider mapProvider;
    private MapController mapController;

    private AnimatedValue pan;

    private Paint paint;
    private Paint paint2;

    public GameContent(Resources resources) {
        controls = new GameControls();
        zComparator = new SpaceObjectZComparator();
        paint = new Paint();

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        paint2 = new Paint();

        paint2.setColor(Color.GREEN);
        paint2.setStyle(Paint.Style.STROKE);

        pan = new AnimatedValue(0);

        rootNode = new SpaceNode();
        rootNode.localPosition.set(0, 0, 0);

        fixedBgObjects = new ArrayList<>();
        parallaxObjects = new ArrayList<>();
        tilingBgObjects = new ArrayList<>();
        foregroundObjects = new ArrayList<>();
        objectBodies = new ArrayList<>();

        mapProvider = new MapProvider(resources);

        int BASE = HORIZON_Y;

        fixedBgObjects.add(createSpaceObject(createSpaceObject(resources, R.drawable.horizon), null, 0, 0, 0));

        tilingBgObjects.add(createSpaceObject(createSpaceObject(resources, R.drawable.ground), rootNode, 0, GROUND_Y, 0));
        tilingBgObjects.add(createSpaceObject(createSpaceObject(resources, R.drawable.ground), rootNode, -WIDTH, GROUND_Y, 0));

        parallaxObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.mountains_far), rootNode, 180 * 2, BASE, 40));
        parallaxObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.mountains_mid_far), rootNode, 180 * 1, BASE, 32));
        parallaxObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.mountains_mid_near), rootNode, 180 * 3, BASE, 28));
        parallaxObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.cloud), rootNode, 180 * 2.5f, BASE - 90, 22));
        parallaxObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.cloud), rootNode, 180 * 1, BASE - 90, 18));
        parallaxObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.mountains_near), rootNode, 180 * 1.5f, BASE, 13));
        parallaxObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.trees_far), rootNode, 180, BASE, 10));

//        foregroundObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.hill_near), rootNode, 40, 0, (GROUND_Y - 0.01f)));
//        foregroundObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.grass_near), rootNode, 35, 0, (GROUND_Y + 15)));
//        foregroundObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.grass_near), rootNode, 10, 0, (BOTTOM_Y + 10)));

//        foregroundObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.table), rootNode, 20, 0, (BOTTOM_Y + 11)));
//        setupSpaceObjectBody(foregroundObjects.get(foregroundObjects.size() - 1), 0, -0.2f, 1, 0);

//        SpriteShape wallShape;

//        wallShape = new ThreePartSpriteShape(resources, R.drawable.wall_single, 17, 32, 17, 1);
//        wallShape.getPivot().offset(0, -5);
//        foregroundObjects.add(createSpaceObject(createSpaceObject(wallShape), rootNode, 80, 0, (BOTTOM_Y + 30)));
//        setupSpaceObjectBody(foregroundObjects.get(foregroundObjects.size() - 1), 0, -0.2f, 1, 0);
//
//        wallShape = new ThreePartSpriteShape(resources, R.drawable.wall_single, 17, 32, 17, 1);
//        wallShape.getPivot().offset(0, -5);
//        foregroundObjects.add(createSpaceObject(createSpaceObject(wallShape), rootNode, 120, 0, (BOTTOM_Y + 50)));
//        setupSpaceObjectBody(foregroundObjects.get(foregroundObjects.size() - 1), 0, -0.2f, 1, 0);

//        wallShape = new ThreePartSpriteShape(resources, R.drawable.wall_single, 17, 32, 17, 1);
//        wallShape.getPivot().offset(0, -5);
//        foregroundObjects.add(createSpaceObject(createSpaceObject(wallShape), rootNode, 130, 0, (BOTTOM_Y + 5)));
//        setupSpaceObjectBody(foregroundObjects.get(foregroundObjects.size() - 1), 0, -0.2f, 1, 0);

        SpaceNode mapNode = new SpaceNode();
        mapNode.addToNode(rootNode);
        mapController = new MapController(mapNode, foregroundObjects, mapProvider);

        SpaceNode playerNode = new SpaceNode();
//        playerNode.localPosition.set(WIDTH * 0.4f, 0, GROUND_Y);
        playerNode.localPosition.set(WIDTH * 0.4f, 0, 0);
        playerNode.addToNode(rootNode);

        playerObject = new SpaceObject(playerNode);

        playerObject.shape = new AnimatedSpriteShape(resources, R.drawable.weirdo_spritesheet, 32, 32, new int[]{1, 4});
        playerObject.shape.setPivot(new Point(playerObject.shape.getWidth() / 2, playerObject.shape.getHeight()));

        setupSpaceObjectBody(playerObject, -0.2f, -0.1f, 0.2f, 0);
        playerObject.body.setController(new InputSpaceObjectBodyController(controls, playerObject));

        foregroundObjects.add(playerObject);

        collectBodies();

        parallaxProjection = new ParallaxPositionProjection(10, new Point3F(0, BASE, 0));
        tileProjection = new TilingPositionProjection(WIDTH, HEIGHT);
        isometricProjection = new IsometricPositionProjection();
    }

    private void collectBodies(){
        objectBodies.clear();
        for (SpaceObject spaceObject : foregroundObjects) {
            if (spaceObject.body != null) {
                objectBodies.add(spaceObject.body);
            }
        }
    }

    public void processInput(MotionEvent event, int screenWidth, int screenHeight) {
        controls.processInput(event, screenWidth, screenHeight);
    }

    public void update(double secondsPerFrame) {

        if (pan.getVelocity() == 0) {
            if (playerObject.spaceNode.localToGlobal().x > WIDTH * 0.75) {
                pan.transitionTo(pan.getValue() + WIDTH * 0.45, 0.5);
            }
            if (playerObject.spaceNode.localToGlobal().x < WIDTH * 0.25) {
                double panTo = pan.getValue() - WIDTH * 0.45;
                if (panTo < 0) {
                    panTo = 0;
                }
                pan.transitionTo(panTo, 0.5);
            }
        }
        pan.update(secondsPerFrame);

        int chunkW = mapProvider.chunkLength * mapProvider.objectWidth;
        int chunkP = (int) (playerObject.spaceNode.localPosition.x / chunkW) - 1;

        if (chunkP > mapController.getCurrentChunkIndex()) {
            System.out.println(chunkP + " " + mapController.getCurrentChunkIndex());
            mapController.advanceChunk(+1);
            collectBodies();
        } else if (chunkP < mapController.getCurrentChunkIndex()) {
            System.out.println(chunkP + " " + mapController.getCurrentChunkIndex());
            mapController.advanceChunk(-1);
            collectBodies();
        }

        // ok for now, but should be in an array of somethings
        playerObject.updateShape(secondsPerFrame);

        for (SpaceObjectBody body : objectBodies) {
            body.update(secondsPerFrame);
        }

        checkObjectBodyCollisions();

        for (SpaceObjectBody body : objectBodies) {
            body.applyPositionUpdate();
            // use global position?
            if (body.getNode().localPosition.z < MAP_TOP_Z) {
                body.getNode().localPosition.z = MAP_TOP_Z;
            }
            if (body.getNode().localPosition.z > MAP_BOTTOM_Z) {
                body.getNode().localPosition.z = MAP_BOTTOM_Z;
            }
            if (body.getNode().localPosition.x < 0) {
                body.getNode().localPosition.x = 0;
            }
        }
    }

    private void checkObjectBodyCollisions() {
        for (SpaceObjectBody body : objectBodies) {
            boolean blocked = false;
            for (SpaceObjectBody other : objectBodies) {
                if (body != other && !blocked) {
                    if (body.willIntersect(other)) {
                        blocked = true;
                        //System.out.println(body.getNode().localPosition + " blocked by "+ other.getNode().localPosition);
                    }
                }
            }
            if (blocked) {
                body.clearNextPosition();
            }
        }
    }

    public void draw(Canvas canvas, float viewWidth, float viewHeight) {
        final float scaleFactorX = viewWidth / WIDTH;
        final float scaleFactorY = viewHeight / HEIGHT;
        final float scaleFactor = Math.min(scaleFactorX, scaleFactorY);
        final float offsetX;
        final float offsetY;

        if (scaleFactorX > scaleFactorY) {
            offsetX = (viewWidth - WIDTH * scaleFactor) / 2;
            offsetY = 0;
        } else {
            offsetX = 0;
            offsetY = (viewHeight - HEIGHT * scaleFactor) / 2;
        }

        final int savedState = canvas.save();
        canvas.translate(offsetX, offsetY);
        canvas.clipRect(0, 0, viewWidth - offsetX * 2, viewHeight - offsetY * 2);
        canvas.scale(scaleFactor, scaleFactor);

        // Draw on actual lo-res bitmap, for speed and pixels
        Bitmap buffer = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas bufferCanvas = new Canvas(buffer);

        this.drawContent(bufferCanvas);

        applyBuffer(buffer, canvas);

        canvas.restoreToCount(savedState);
    }

    private void applyBuffer(Bitmap buffer, Canvas canvas) {
        canvas.drawBitmap(buffer, 0, 0, null);
    }

    private void drawContent(Canvas canvas) {
        rootNode.localPosition.set(-pan.getValue(), 0, GROUND_Y);

        for (SpaceObject spaceObject : fixedBgObjects) {
            spaceObject.draw(canvas, null);
        }

        for (SpaceObject spaceObject : tilingBgObjects) {
            spaceObject.draw(canvas, tileProjection);
        }

        for (SpaceObject spaceObject : parallaxObjects) {
            spaceObject.draw(canvas, parallaxProjection);
        }

        Collections.sort(foregroundObjects, zComparator);
        Collections.reverse(foregroundObjects);

        for (SpaceObject spaceObject : foregroundObjects) {
            spaceObject.draw(canvas, isometricProjection);
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

        SpaceNode node = new SpaceNode();
        SpaceObject spaceObject = new SpaceObject(node);
        spaceObject.shape = shape;

        return spaceObject;
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

    private void setupSpaceObjectBody(SpaceObject spaceObject, float fl, float ft, float fr, float fb) {
        spaceObject.setupBody(new Rect(
                (int) (spaceObject.shape.getWidth() * fl),
                (int) (spaceObject.shape.getHeight() * ft),
                (int) (spaceObject.shape.getWidth() * fr),
                (int) (spaceObject.shape.getHeight() * fb)));
    }

}
