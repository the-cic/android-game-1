package com.mush.weirdo;

import android.content.res.Resources;
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

    ArrayList<WorldObject> backgroundObjects;
    ArrayList<WorldObject> worldObjects;
    Sprite weirdo;
    GameControls controls;

    private double x, y, dx;
    private double jenU;
    private double jenV;
    private int jenX;
    private int jenY;
    private double jenY0 = 0;
    private int jenOffset;

    public GameContent(Resources resources) {

        backgroundObjects = new ArrayList<>();
        backgroundObjects.add(createFixedHorizonBackground(resources, R.drawable.horizon));

        backgroundObjects.add(createHorizonObject(resources, R.drawable.mountains_far, 0.1));
        backgroundObjects.add(createHorizonObject(resources, R.drawable.mountains_mid_far, 0.15));
        backgroundObjects.add(createHorizonObject(resources, R.drawable.mountains_mid_near, 0.2));
        backgroundObjects.add(createFloatingHorizonObject(resources, R.drawable.cloud, 0.3, HORIZON_Y * 0.5));
        backgroundObjects.add(createFloatingHorizonObject(resources, R.drawable.cloud, 0.35, HORIZON_Y * 0.4));
        backgroundObjects.add(createHorizonObject(resources, R.drawable.mountains_near, 0.4));
        backgroundObjects.add(createHorizonObject(resources, R.drawable.trees_far, 0.5));

        backgroundObjects.add(createRepeatingGroundBackground(resources, R.drawable.ground, 0));
        backgroundObjects.add(createRepeatingGroundBackground(resources, R.drawable.ground, 1));

        worldObjects = new ArrayList<>();
        worldObjects.add(createGroundObject(resources, R.drawable.hill_near, GROUND_Y));
        worldObjects.add(createGroundObject(resources, R.drawable.grass_near, GROUND_Y));
        worldObjects.add(createGroundObject(resources, R.drawable.water_near, BOTTOM_Y));

        SpriteShape wall = new ThreePartSpriteShape(resources, R.drawable.wall_left, R.drawable.wall_middle, R.drawable.wall_right, 2);
        worldObjects.add(new WorldObject(new Sprite(wall), 0, BOTTOM_Y + 30, FollowScreenPanEffect.INSTANCE));

        wall = new ThreePartSpriteShape(resources, R.drawable.wall_left, R.drawable.wall_middle, R.drawable.wall_right, 5);
        worldObjects.add(new WorldObject(new Sprite(wall), 0, BOTTOM_Y + 50, FollowScreenPanEffect.INSTANCE));

        weirdo = new Sprite(new ImageSpriteShape(resources, R.drawable.weirdo, SpriteShapeAlignment.SSA_BOTTOM_LEFT));

        jenOffset = 5;

        setVector(30);
        controls = new GameControls();
    }

    public void processInput(MotionEvent event, int screenWidth, int screenHeight) {
        controls.processInput(event, screenWidth, screenHeight);
//        System.out.println("direction: " + controls.getDirection());
    }

    public void update(double secondsPerFrame) {
        x += dx * secondsPerFrame * controls.getHorizontalDirection();
        jenY0 -= dx * secondsPerFrame * controls.getVerticalDirection();
        if (x < 0) {
            x = 0;
        }
        if (jenY0 < 0) {
            jenY0 = 0;
        }
        if (jenY0 > BOTTOM_Y*2 - GROUND_Y) {
            jenY0 = BOTTOM_Y*2 - GROUND_Y;
        }

        for (WorldObject worldObject : backgroundObjects) {
            if (worldObject.getSprite().getX() < -worldObject.getSprite().getWidth()) {
                ScreenPanEffect panEffect = worldObject.getPanEffect();
                if (panEffect instanceof ParallaxScreenPanEffect) {
                    double factor = ((ParallaxScreenPanEffect)panEffect).getFactor();
                    worldObject.setX(worldObject.getX() + ((worldObject.getSprite().getWidth() + GamePanel.WIDTH * (1 + Math.random()*0.2)) / factor));
                }
            }
            worldObject.applyScreenPan(x, y);
        }

        for (WorldObject worldObject : worldObjects) {
            if (worldObject.getSprite().getX() < -worldObject.getSprite().getWidth()) {
                worldObject.setX(worldObject.getX() + ((worldObject.getSprite().getWidth() + GamePanel.WIDTH * (1 + Math.random()))));
            }
            worldObject.applyScreenPan(x, y);
        }

        jenU += 3.3 * secondsPerFrame;
        jenV += 11.1 * secondsPerFrame;
        jenX = (int) (Math.sin(jenU) * jenOffset);
        jenY = (int) -Math.abs((Math.sin(jenV) * jenOffset));

        weirdo.setPosition(GamePanel.WIDTH / 4 + jenX, GROUND_Y + jenY + jenY0);
    }

    public void draw(Canvas canvas) {

        for (WorldObject worldObject : backgroundObjects) {
            worldObject.getSprite().draw(canvas);
        }

        for (WorldObject worldObject : worldObjects) {
            worldObject.getSprite().draw(canvas);
        }

        weirdo.draw(canvas);
    }

    public void setVector(int dx) {
        this.dx = dx;
    }

    private WorldObject createFixedHorizonBackground(Resources resources, int resourceId) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId, SpriteShapeAlignment.SSA_TOP_LEFT);

        Sprite sprite = new Sprite(shape);

        return new WorldObject(sprite, 0, 0, null);
    }

    private WorldObject createRepeatingGroundBackground(Resources resources, int resourceId, int offset) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId, SpriteShapeAlignment.SSA_TOP_LEFT);

        Sprite sprite = new Sprite(shape);

        return new WorldObject(sprite, offset * GamePanel.WIDTH, GROUND_Y, CycleScreenPanEffect.INSTANCE);
    }

    private WorldObject createHorizonObject(Resources resources, int resourceId, double factor) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId, SpriteShapeAlignment.SSA_BOTTOM_LEFT);

        Sprite sprite = new Sprite(shape);

        double firstX = ((Math.random() * 0.5 + 0.2) * GamePanel.WIDTH) / factor;

        return new WorldObject(sprite, firstX, HORIZON_Y, new ParallaxScreenPanEffect(factor));
    }

    private WorldObject createFloatingHorizonObject(Resources resources, int resourceId, double factor, double y) {
        WorldObject object = createHorizonObject(resources, resourceId, factor);
        object.setY(y);
        return object;
    }

    private WorldObject createGroundObject(Resources resources, int resourceId, int y) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId, SpriteShapeAlignment.SSA_BOTTOM_LEFT);

        Sprite sprite = new Sprite(shape);

        return new WorldObject(sprite, 0, y, FollowScreenPanEffect.INSTANCE);
    }

}
