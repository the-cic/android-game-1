package com.mush.weirdo;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by mirko on 03/03/2016.
 */
public class MainThread extends Thread {

    private static final long NANOS_PER_MILLISECOND = 1000000;
    private static final long NANOS_PER_SECOND = 1000 * NANOS_PER_MILLISECOND;

    public int FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    private static Canvas canvas;

//    private DrawWorkerThread[] workerThreads;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
//        this.workerThreads = new DrawWorkerThread[] {
//                new DrawWorkerThread(gamePanel, 0),
//                new DrawWorkerThread(gamePanel, 1),
//                new DrawWorkerThread(gamePanel, 2),
//                new DrawWorkerThread(gamePanel, 3)
//        };
//
//        for (DrawWorkerThread worker : workerThreads) {
//            worker.start();
//        }
    }

    @Override
    public void run() {
        long currentTime;
        long elapsedTime;
        long targetTime;
        long waitTime;
        long remainingTime;
        long totalTime = 0;
        int frameCount = 0;

        long lastTime = System.nanoTime();

        System.out.println("thread run started");

        while (running) {
            currentTime = System.nanoTime();
            elapsedTime = currentTime - lastTime;

            targetTime = NANOS_PER_SECOND / FPS;

            updateAndDraw(elapsedTime);

            lastTime = currentTime;
            currentTime = System.nanoTime();
            remainingTime = targetTime - (currentTime - lastTime);

            if (remainingTime > 0) {
                waitTime = remainingTime / NANOS_PER_MILLISECOND;
            } else {
                waitTime = 1;
            }

            try {
                this.sleep(waitTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            totalTime += elapsedTime;
            frameCount++;

            if (frameCount >= FPS) {
                averageFPS = NANOS_PER_SECOND / (totalTime / frameCount);
                frameCount = 0;
                totalTime = 0;
//                System.out.println("fps:"+averageFPS);
//                System.out.println("e:"+elapsedTime + "t:"+targetTime);
//                System.out.println("w:"+waitTime);
            }
        }

        System.out.println("thread run finished");
    }

    private void updateAndDraw(long elapsedTime) {
        canvas = null;
        double elapsedSeconds = (double) elapsedTime / NANOS_PER_SECOND;

        try {
            //canvas = this.surfaceHolder.lockCanvas();
            canvas = this.surfaceHolder.lockCanvas();
            synchronized (surfaceHolder) {
//                this.gamePanel.processInput();
                this.gamePanel.update(elapsedSeconds);
                this.gamePanel.draw(canvas);

//                for (DrawWorkerThread worker : workerThreads) {
//                    worker.setCanvas(canvas);
//                    worker.interrupt();
//                }
//
//                while (!(workerThreads[0].isIdle() && workerThreads[1].isIdle() && workerThreads[2].isIdle() && workerThreads[3].isIdle())) {
//                    this.sleep(1);
//                }
            }
        } catch (Exception e) {

        } finally {
            if (canvas != null) {
                try {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void setRunning(boolean b) {
        running = b;
//        if (!running) {
//            for (DrawWorkerThread worker : workerThreads) {
//                worker.setRunning(false);
//            }
//        }
    }

    public double getAverageFPS() {
        return averageFPS;
    }
}
