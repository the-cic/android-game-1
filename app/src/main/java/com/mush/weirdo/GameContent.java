package com.mush.weirdo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import com.mush.weirdo.map.BackgroundObjects;
import com.mush.weirdo.map.MapController;
import com.mush.weirdo.map.MapProvider;
import com.mush.weirdo.space.SpaceObjectBodySpace;
import com.mush.weirdo.sprites.AnimatedSpriteShape;
import com.mush.weirdo.util.AnimatedValue;
import com.mush.weirdo.space.SpaceObjectZComparator;
import com.mush.weirdo.space.IsometricPositionProjection;
import com.mush.weirdo.space.PositionProjection;
import com.mush.weirdo.space.SpaceNode;
import com.mush.weirdo.space.SpaceObject;
import com.mush.weirdo.space.InputSpaceObjectBodyController;

import java.util.Collections;

/**
 * Created by mirko on 03/03/2016.
 */
public class GameContent {
    public static final int WIDTH = 180;
    public static final int HEIGHT = 100;

    private static final int HORIZON_Y = 40;
    private static final int GROUND_Y = HORIZON_Y + 3;

    public static final int MAP_TOP_Z = 0;
    public static final int MAP_BOTTOM_Z = 55;

    private GameControls controls;

    private SpaceNode rootNode;
    private PositionProjection isometricProjection;

    private BackgroundObjects backgroundObjects;

    // Foreground objects and bodies
    private SpaceObjectBodySpace objectBodySpace;

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

        backgroundObjects = new BackgroundObjects(resources, rootNode, WIDTH, HEIGHT);
        objectBodySpace = new SpaceObjectBodySpace();

        mapProvider = new MapProvider(resources);

        SpaceNode mapNode = new SpaceNode();
        mapNode.addToNode(rootNode);
        mapController = new MapController(mapNode, objectBodySpace.getObjects(), mapProvider);

        SpaceNode playerNode = new SpaceNode();
        playerNode.localPosition.set(WIDTH * 0.4f, 0, 0);
        playerNode.addToNode(rootNode);

        playerObject = new SpaceObject(playerNode);

        playerObject.shape = new AnimatedSpriteShape(resources, R.drawable.weirdo_spritesheet, 32, 32, new int[]{1, 4});
        playerObject.shape.setPivot(new Point(playerObject.shape.getWidth() / 2, playerObject.shape.getHeight()));

        playerObject.setupBodyRelativeToShape(-0.18f, -0.1f, 0.18f, 0, true);
        playerObject.body.setController(new InputSpaceObjectBodyController(controls, playerObject));

        objectBodySpace.getObjects().add(playerObject);

        objectBodySpace.collectBodies();

        isometricProjection = new IsometricPositionProjection();
    }

    public void processInput(MotionEvent event, int screenWidth, int screenHeight) {
        controls.processInput(event, screenWidth, screenHeight);
    }

    public void update(double secondsPerFrame) {

        if (pan.getVelocity() == 0) {

            if (playerObject.spaceNode.localToGlobal().x > WIDTH * 0.75) {
                double panTo = pan.getValue() + WIDTH * 0.5;
                pan.transitionTo(panTo, 0.25);

                int chunkW = mapProvider.chunkWidth * mapProvider.objectWidth;
                int panIndex = (int) panTo / chunkW;
                if (panIndex - 1 > mapController.getCurrentChunkIndex()) {
                    mapController.advanceChunk(+1);
                    objectBodySpace.collectBodies();
                }
            }
            if (playerObject.spaceNode.localToGlobal().x < WIDTH * 0.25) {
                double panTo = pan.getValue() - WIDTH * 0.5;
                if (panTo >= 0) {
                    pan.transitionTo(panTo, 0.25);

                    int chunkW = mapProvider.chunkWidth * mapProvider.objectWidth;
                    int panIndex = (int) panTo / chunkW;
                    if (panIndex < mapController.getCurrentChunkIndex()) {
                        mapController.advanceChunk(-1);
                        objectBodySpace.collectBodies();
                    }
                }
            }
        }

        pan.update(secondsPerFrame);

        objectBodySpace.update(secondsPerFrame);
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
        rootNode.localPosition.set(-pan.getValue(), 0, 0);

        backgroundObjects.draw(canvas);

        Collections.sort(objectBodySpace.getObjects(), zComparator);
        Collections.reverse(objectBodySpace.getObjects());

        canvas.translate(0, GROUND_Y);

        for (SpaceObject spaceObject : objectBodySpace.getObjects()) {
            spaceObject.draw(canvas, isometricProjection);
        }
        if (SpaceObject.shouldDrawMarkers) {
            for (SpaceObject spaceObject : objectBodySpace.getObjects()) {
                spaceObject.drawMarkers(canvas);
            }
        }
    }

}
