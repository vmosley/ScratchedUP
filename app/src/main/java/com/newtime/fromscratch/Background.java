package com.newtime.fromscratch;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;

import android.graphics.Bitmap;
import android.graphics.Canvas;

//nope
public class Background { //makes sense I need a background
    private Bitmap image; // yep need an image
    private int x, y, dx; //x location, and dx is change of x (horizontal scrolling)

    public Background(Bitmap res) // my constructor (things I need) in this case picture/bitmap
    {
        image = res; // by bitmap now is named 'image' [passed by copy or ref <skip for now>]
        dx = GamePanel.MOVESPEED;  // hmmm so my MOVESPEED is static? Yep; reachable. Caps for final <const>
    }

    public void update() //somehow Background updates itself? Well it's called by something else
    {
        x += dx; //what's this? x "increases" by speed/velocity/change of x. which is? always -5 (-5 -5 -5 etc) goes left
        if (x < -GamePanel.WIDTH) { //'<-' is two seperate operators < and - . x is less than the entire width?
            x = 0;  //rescroll same image from starting location. needed or looks weird
        }
    }

    public void draw(Canvas canvas) // we can update forever, but it's invisible until we draw.
    {                               // We need a (canvas) .. obviously
        canvas.drawBitmap(image, x, y, null); //canvas has a method, I guess it draws to itself
        //I need a image, location,
        if (x < 0) //what's this mean?  if pic starts on left of canvas.
        {
            canvas.drawBitmap(image, x + GamePanel.WIDTH, y, null); //draw second image(dupe) on right of first
        }
    }
//    public  void setVector(int dx)
//    {
//        this.dx=dx;
//    }
}
