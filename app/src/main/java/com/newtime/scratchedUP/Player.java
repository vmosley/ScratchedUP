package com.newtime.scratchedUP;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Player extends GameObject { //<-understood
    private Bitmap spritesheet;         //yeh
    private int score;                  //yeh
    public int health;
    //private double dya;
    private boolean up;
    private boolean playing;
    private Animator animation = new Animator(); //Ok I have some trouble with this
    /* the instance is called animation, it is a new hAnimation. Why is new needed?
    * because it's not old? why doesn't playing need a new boolean? Because it's a primative.
    * new makes things explicit, this is good.
    *isn't Animantion a verb/ an action. how can it be a noun? methods are verbs.
    * nouns are 'objects?'
    * */
    private long startTime;
    /* */

    public Player(Bitmap res, int w, int h, int numFrames) {
        x = 100;
        y = GamePanel.HEIGHT / 2;
        dy = 0;
        score = 0;
        health = 3;
        height = h;
        width = w;
        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;
        for (int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i * width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(10);
        startTime = System.nanoTime();
    }

    public void setUp(boolean b) {
        up = b;
    }

    public void update() {
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if (elapsed > 100) {
            score++;
            startTime = System.nanoTime();
        }
        animation.update();
        if (up) {
            //dy = (int) (dya+= 1.1);
            dy -= 1.5;
        } else {
            //dy = (int)(dya+=1.1); //episode 5 1:37
            dy += 1.5;
        }
        if (dy > 14) dy = 14; // sets limit on fall speed?
        if (dy < -14) dy = -14;//see above
        y += dy * 2;
        //   dy = 0;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), x, y, null);
    }

    public void setHealthbar() {
    }

    public int getScore() {
        return score;
    }

    public boolean getPlaying() {
        return playing;
    }

    public void setPlaying(boolean b) {
        playing = b;
    }

    public void resetDY() {
        dy = 0;
    }

    public void resetScore() {
        score = 0;
        health = 3; // a cheat I don't know where to put this
    }

   public void setHealth(int health){ this.health = health;}

    public int getHealth(){ return health;}

    public void decreaseHealth() {
        --health;
    }

    public void increaseHealth() {
        ++health;
    }
}
