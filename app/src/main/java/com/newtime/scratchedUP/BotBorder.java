package com.newtime.scratchedUP;

import android.graphics.Bitmap;
import android.graphics.Canvas;

//nope
public class BotBorder extends GameObject { //yep
    private Bitmap image; //yep needs image (brick)

    public BotBorder(Bitmap res, int x, int y) //constructor
    {
        height = 50; //yep
        width = 20;  //yep
        this.x = x;  //this means thru out this class. x from constructor used thru-out BotBorder
        this.y = y;
        dx = GamePanel.MOVESPEED;  // same movespeed keeps pace with everything.
        image = Bitmap.createBitmap(res, 0, 0, width, height); // start at 0,0
    }

    public void update() //same update is on pace
    {
        x += dx;    // -5 -5 etc (moving left)
    }

    public void draw(Canvas canvas) //to draw I need a canvas
    {
        canvas.drawBitmap(image, x, y, null); //still weird a little that canvas draws to itself a lil
    }
}
