package com.mush.weirdo;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.mush.weirdo.screenpan.CycleScreenPanEffect;
import com.mush.weirdo.screenpan.FollowScreenPanEffect;
import com.mush.weirdo.screenpan.ParallaxScreenPanEffect;
import com.mush.weirdo.screenpan.ScreenPanEffect;
import com.mush.weirdo.sprites.AnimatedSpriteShape;
import com.mush.weirdo.sprites.ImageSpriteShape;
import com.mush.weirdo.sprites.Sprite;
import com.mush.weirdo.sprites.SpriteShape;
import com.mush.weirdo.sprites.ThreePartSpriteShape;
import com.mush.weirdo.util.AnimatedValue;
import com.mush.weirdo.world.ParallaxPositionProjection;
import com.mush.weirdo.worldobjectcontrol.InputWorldObjectControl;

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
    private static final int BOTTOM_Y = 50;

    private WorldObjectRepository objectRepository;
    private WorldObject player;
    private GameControls controls;
    private WorldObjectDistanceComparator comparator;

    private AnimatedValue pan;

    private Paint paint;
    private Paint paint2;

    public GameContent(Resources resources) {
        controls = new GameControls();
        comparator = new WorldObjectDistanceComparator();
        paint = new Paint();

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        paint2 = new Paint();

        paint2.setColor(Color.GREEN);
        paint2.setStyle(Paint.Style.STROKE);

        objectRepository = new WorldObjectRepository();

        objectRepository.addBackground(createFixedHorizonBackground(resources, R.drawable.horizon));

        objectRepository.addBackground(createHorizonObject(resources, R.drawable.mountains_far, 0.1));
        objectRepository.addBackground(createHorizonObject(resources, R.drawable.mountains_mid_far, 0.15));
        objectRepository.addBackground(createHorizonObject(resources, R.drawable.mountains_mid_near, 0.2));
        objectRepository.addBackground(createFloatingHorizonObject(resources, R.drawable.cloud, 0.3, HORIZON_Y * 0.5));
        objectRepository.addBackground(createFloatingHorizonObject(resources, R.drawable.cloud, 0.35, HORIZON_Y * 0.4));
        objectRepository.addBackground(createHorizonObject(resources, R.drawable.mountains_near, 0.4));
        objectRepository.addBackground(createHorizonObject(resources, R.drawable.trees_far, 0.5));

        objectRepository.addBackground(createRepeatingGroundBackground(resources, R.drawable.ground, 0));
        objectRepository.addBackground(createRepeatingGroundBackground(resources, R.drawable.ground, 1));

        objectRepository.add(createGroundObject(resources, R.drawable.hill_near, GROUND_Y));
        objectRepository.add(createGroundObject(resources, R.drawable.grass_near, GROUND_Y));
        objectRepository.add(createGroundObject(resources, R.drawable.grass_near, BOTTOM_Y + 10));
        //worldObjects.add(createGroundObject(resources, R.drawable.water_near, BOTTOM_Y));

        WorldObject table = createGroundObject(resources, R.drawable.table, BOTTOM_Y + 15);
        table.setBounds(0, (int) (-table.getSprite().getHeight() * 0.2), table.getSprite().getWidth(), 0);
        objectRepository.add(table);

        SpriteShape wallShape = new ThreePartSpriteShape(resources, R.drawable.wall_left, R.drawable.wall_middle, R.drawable.wall_right, 2);
        WorldObject wall = new WorldObject(new Sprite(wallShape), 0, BOTTOM_Y + 30, FollowScreenPanEffect.INSTANCE, null);
        wall.setBounds(0, (int) (-wallShape.getHeight() * 0.2), wallShape.getWidth(), 0);
        objectRepository.add(wall);

        wallShape = new ThreePartSpriteShape(resources, R.drawable.wall_left, R.drawable.wall_middle, R.drawable.wall_right, 5);
        wall = new WorldObject(new Sprite(wallShape), 0, BOTTOM_Y + 50, FollowScreenPanEffect.INSTANCE, null);
        wall.setBounds(0, (int) (-wallShape.getHeight() * 0.2), wallShape.getWidth(), 0);
        objectRepository.add(wall);

        //Sprite weirdo = new Sprite(new ImageSpriteShape(resources, R.drawable.weirdo_stand));
        Sprite weirdo = new Sprite(new AnimatedSpriteShape(resources, R.drawable.weirdo_spritesheet, 32, 32, new int[]{1, 4}));
//        Sprite weirdo = new Sprite(new ImageSpriteShape(resources, R.drawable.weirdo));
        weirdo.getShape().setPivot(new Point(weirdo.getShape().getWidth() / 2, weirdo.getHeight()));
        player = new WorldObject(weirdo, 0, 0, FollowScreenPanEffect.INSTANCE, new InputWorldObjectControl(controls));
        player.setBounds((int) (-weirdo.getWidth() * 0.2), -(int) (weirdo.getHeight() * 0.1), (int) (weirdo.getWidth() * 0.2), 0);
        player.setX(WIDTH * 0.35);
        player.setY(GROUND_Y);
        player.setVelocity(new PointF());
        player.setObjectRepository(this.objectRepository);
        objectRepository.add(player);

        pan = new AnimatedValue(0);
    }

    public void processInput(MotionEvent event, int screenWidth, int screenHeight) {
        controls.processInput(event, screenWidth, screenHeight);
    }

    public void update(double secondsPerFrame) {
        if (pan.getVelocity() == 0) {
            if (player.getSprite().getX() > WIDTH * 0.75) {
                pan.transitionTo(pan.getValue() + WIDTH * 0.45, 1);
            }
            if (player.getSprite().getX() < WIDTH * 0.25) {
                double panTo = pan.getValue() - WIDTH * 0.45;
                if (panTo < 0) {
                    panTo = 0;
                }
                pan.transitionTo(panTo, 3);
            }
        }
        pan.update(secondsPerFrame);


        for (WorldObject worldObject : this.objectRepository.getBackgrounds()) {
            if (worldObject.getSprite().getX() < -worldObject.getSprite().getWidth()) {
                ScreenPanEffect panEffect = worldObject.getPanEffect();
                if (panEffect instanceof ParallaxScreenPanEffect) {
                    double factor = ((ParallaxScreenPanEffect)panEffect).getFactor();
                    worldObject.setX(worldObject.getX() + ((worldObject.getSprite().getWidth() + WIDTH * (1 + Math.random()*0.2)) / factor));
                }
            }
            worldObject.applyScreenPan(pan.getValue(), 0);
        }

        for (WorldObject worldObject : this.objectRepository.getObjects()) {
            if (worldObject.getSprite().getX() < -worldObject.getSprite().getWidth()) {
                worldObject.setX(worldObject.getX() + ((worldObject.getSprite().getWidth() + WIDTH * (1 + Math.random()))));
            }
            worldObject.applyScreenPan(pan.getValue(), 0);
        }

        player.update(secondsPerFrame);
        player.applyUpdate();

        if (player.getX() < 0) {
            player.setX(0);
        }
        if (player.getY() < GROUND_Y) {
            player.setY(GROUND_Y);
        }
        if (player.getY() > BOTTOM_Y*2) {
            player.setY(BOTTOM_Y*2);
        }
        player.applyScreenPan(pan.getValue(), 0);
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

        this.draw(canvas);

        canvas.restoreToCount(savedState);
    }

    private void draw(Canvas canvas) {

        for (WorldObject worldObject : this.objectRepository.getBackgrounds()) {
            worldObject.getSprite().draw(canvas);
        }

        ArrayList<WorldObject> objects = this.objectRepository.getObjects();

        Collections.sort(objects, comparator);

        for (WorldObject worldObject : objects) {
            worldObject.getSprite().draw(canvas);

            if (worldObject.getBounds() != null) {
                Rect bounds = worldObject.getBounds();
                canvas.drawRect(
                        (float)(worldObject.getSprite().getX() + bounds.left),
                        (float)(worldObject.getSprite().getY() + bounds.top),
                        (float)(worldObject.getSprite().getX() + bounds.right),
                        (float)(worldObject.getSprite().getY() + bounds.bottom),
                        paint);
            }
        }

        ParallaxPositionProjection proj = new ParallaxPositionProjection(5);

        PointF p = new PointF();
        for (int i=0; i < 10; i++) {
            for (int j=0; j < 10; j++) {
                float u = 10 + i * 3;
                float v = 10 + j * 3;
                canvas.drawRect(u, v, u+1, v+1, paint);


                proj.transform(u-10, 10, v-10, p);
                canvas.drawRect(100+p.x, 20 + p.y, 100+p.x+1, 20+p.y+1, paint2);
            }
        }
    }

    private WorldObject createFixedHorizonBackground(Resources resources, int resourceId) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId);

        Sprite sprite = new Sprite(shape);

        return new WorldObject(sprite, 0, 0, null, null);
    }

    private WorldObject createRepeatingGroundBackground(Resources resources, int resourceId, int offset) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId);

        Sprite sprite = new Sprite(shape);

        return new WorldObject(sprite, offset * WIDTH, GROUND_Y, CycleScreenPanEffect.INSTANCE, null);
    }

    private WorldObject createHorizonObject(Resources resources, int resourceId, double factor) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId);
        shape.setPivot(new Point(0, shape.getHeight()));

        Sprite sprite = new Sprite(shape);

        double firstX = ((Math.random() * 0.5 + 0.2) * WIDTH) / factor;

        return new WorldObject(sprite, firstX, HORIZON_Y, new ParallaxScreenPanEffect(factor), null);
    }

    private WorldObject createFloatingHorizonObject(Resources resources, int resourceId, double factor, double y) {
        WorldObject object = createHorizonObject(resources, resourceId, factor);
        object.setY(y);
        return object;
    }

    private WorldObject createGroundObject(Resources resources, int resourceId, int y) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId);
        shape.setPivot(new Point(0, shape.getHeight()));

        Sprite sprite = new Sprite(shape);

        return new WorldObject(sprite, 0, y, FollowScreenPanEffect.INSTANCE, null);
    }

}
