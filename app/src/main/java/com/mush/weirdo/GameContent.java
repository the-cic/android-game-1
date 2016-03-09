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
    ArrayList<BackgroundObject> backgroundObjects;
    private Sprite grassTiles;

    private Bitmap jen;
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
        backgroundObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.mountains_far), 0.1, HORIZON_Y));
        backgroundObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.mountains_mid_far), 0.15, HORIZON_Y));
        backgroundObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.mountains_mid_near), 0.2, HORIZON_Y));
        backgroundObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.cloud), 0.3, HORIZON_Y * 0.5));
        backgroundObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.cloud), 0.35, HORIZON_Y * 0.4));
        backgroundObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.mountains_near), 0.4, HORIZON_Y));
        backgroundObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.trees_far), 0.5, HORIZON_Y));
        backgroundObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.hill_near), 1, GROUND_Y));
        backgroundObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.grass_near), 1, GROUND_Y));
        backgroundObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.water_near), 1, BOTTOM_Y));

        for (BackgroundObject bgObject : backgroundObjects) {
            bgObject.setX((int) ((Math.random() * 0.5 + 0.2) * GamePanel.WIDTH));
        }

        grassTiles = new Sprite(BitmapFactory.decodeResource(resources, R.drawable.grass), 20, 10, 6);

        jen = BitmapFactory.decodeResource(resources, R.drawable.weirdo);

        jenOffset = 5;

        setVector(30);
    }

    public void processInput(MotionEvent event) {

    }

    public void update(double secondsPerFrame) {
        x += dx * secondsPerFrame;
        y += dx * secondsPerFrame * 0.05;
        if (x > GamePanel.WIDTH) {
            //x = 0;
        }

        for (BackgroundObject bgObject : backgroundObjects) {
            double objectScreenX = bgObject.getScreenX(x);
            if (objectScreenX < -bgObject.image.getWidth()) {
                bgObject.setX(bgObject.getX() + (bgObject.image.getWidth() + (GamePanel.WIDTH * (1 + Math.random()))) / bgObject.factor);
            }
        }

        jenU += 3.3 * secondsPerFrame;
        jenV += 11.1 * secondsPerFrame;
        jenX = (int) (Math.sin(jenU) * jenOffset);
        jenY = (int) -Math.abs((Math.sin(jenV) * jenOffset));
    }


    public void draw(Canvas canvas) {
        canvas.drawBitmap(horizonBackground, (int) 0, (int) 0, null);
        canvas.drawBitmap(groundBackground, (float) (-x % GamePanel.WIDTH), GROUND_Y, null);
        canvas.drawBitmap(groundBackground, (float) (-x % GamePanel.WIDTH + GamePanel.WIDTH), GROUND_Y, null);

        for (BackgroundObject object : backgroundObjects) {
            canvas.drawBitmap(object.image,
                    (float) object.getScreenX(x),
                    (float) object.getScreenY(),
                    null);
        }

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
        canvas.drawBitmap(jen, GamePanel.WIDTH / 4 + jenX, GROUND_Y + jenY - jen.getHeight(), null);
//        canvas.restoreToCount(savedState);
    }

    public void setVector(int dx) {
        this.dx = dx;
    }
}
