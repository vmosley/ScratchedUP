package com.newtime.scratchedUP;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Explosion { //why's this not extending game object?
    private int x;      //could be from game object but not
    private int y;      //could be from game object but not
    private int width;  //could be from game object but not
    private int height; //could be from game object but not
    private int row;    //row of the animation sheet (5 by 5 sheet)
    private Animation animation = new Animation(); //Ok This I need to learn more on
    private Bitmap spritesheet; //The 5 by 5 sheet

    public Explosion(Bitmap res, int x, int y, int w, int h, int numFrames) //there's gonna be an explosion
    {
        this.x = x;     //initialized by constructor's parameters 'this' allows x to be used throu class
        this.y = y;
        this.width = w;
        this.height = h;
        Bitmap[] image = new Bitmap[numFrames]; // an array of images; 25 in our case.
        this.spritesheet = res;    // why not this.spritesheet ?
        for (int i = 0; i < image.length; i++) //image.length = 25
        {
            if (i % 5 == 0 && i > 0)
                row++; //i get the mod5; why's i gotta be greater than 0? ok remainder starts at 0
            image[i] = Bitmap.createBitmap(spritesheet, (i - (5 * row)) * width, row * height, width, height); //just assume this works
        }
        animation.setFrames(image); //what does setFrames even do; even mean?
        animation.setDelay(10);   //if I increase would it be slow explosion? yep
    }

    public void draw(Canvas canvas) {
        if (!animation.playedOnce()) //boolean (true or false)
        {
            canvas.drawBitmap(animation.getImage(), x, y, null); //
        }
    }

    public void update() {
        if (!animation.playedOnce()) {
            animation.update();
        }
    }

    public int getHeight() {
        return height;
    }
}
