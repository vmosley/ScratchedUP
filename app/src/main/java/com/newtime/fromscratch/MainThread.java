package com.newtime.fromscratch;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

//nope
public class MainThread extends Thread //What's a thread?
{
    private int FPS = 30; //ok
    private double averageFPS; // ok
    private SurfaceHolder surfaceHolder; // what's a sufaceholder? like a sheet of paper?
    private GamePanel gamePanel; // what's the diff between suffaceholder and gamepan
    private boolean running;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000 / FPS;
        while (running) {
            startTime = System.nanoTime();
            canvas = null;
            //try locking the canvas for pixel editing
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
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
            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;
            try {
                this.sleep(waitTime);
            } catch (Exception e) {
            }
            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == FPS) {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.print(averageFPS);
            }
        }
    }

    public void setRunning(boolean b) {
        running = b;
    }

    public static class background {
    }
}
