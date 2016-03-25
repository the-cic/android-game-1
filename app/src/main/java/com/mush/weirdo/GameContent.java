package com.mush.weirdo;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mirko on 03/03/2016.
 */
public class GameContent {
    private static final int HORIZON_Y = 40;
    private static final int GROUND_Y = HORIZON_Y + 3;
    private static final int BOTTOM_Y = 50;

    private ArrayList<WorldObject> backgroundObjects;
    private ArrayList<WorldObject> worldObjects;
    private WorldObject player;
    private GameControls controls;
    private WorldObjectDistanceComparator comparator;

    private double panX;
    private double targetPanX;
    private double panSpeed;
    private int panDirection;

    public GameContent(Resources resources) {
        controls = new GameControls();
        comparator = new WorldObjectDistanceComparator();

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
        worldObjects.add(createGroundObject(resources, R.drawable.grass_near, BOTTOM_Y + 10));
        //worldObjects.add(createGroundObject(resources, R.drawable.water_near, BOTTOM_Y));

        WorldObject table = createGroundObject(resources, R.drawable.table, BOTTOM_Y + 15);
        table.setBounds(table.getSprite().getWidth(), table.getSprite().getHeight() * 0.2);
        worldObjects.add(table);

        SpriteShape wallShape = new ThreePartSpriteShape(resources, R.drawable.wall_left, R.drawable.wall_middle, R.drawable.wall_right, 2);
        WorldObject wall = new WorldObject(new Sprite(wallShape), 0, BOTTOM_Y + 30, FollowScreenPanEffect.INSTANCE, null);
        wall.setBounds(wallShape.getWidth(), wallShape.getHeight()*0.2);
        worldObjects.add(wall);

        wallShape = new ThreePartSpriteShape(resources, R.drawable.wall_left, R.drawable.wall_middle, R.drawable.wall_right, 5);
        wall = new WorldObject(new Sprite(wallShape), 0, BOTTOM_Y + 50, FollowScreenPanEffect.INSTANCE, null);
        wall.setBounds(wallShape.getWidth(), wallShape.getHeight()*0.2);
        worldObjects.add(wall);

        Sprite weirdo = new Sprite(new ImageSpriteShape(resources, R.drawable.weirdo, SpriteShapeAlignment.SSA_BOTTOM_LEFT));
        player = new WorldObject(weirdo, 0, 0, FollowScreenPanEffect.INSTANCE, new InputWorldObjectControl(controls));
        player.setX(GamePanel.WIDTH * 0.35);
        player.setY(GROUND_Y);
        worldObjects.add(player);

        panSpeed = 100;
        panDirection = 0;
        panX = 0;
        targetPanX = 0;
    }

    public void processInput(MotionEvent event, int screenWidth, int screenHeight) {
        controls.processInput(event, screenWidth, screenHeight);
    }

    public void update(double secondsPerFrame) {
        if (panDirection == 0) {
            if (player.getSprite().getX() > GamePanel.WIDTH * 0.75) {
                targetPanX += GamePanel.WIDTH * 0.45;
                panDirection = 1;
            }
            if (player.getSprite().getX() < GamePanel.WIDTH * 0.25) {
                targetPanX -= GamePanel.WIDTH * 0.45;
                panDirection = -1;
                if (targetPanX < 0) {
                    targetPanX = 0;
                    panDirection = 0;
                }
            }
        } else {
            if (panDirection == 1) {
                if (panX < targetPanX) {
                    panX += panSpeed * secondsPerFrame;
                } else {
                    panDirection = 0;
                }
            } else
            if (panDirection == -1) {
                if (panX > targetPanX) {
                    panX -= panSpeed * secondsPerFrame;
                } else {
                    panDirection = 0;
                }
            }
        }
        if (panX < 0) {
            panX = 0;
        }

        for (WorldObject worldObject : backgroundObjects) {
            if (worldObject.getSprite().getX() < -worldObject.getSprite().getWidth()) {
                ScreenPanEffect panEffect = worldObject.getPanEffect();
                if (panEffect instanceof ParallaxScreenPanEffect) {
                    double factor = ((ParallaxScreenPanEffect)panEffect).getFactor();
                    worldObject.setX(worldObject.getX() + ((worldObject.getSprite().getWidth() + GamePanel.WIDTH * (1 + Math.random()*0.2)) / factor));
                }
            }
            worldObject.applyScreenPan(panX, 0);
        }

        for (WorldObject worldObject : worldObjects) {
            if (worldObject.getSprite().getX() < -worldObject.getSprite().getWidth()) {
                worldObject.setX(worldObject.getX() + ((worldObject.getSprite().getWidth() + GamePanel.WIDTH * (1 + Math.random()))));
            }
            worldObject.applyScreenPan(panX, 0);
        }

        player.update(secondsPerFrame, worldObjects);

        if (player.getX() < 0) {
            player.setX(0);
        }
        if (player.getY() < GROUND_Y) {
            player.setY(GROUND_Y);
        }
        if (player.getY() > BOTTOM_Y*2) {
            player.setY(BOTTOM_Y*2);
        }
        player.applyScreenPan(panX, 0);
    }

    public void draw(Canvas canvas) {

        for (WorldObject worldObject : backgroundObjects) {
            worldObject.getSprite().draw(canvas);
        }

        Collections.sort(worldObjects, comparator);

        for (WorldObject worldObject : worldObjects) {
            worldObject.getSprite().draw(canvas);
        }
    }

    private WorldObject createFixedHorizonBackground(Resources resources, int resourceId) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId, SpriteShapeAlignment.SSA_TOP_LEFT);

        Sprite sprite = new Sprite(shape);

        return new WorldObject(sprite, 0, 0, null, null);
    }

    private WorldObject createRepeatingGroundBackground(Resources resources, int resourceId, int offset) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId, SpriteShapeAlignment.SSA_TOP_LEFT);

        Sprite sprite = new Sprite(shape);

        return new WorldObject(sprite, offset * GamePanel.WIDTH, GROUND_Y, CycleScreenPanEffect.INSTANCE, null);
    }

    private WorldObject createHorizonObject(Resources resources, int resourceId, double factor) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId, SpriteShapeAlignment.SSA_BOTTOM_LEFT);

        Sprite sprite = new Sprite(shape);

        double firstX = ((Math.random() * 0.5 + 0.2) * GamePanel.WIDTH) / factor;

        return new WorldObject(sprite, firstX, HORIZON_Y, new ParallaxScreenPanEffect(factor), null);
    }

    private WorldObject createFloatingHorizonObject(Resources resources, int resourceId, double factor, double y) {
        WorldObject object = createHorizonObject(resources, resourceId, factor);
        object.setY(y);
        return object;
    }

    private WorldObject createGroundObject(Resources resources, int resourceId, int y) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId, SpriteShapeAlignment.SSA_BOTTOM_LEFT);

        Sprite sprite = new Sprite(shape);

        return new WorldObject(sprite, 0, y, FollowScreenPanEffect.INSTANCE, null);
    }

}
