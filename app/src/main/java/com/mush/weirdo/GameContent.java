package com.mush.weirdo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by mirko on 03/03/2016.
 */
public class GameContent {
    private static final int HORIZON_Y = 40;
    private static final int GROUND_Y = HORIZON_Y + 3;
    private static final int BOTTOM_Y = 50;

    private Bitmap horizonBackground;
    private Bitmap groundBackground;
//    ArrayList<BackgroundObject> horizonObjects;
    ArrayList<WorldObject> backgroundObjects;
    ArrayList<WorldObject> groundObjects;
    Sprite weirdo;
//    private SpriteFrames grassTiles;

    private double x, y, dx;
    private double jenU;
    private double jenV;
    private int jenX;
    private int jenY;
    private int jenOffset;

    public GameContent(Resources resources) {
        horizonBackground = BitmapFactory.decodeResource(resources, R.drawable.horizon);
        groundBackground = BitmapFactory.decodeResource(resources, R.drawable.ground);

        backgroundObjects = new ArrayList<>();
        backgroundObjects.add(createHorizonObject(resources, R.drawable.mountains_far, 0.1));
        backgroundObjects.add(createHorizonObject(resources, R.drawable.mountains_mid_far, 0.15));
        backgroundObjects.add(createHorizonObject(resources, R.drawable.mountains_mid_near, 0.2));
        backgroundObjects.add(createFloatingHorizonObject(resources, R.drawable.cloud, 0.3, HORIZON_Y * 0.5));
        backgroundObjects.add(createFloatingHorizonObject(resources, R.drawable.cloud, 0.35, HORIZON_Y * 0.4));
        backgroundObjects.add(createHorizonObject(resources, R.drawable.mountains_near, 0.4));
        backgroundObjects.add(createHorizonObject(resources, R.drawable.trees_far, 0.5));

        groundObjects = new ArrayList<>();
        groundObjects.add(createGroundObject(resources, R.drawable.hill_near, SpriteShape.ALIGN_LEFT | SpriteShape.ALIGN_BOTTOM, GROUND_Y));
        groundObjects.add(createGroundObject(resources, R.drawable.grass_near, SpriteShape.ALIGN_LEFT | SpriteShape.ALIGN_BOTTOM, GROUND_Y));
        groundObjects.add(createGroundObject(resources, R.drawable.water_near, SpriteShape.ALIGN_LEFT | SpriteShape.ALIGN_BOTTOM, BOTTOM_Y));

        SpriteShape wall = new ExtendableSpriteShape(resources, R.drawable.wall_left, R.drawable.wall_middle, R.drawable.wall_right, 2);
        wall.setAlignment(SpriteShape.ALIGN_LEFT | SpriteShape.ALIGN_BOTTOM);
        groundObjects.add(new WorldObject(new Sprite(wall), 0, BOTTOM_Y + 30));

        wall = new ExtendableSpriteShape(resources, R.drawable.wall_left, R.drawable.wall_middle, R.drawable.wall_right, 5);
        wall.setAlignment(SpriteShape.ALIGN_LEFT | SpriteShape.ALIGN_BOTTOM);
        groundObjects.add(new WorldObject(new Sprite(wall), 0, BOTTOM_Y + 50));

//        horizonObjects = new ArrayList<>();
//        horizonObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.mountains_far), 0.1, HORIZON_Y));
//        horizonObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.mountains_mid_far), 0.15, HORIZON_Y));
//        horizonObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.mountains_mid_near), 0.2, HORIZON_Y));
//        horizonObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.cloud), 0.3, HORIZON_Y * 0.5));
//        horizonObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.cloud), 0.35, HORIZON_Y * 0.4));
//        horizonObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.mountains_near), 0.4, HORIZON_Y));
//        horizonObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.trees_far), 0.5, HORIZON_Y));
//        horizonObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.hill_near), 1, GROUND_Y));
//        horizonObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.grass_near), 1, GROUND_Y));
//        horizonObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.water_near), 1, BOTTOM_Y));
//
//        horizonObjects.add(new ExtendableObject(
//                BitmapFactory.decodeResource(resources, R.drawable.wall_left),
//                BitmapFactory.decodeResource(resources, R.drawable.wall_middle),
//                BitmapFactory.decodeResource(resources, R.drawable.wall_right),
//                2, 1, BOTTOM_Y + 30));
//
//        horizonObjects.add(new ExtendableObject(
//                BitmapFactory.decodeResource(resources, R.drawable.wall_left),
//                BitmapFactory.decodeResource(resources, R.drawable.wall_middle),
//                BitmapFactory.decodeResource(resources, R.drawable.wall_right),
//                5, 1, BOTTOM_Y + 50));
//
//        for (BackgroundObject bgObject : horizonObjects) {
//            bgObject.setX((int) ((Math.random() * 0.5 + 0.2) * GamePanel.WIDTH));
//        }

        for (WorldObject object : backgroundObjects) {
            object.setX(((Math.random() * 0.5 + 0.2) * GamePanel.WIDTH) / ((ParallaxWorldObject)object).getFactor());
        }

//        grassTiles = new SpriteFrames(BitmapFactory.decodeResource(resources, R.drawable.grass), 20, 10, 6);

        weirdo = new Sprite(new ImageSpriteShape(BitmapFactory.decodeResource(resources, R.drawable.weirdo)));

        jenOffset = 5;

        setVector(30);
    }

