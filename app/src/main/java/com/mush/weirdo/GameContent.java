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
    ArrayList<WorldObject> backgroundObjects;
    ArrayList<WorldObject> groundObjects;
    Sprite weirdo;

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
        groundObjects.add(createGroundObject(resources, R.drawable.hill_near, GROUND_Y));
        groundObjects.add(createGroundObject(resources, R.drawable.grass_near, GROUND_Y));
        groundObjects.add(createGroundObject(resources, R.drawable.water_near, BOTTOM_Y));

        SpriteShape wall = new ExtendableSpriteShape(resources, R.drawable.wall_left, R.drawable.wall_middle, R.drawable.wall_right, 2);
        wall.setAlignment(SpriteShape.ALIGN_LEFT | SpriteShape.ALIGN_BOTTOM);
        groundObjects.add(new WorldObject(new Sprite(wall), 0, BOTTOM_Y + 30));

        wall = new ExtendableSpriteShape(resources, R.drawable.wall_left, R.drawable.wall_middle, R.drawable.wall_right, 5);
        wall.setAlignment(SpriteShape.ALIGN_LEFT | SpriteShape.ALIGN_BOTTOM);
        groundObjects.add(new WorldObject(new Sprite(wall), 0, BOTTOM_Y + 50));

        for (WorldObject object : backgroundObjects) {
            object.setX(((Math.random() * 0.5 + 0.2) * GamePanel.WIDTH) / ((ParallaxWorldObject)object).getFactor());
        }

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

        weirdo.draw(canvas);
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

    private WorldObject createGroundObject(Resources resources, int resourceId, int y) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId);
        shape.setAlignment(SpriteShape.ALIGN_LEFT | SpriteShape.ALIGN_BOTTOM);

        Sprite sprite = new Sprite(shape);

        WorldObject worldObject = new WorldObject(sprite, 0, y);
        return worldObject;
    }

}
