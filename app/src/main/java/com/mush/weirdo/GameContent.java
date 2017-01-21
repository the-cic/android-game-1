package com.mush.weirdo;

import android.content.res.Resources;
import android.graphics.Bitmap;
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
import com.mush.weirdo.util.SpaceObjectZComparator;
import com.mush.weirdo.world.IsometricPositionProjection;
import com.mush.weirdo.world.ParallaxPositionProjection;
import com.mush.weirdo.world.Point3F;
import com.mush.weirdo.world.PositionProjection;
import com.mush.weirdo.world.SpaceNode;
import com.mush.weirdo.world.SpaceObject;
import com.mush.weirdo.world.InputSpaceObjectBodyController;
import com.mush.weirdo.world.TilingPositionProjection;
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

    private SpaceNode rootNode;
    private PositionProjection parallaxProjection;
    private PositionProjection tileProjection;
    private PositionProjection isometricProjection;

    private ArrayList<SpaceObject> fixedBgObjects;
    private ArrayList<SpaceObject> tilingBgObjects;
    private ArrayList<SpaceObject> parallaxObjects;
    private ArrayList<SpaceObject> foregroundObjects;
    private SpaceObject playerObject;
    private SpaceObjectZComparator zComparator;

    private AnimatedValue pan;

    private Paint paint;
    private Paint paint2;

    public GameContent(Resources resources) {
        controls = new GameControls();
        comparator = new WorldObjectDistanceComparator();
        zComparator = new SpaceObjectZComparator();
        paint = new Paint();

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        paint2 = new Paint();

        paint2.setColor(Color.GREEN);
        paint2.setStyle(Paint.Style.STROKE);

        objectRepository = new WorldObjectRepository();

//        objectRepository.addBackground(createFixedHorizonBackground(resources, R.drawable.horizon));

//        objectRepository.addBackground(createHorizonObject(resources, R.drawable.mountains_far, 0.1));
//        objectRepository.addBackground(createHorizonObject(resources, R.drawable.mountains_mid_far, 0.15));
//        objectRepository.addBackground(createHorizonObject(resources, R.drawable.mountains_mid_near, 0.2));
//        objectRepository.addBackground(createFloatingHorizonObject(resources, R.drawable.cloud, 0.3, HORIZON_Y * 0.5));
//        objectRepository.addBackground(createFloatingHorizonObject(resources, R.drawable.cloud, 0.35, HORIZON_Y * 0.4));
//        objectRepository.addBackground(createHorizonObject(resources, R.drawable.mountains_near, 0.4));
//        objectRepository.addBackground(createHorizonObject(resources, R.drawable.trees_far, 0.5));

//        objectRepository.addBackground(createRepeatingGroundBackground(resources, R.drawable.ground, 0));
//        objectRepository.addBackground(createRepeatingGroundBackground(resources, R.drawable.ground, 1));

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

        AnimatedSpriteShape playerShape = new AnimatedSpriteShape(resources, R.drawable.weirdo_spritesheet, 32, 32, new int[]{1, 4});
        Sprite weirdo = new Sprite(playerShape);
        weirdo.getShape().setPivot(new Point(weirdo.getShape().getWidth() / 2, weirdo.getHeight()));
        player = new WorldObject(weirdo, 0, 0, FollowScreenPanEffect.INSTANCE, new InputWorldObjectControl(controls));
        player.setBounds((int) (-weirdo.getWidth() * 0.2), -(int) (weirdo.getHeight() * 0.1), (int) (weirdo.getWidth() * 0.2), 0);
        player.setX(WIDTH * 0.35);
        player.setY(GROUND_Y);
        player.setVelocity(new PointF());
        player.setObjectRepository(this.objectRepository);
        objectRepository.add(player);

        pan = new AnimatedValue(0);


        rootNode = new SpaceNode();
        rootNode.localPosition.set(0, 0, 0);

        fixedBgObjects = new ArrayList<>();
        parallaxObjects = new ArrayList<>();
        tilingBgObjects = new ArrayList<>();
        foregroundObjects = new ArrayList<>();

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

        foregroundObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.hill_near), rootNode, 0, 0, (GROUND_Y)));
        foregroundObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.grass_near), rootNode, 20, 0, (GROUND_Y+15)));
        foregroundObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.grass_near), rootNode, 0, 0, (BOTTOM_Y + 10)));

        foregroundObjects.add(createSpaceObject(createGroundSpaceObject(resources, R.drawable.table), rootNode, 0, 0, (BOTTOM_Y + 15)));

        wallShape = new ThreePartSpriteShape(resources, R.drawable.wall_left, R.drawable.wall_middle, R.drawable.wall_right, 2);
        foregroundObjects.add(createSpaceObject(createSpaceObject(wallShape), rootNode, 80, 0, (BOTTOM_Y + 30)));

        wallShape = new ThreePartSpriteShape(resources, R.drawable.wall_left, R.drawable.wall_middle, R.drawable.wall_right, 5);
        foregroundObjects.add(createSpaceObject(createSpaceObject(wallShape), rootNode, 0, 0, (BOTTOM_Y + 50)));

        SpaceNode playerNode = new SpaceNode();
        playerNode.localPosition.set(WIDTH * 0.4f, 0, GROUND_Y);
        playerNode.addToNode(rootNode);

        playerObject = new SpaceObject(playerNode);

        playerObject.shape = new AnimatedSpriteShape(resources, R.drawable.weirdo_spritesheet, 32, 32, new int[]{1, 4});
        playerObject.shape.setPivot(new Point(playerObject.shape.getWidth() / 2, playerObject.shape.getHeight()));

        playerObject.setupBody(new Rect(
                (int) (-playerObject.shape.getWidth() * 0.2),
                -(int) (playerObject.shape.getHeight() * 0.1),
                (int) (playerObject.shape.getWidth() * 0.2),
                0));
        playerObject.body.setController(new InputSpaceObjectBodyController(controls, playerObject));

        foregroundObjects.add(playerObject);


        parallaxProjection = new ParallaxPositionProjection(10, new Point3F(0, BASE, 0));
        tileProjection = new TilingPositionProjection(WIDTH, HEIGHT);
        isometricProjection = new IsometricPositionProjection();
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
                    double factor = ((ParallaxScreenPanEffect) panEffect).getFactor();
                    worldObject.setX(worldObject.getX() + ((worldObject.getSprite().getWidth() + WIDTH * (1 + Math.random() * 0.2)) / factor));
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
        if (player.getY() > BOTTOM_Y * 2) {
            player.setY(BOTTOM_Y * 2);
        }
        player.applyScreenPan(pan.getValue(), 0);

        playerObject.update(secondsPerFrame);
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
        this.draw(bufferCanvas);
        canvas.drawBitmap(buffer, 0, 0, null);

