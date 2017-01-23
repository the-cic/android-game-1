package com.mush.weirdo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.text.MessageFormat;

/**
 * Created by mirko on 03/03/2016.
 */
public class GameContentView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawThread drawThread;
    private GameContent gameContent;
    private Paint fpsPaint;

    public GameContentView(Context context) {
        super(context);

        System.out.println("New game panel");
        getHolder().addCallback(this);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        System.out.println("Surface Created");

        if (gameContent == null) {
            System.out.println("Create gameContent");
            gameContent = new GameContent(getResources());
        }

        if (drawThread == null) {
            System.out.println("Create main thread");
            drawThread = new DrawThread(getHolder(), this);
            drawThread.setName("DrawThread");
            drawThread.setRunning(true);
            drawThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println(MessageFormat.format("Surface Changed f:{0} w:{1} h:{2}", format, width, height));
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        System.out.println("Surface Destroyed");

        boolean retry = true;
        while (retry) {
            try {
                System.out.println("set running false");
                drawThread.setRunning(false);
                System.out.println("join");
                drawThread.join();
                retry = false;
                System.out.println("running false and thread joined");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        drawThread = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean b = super.onTouchEvent(event);

        processInput(event);

        // Follow move events on the right side, and only touch events on the left
        if (!(event.getX() < getWidth() * 0.1 && event.getY() < getHeight() * 0.1)) {
            return true;
        }
        return b;
    }

    private void processInput(MotionEvent event) {
        if (event.getX() < getWidth() * 0.1 && event.getY() < getHeight() * 0.1) {
            if (drawThread != null) {
                switch (drawThread.FPS) {
                    case 30:
                        drawThread.FPS = 60;
                        break;
                    case 60:
                        drawThread.FPS = 15;
                        break;
                    case 15:
                        drawThread.FPS = 30;
                        break;
                    default:
                        drawThread.FPS = 30;
                }
            }
        }

        gameContent.processInput(event, getWidth(), getHeight());
    }

    public void update(double secondsPerFrame) {
        gameContent.update(secondsPerFrame);
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas == null) {
            System.out.println("Canvas is null!");
            return;
        }
        super.draw(canvas);
        render(canvas);
    }

    private void render(Canvas canvas) {
        if (canvas != null) {
            gameContent.draw(canvas, getWidth(), getHeight());

            if (drawThread != null) {
                double fps = drawThread.getAverageFPS();
                double fpsDiff = drawThread.FPS - fps;

                if (fpsDiff < drawThread.FPS * 0.2) {
                    getFpsPaint().setColor(Color.GREEN);
                } else if (fpsDiff < drawThread.FPS * 0.4) {
                    getFpsPaint().setColor(Color.YELLOW);
                } else {
                    getFpsPaint().setColor(Color.RED);
                }

                canvas.drawText("Fps: " + fps, 10, 20, getFpsPaint());
            }
        }
    }

    private Paint getFpsPaint() {
        if (fpsPaint == null) {
            Typeface fpsTypeface = Typeface.create("sans-serif", Typeface.BOLD);
            fpsPaint = new Paint();

            fpsPaint.setColor(Color.RED);
            fpsPaint.setStyle(Paint.Style.FILL);
            fpsPaint.setTextSize(20);
            fpsPaint.setTypeface(fpsTypeface);
        }

        return fpsPaint;
    }

}
