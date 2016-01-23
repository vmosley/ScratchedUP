package com.newtime.scratchedUP;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Explosion { //why's this not extending game object?
    private int x;      //could be from game object but not
    private int y;      //could be from game object but not
    private int width;  //could be from game object but not
    private int height; //could be from game object but not
    private int row;    //row of the animator sheet (5 by 5 sheet)
    private Animator animator = new Animator(); //Ok This I need to learn more on
    private Bitmap spritesheet; //The 5 by 5 sheet

    public Explosion(Bitmap res, int x, int y, int w, int h, int numFrames) //there's gonna be an explosion
    {
        this.x = x;     //initialized by constructor's parameters 'this' allows x to be used throu class
        this.y = y;
        this.width = w;
        this.height = h;
        Bitmap[] image = new Bitmap[numFrames]; // an array of images; 25 in our case.
        this.spritesheet = res;    // why not this.spritesheet ?

        //This is interesting we are expecting a specific format grid for the picture. 5x5(25)
        // So the question is; why not load the actual Bitmap here as well?.
        //Something to do about bitmaps being loaded on surfaces only.
        for (int i = 0; i < image.length; i++) //image.length = 25
        {
            if (i % 5 == 0 && i > 0) //i> 0 will get past initial [0]. otherwise 0 mod anything = 0
                row++; //i get the mod5; why's i gotta be greater than 0? ok remainder starts at 0
            image[i] = Bitmap.createBitmap(spritesheet, (i - (5 * row)) * width, row * height, width, height); //just assume this works
        }

        animator.setFrames(image); //what does setFrames even do; even mean?
        animator.setDelay(10);   //if I increase would it be slow explosion? yep
    }

    public void draw(Canvas canvas) {
        if (!animator.isPlayedOnce()) //boolean (true or false)
        {
            canvas.drawBitmap(animator.getImage(), x, y, null); //
        }
    }

    public void update() {
        if (!animator.isPlayedOnce()) {
            animator.update();
        }
    }

    public int getHeight() {
        return height;
    }
}
