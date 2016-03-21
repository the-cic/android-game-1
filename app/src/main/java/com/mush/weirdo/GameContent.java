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
    WorldObject player;
    GameControls controls;

    private double panX;
    private double targetPanX;
    private double panSpeed;
    private int panDirection;
//    private double jenU;
//    private double jenV;
//    private int jenX;
//    private int jenY;
//    private double jenY0 = 0;
//    private int jenOffset;

    public GameContent(Resources resources) {
        controls = new GameControls();

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
        worldObjects.add(new WorldObject(new Sprite(wall), 0, BOTTOM_Y + 30, FollowScreenPanEffect.INSTANCE, null));

        wall = new ThreePartSpriteShape(resources, R.drawable.wall_left, R.drawable.wall_middle, R.drawable.wall_right, 5);
        worldObjects.add(new WorldObject(new Sprite(wall), 0, BOTTOM_Y + 50, FollowScreenPanEffect.INSTANCE, null));

        Sprite weirdo = new Sprite(new ImageSpriteShape(resources, R.drawable.weirdo, SpriteShapeAlignment.SSA_BOTTOM_LEFT));
        player = new WorldObject(weirdo, 0, 0, FollowScreenPanEffect.INSTANCE, new InputWorldObjectControl(controls));
        player.setX(GamePanel.WIDTH * 0.35);
        player.setY(GROUND_Y);

//        jenOffset = 5;

//        setVector(30);
        panSpeed = 100;
        panDirection = 0;
        panX = 0;
        targetPanX = 0;
    }

    public void processInput(MotionEvent event, int screenWidth, int screenHeight) {
        controls.processInput(event, screenWidth, screenHeight);
//        System.out.println("direction: " + controls.getDirection());
    }

    public void update(double secondsPerFrame) {
        //x += dx * secondsPerFrame * controls.getHorizontalDirection();
//        jenY0 -= dx * secondsPerFrame * controls.getVerticalDirection();
        if (panDirection == 0) {
            if (player.getSprite().getX() > GamePanel.WIDTH * 0.75) {
//            pan += dx * secondsPerFrame;
                targetPanX += GamePanel.WIDTH * 0.45;
                panDirection = 1;
            }
            if (player.getSprite().getX() < GamePanel.WIDTH * 0.25) {
//            x -= dx * secondsPerFrame;
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
//        if (jenY0 < 0) {
//            jenY0 = 0;
//        }
//        if (jenY0 > BOTTOM_Y*2 - GROUND_Y) {
//            jenY0 = BOTTOM_Y*2 - GROUND_Y;
//        }

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

//        jenU += 3.3 * secondsPerFrame;
//        jenV += 11.1 * secondsPerFrame;
//        jenX = (int) (Math.sin(jenU) * jenOffset);
//        jenY = (int) -Math.abs((Math.sin(jenV) * jenOffset));
//
//        player.setX(x + GamePanel.WIDTH / 4 + jenX);
//        player.setY(GROUND_Y + jenY + jenY0);
//        player.applyScreenPan(x, y);

        player.update(secondsPerFrame);

//        player.setX(player.getX() + controls.getHorizontalDirection() * secondsPerFrame * dx);
//        player.setY(player.getY() - controls.getVerticalDirection() * secondsPerFrame * dx);
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

        for (WorldObject worldObject : worldObjects) {
            worldObject.getSprite().draw(canvas);
        }

        player.getSprite().draw(canvas);
    }

//    public void setVector(int dx) {
//        this.dx = dx;
//    }

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
