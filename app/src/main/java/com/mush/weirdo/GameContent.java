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

    private Bitmap fixedBackground;
    ArrayList<BackgroundObject> backgroundObjects;

    private Bitmap jen;
    private double x, y, dx;
    private double jenU;
    private double jenV;
    private int jenX;
    private int jenY;
    private int jenOffset;

    public GameContent(Resources resources) {
        fixedBackground = BitmapFactory.decodeResource(resources, R.drawable.background);

        backgroundObjects = new ArrayList<>();
        backgroundObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.mountains_far), 0.6, HORIZON_Y));
        backgroundObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.mountains_mid_far), 0.65, HORIZON_Y));
        backgroundObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.mountains_mid_near), 0.7, HORIZON_Y));
        backgroundObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.mountains_near), 0.85, HORIZON_Y));
        backgroundObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.trees_far), 0.9, HORIZON_Y));
        backgroundObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.hill_near), 1, GROUND_Y));
        backgroundObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.grass_near), 1, GROUND_Y));
        backgroundObjects.add(new BackgroundObject(BitmapFactory.decodeResource(resources, R.drawable.water_near), 1, BOTTOM_Y));

        for (BackgroundObject bgObject : backgroundObjects) {
            bgObject.setX((int) ((Math.random()* 0.5 + 0.2) * GamePanel.WIDTH));
        }

        jen = BitmapFactory.decodeResource(resources, R.drawable.weirdo);

        jenOffset = 5;

        setVector(30);
    }

    public void processInput(MotionEvent event) {

    }

    public void update(double secondsPerFrame) {
        x += dx * secondsPerFrame;
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
        canvas.drawBitmap(fixedBackground, (int) 0, (int) 0, null);

        for (BackgroundObject object : backgroundObjects) {
            canvas.drawBitmap(object.image,
                    (float)object.getScreenX(x),
                    (float)object.getScreenY(),
                    null);
        }

//        final int savedState = canvas.save();
//        canvas.scale(0.15f, 0.15f);
        canvas.drawBitmap(jen, GamePanel.WIDTH / 4 + jenX, GROUND_Y + jenY - jen.getHeight(), null);
//        canvas.restoreToCount(savedState);
    }

    public void setVector(int dx) {
        this.dx = dx;
    }
}
