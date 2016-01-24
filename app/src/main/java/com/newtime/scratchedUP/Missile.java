package com.newtime.scratchedUP;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

//nope
public class Missile extends GameObject {
    private int score;
    private int speed;
    private Random rand = new Random();
    private Animator animator = new Animator();
    private Bitmap spritesheet;

    public Missile(Bitmap res, int x, int y, int w, int h, int s, int numFrames) {
        spritesheet = res;
        Bitmap[] image = new Bitmap[numFrames]; //create blank Bitmaps for frames

        super.x = x; //location
        super.y = y; // location
        width = w;  //size, limited by bitmap size
        height = h; // size, limited by bitmap size
        speed = 7 + (int) (rand.nextDouble() * score / 30); //speed based on score rising

        if (speed > 70)
            speed = 70; //cap missile speed

        for (int i = 0; i < image.length; i++) { // for 3 times(numFrames)
            image[i] = Bitmap.createBitmap(spritesheet, 0, i * height, width, height);
            //cut/paste image from helicopter spritesheet, start at top of image, increment by height of One helicopter
        }

        animator.setFrames(image);
        animator.setDelay(100 - speed);
    }

    public void update() {
        x -= speed;
        animator.update();
    }

    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(animator.getImage(), x, y, null);
        } catch (Exception e) {
        }
    }

    @Override
    public int getWidth() {
        //offset slightl
        // for more realistic collision detection
        return width - 10;
    }
}