//        this.draw(canvas);

        canvas.restoreToCount(savedState);
    }

    private void draw(Canvas canvas) {
        rootNode.localPosition.set(-pan.getValue(), 0, 0);

        for (SpaceObject spaceObject : fixedBgObjects) {
            spaceObject.draw(canvas, null);
        }

        for (SpaceObject spaceObject : tilingBgObjects) {
            spaceObject.draw(canvas, tileProjection);
        }

        // just a test, not needed for background objects
        // Collections.sort(parallaxObjects, zComparator);

        for (SpaceObject spaceObject : parallaxObjects) {
            spaceObject.draw(canvas, parallaxProjection);
        }

        Collections.sort(foregroundObjects, zComparator);
        Collections.reverse(foregroundObjects);

        for (SpaceObject spaceObject : foregroundObjects) {
            spaceObject.draw(canvas, isometricProjection);
        }

//        playerObject.draw(canvas, null);

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
                        (float) (worldObject.getSprite().getX() + bounds.left),
                        (float) (worldObject.getSprite().getY() + bounds.top),
                        (float) (worldObject.getSprite().getX() + bounds.right),
                        (float) (worldObject.getSprite().getY() + bounds.bottom),
                        paint);
            }
        }
    }

//    private WorldObject createFixedHorizonBackground(Resources resources, int resourceId) {
//        SpriteShape shape = new ImageSpriteShape(resources, resourceId);
//
//        Sprite sprite = new Sprite(shape);
//
//        return new WorldObject(sprite, 0, 0, null, null);
//    }

//    private WorldObject createRepeatingGroundBackground(Resources resources, int resourceId, int offset) {
//        SpriteShape shape = new ImageSpriteShape(resources, resourceId);
//
//        Sprite sprite = new Sprite(shape);
//
//        return new WorldObject(sprite, offset * WIDTH, GROUND_Y, CycleScreenPanEffect.INSTANCE, null);
//    }

//    private WorldObject createHorizonObject(Resources resources, int resourceId, double factor) {
//        SpriteShape shape = new ImageSpriteShape(resources, resourceId);
//        shape.setPivot(new Point(0, shape.getHeight()));
//
//        Sprite sprite = new Sprite(shape);
//
//        double firstX = ((Math.random() * 0.5 + 0.2) * WIDTH) / factor;
//
//        return new WorldObject(sprite, firstX, HORIZON_Y, new ParallaxScreenPanEffect(factor), null);
//    }

//    private WorldObject createFloatingHorizonObject(Resources resources, int resourceId, double factor, double y) {
//        WorldObject object = createHorizonObject(resources, resourceId, factor);
//        object.setY(y);
//        return object;
//    }

    private WorldObject createGroundObject(Resources resources, int resourceId, int y) {
        SpriteShape shape = new ImageSpriteShape(resources, resourceId);
        shape.setPivot(new Point(0, shape.getHeight()));

        Sprite sprite = new Sprite(shape);

        return new WorldObject(sprite, 0, y, FollowScreenPanEffect.INSTANCE, null);
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

}
