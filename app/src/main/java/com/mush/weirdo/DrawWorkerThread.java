package com.mush.weirdo;

import android.graphics.Canvas;

/**
 * Created by mirko on 21/03/2016.
 */
public class DrawWorkerThread extends Thread {

    private GamePanel gamePanel;
    private boolean running;
    private Canvas canvas;
    private int quadrant;

    public DrawWorkerThread(GamePanel gamePanel, int quadrant) {
        super();
        this.gamePanel = gamePanel;
        this.quadrant = quadrant;
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            if (canvas != null) {
                synchronized (canvas) {
//                final int savedState = canvas.save();
//                switch (quadrant) {
//                    case 0 : canvas.clipRect(0, 0, this.gamePanel.getWidth() / 2 - 1, this.gamePanel.getHeight() / 2 - 1);
//                        break;
//                    case 1 : canvas.clipRect(this.gamePanel.getWidth() / 2 + 1, 0, this.gamePanel.getWidth(), this.gamePanel.getHeight() / 2  -1);
//                        break;
//                    case 2 : canvas.clipRect(0, this.gamePanel.getHeight() / 2 + 1, this.gamePanel.getWidth() / 2 - 1, this.gamePanel.getHeight());
//                        break;
//                    case 3 : canvas.clipRect(this.gamePanel.getWidth() / 2 + 1, this.gamePanel.getHeight() / 2 + 1, this.gamePanel.getWidth(), this.gamePanel.getHeight());
//                        break;
//                }
//                this.gamePanel.draw(canvas);
//                canvas.restoreToCount(savedState);
                    canvas = null;
                }

                try {
                    this.sleep(1);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        }
    }

    public boolean isIdle(){
        return this.canvas == null;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setRunning(boolean b) {
        this.running = b;
    }
}