    public void processInput(MotionEvent event) {

    }

    public void update(double secondsPerFrame) {
        x += dx * secondsPerFrame;
        //y += dx * secondsPerFrame * 0.05;
        if (x > GamePanel.WIDTH) {
            //x = 0;
        }

//        for (BackgroundObject bgObject : horizonObjects) {
//            double objectScreenX = bgObject.getScreenX(x);
//            if (objectScreenX < -bgObject.getWidth()) {
//                bgObject.setX(bgObject.getX() + (bgObject.getWidth() + (GamePanel.WIDTH * (1 + Math.random()))) / bgObject.factor);
//            }
//        }

        for (WorldObject worldObject : backgroundObjects) {
            worldObject.applyScreenPosition(x, y);
            if (worldObject.getSprite().getX() < -worldObject.getSprite().getWidth()) {
                worldObject.setX(worldObject.getX() + ((worldObject.getSprite().getWidth() + GamePanel.WIDTH * (1 + Math.random()*0.2)) / ((ParallaxWorldObject)worldObject).getFactor()));
            }
        }

        for (WorldObject worldObject : groundObjects) {
            worldObject.applyScreenPosition(x, y);
            if (worldObject.getSprite().getX() < -worldObject.getSprite().getWidth()) {
                worldObject.setX(worldObject.getX() + ((worldObject.getSprite().getWidth() + GamePanel.WIDTH * (1 + Math.random()))));
            }
        }

        jenU += 3.3 * secondsPerFrame;
        jenV += 11.1 * secondsPerFrame;
        jenX = (int) (Math.sin(jenU) * jenOffset);
        jenY = (int) -Math.abs((Math.sin(jenV) * jenOffset));

        weirdo.setPosition(GamePanel.WIDTH / 4 + jenX, GROUND_Y + jenY - weirdo.getHeight());
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(horizonBackground, (int) 0, (int) 0, null);
        canvas.drawBitmap(groundBackground, (float) (-x % GamePanel.WIDTH), GROUND_Y, null);
        canvas.drawBitmap(groundBackground, (float) (-x % GamePanel.WIDTH + GamePanel.WIDTH), GROUND_Y, null);

        for (WorldObject worldObject : backgroundObjects) {
            worldObject.getSprite().draw(canvas);
        }

        for (WorldObject worldObject : groundObjects) {
            worldObject.getSprite().draw(canvas);
        }

//        for (BackgroundObject object : horizonObjects) {
            //object.draw(canvas, x);
//            canvas.drawBitmap(object.image,
//                    (float) object.getScreenX(x),
//                    (float) object.getScreenY(),
//                    null);
//        }

        /*
        if ((System.currentTimeMillis() / 1000) % 10 < 10) {
            float lineY = GROUND_Y;

            for (int j = 0; j < 8; j++) {
                float v = j + (float)(y % 1);
                float h = 0;
                int savedState = 0;
                if (j <= 3) {
                    float s = ((float)v  / 4f);
                    h = grassTiles.getHeight() * s;
                    savedState = canvas.save();
                    canvas.scale(1f, s, 0, lineY);
                }
                for (int i = 0; i < 10; i++) {
                    canvas.drawBitmap(grassTiles.getFrame(0),
                            i * grassTiles.getWidth() + (float)(-x % 20),
                            lineY,
                            null);
                }
                if (j <= 3) {
                    canvas.restoreToCount(savedState);
                    lineY += h;
                } else {
                    lineY += grassTiles.getHeight();
                }
            }
        }*/

//        final int savedState = canvas.save();
//        canvas.scale(0.15f, 0.15f);
//        canvas.drawBitmap(jen, GamePanel.WIDTH / 4 + jenX, GROUND_Y + jenY - jen.getHeight(), null);
        weirdo.draw(canvas);
//        canvas.restoreToCount(savedState);
    }

    public void setVector(int dx) {
        this.dx = dx;
    }

    private WorldObject createHorizonObject(Resources resources, int resourceId, double factor) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId);
        shape.setAlignment(SpriteShape.ALIGN_LEFT | SpriteShape.ALIGN_BOTTOM);

        Sprite sprite = new Sprite(shape);

        ParallaxWorldObject worldObject = new ParallaxWorldObject(sprite, 0, HORIZON_Y, factor);

        return worldObject;
    }

    private WorldObject createFloatingHorizonObject(Resources resources, int resourceId, double factor, double y) {
        WorldObject object = createHorizonObject(resources, resourceId, factor);
        object.setY(y);
        return object;
    }

    private WorldObject createGroundObject(Resources resources, int resourceId, int align, int y) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId);
        shape.setAlignment(align);

        Sprite sprite = new Sprite(shape);

        WorldObject worldObject = new WorldObject(sprite, 0, y);
        return worldObject;
    }

}
