package com.newtime.fromscratch;

import android.graphics.Rect;

//I understand this entirely
public class GameObject {
   //this is just a test part2
    protected int x;
    protected int y;
    protected int dy;
    protected int dx;
    protected int width;
    protected int height;

    //with this I can change horizontal position
    public void setX(int x) {
        this.x = x;
    }

    //with this I can change vertical position
    public void setY(int y) {
        this.y = y;
    }

    //  find out current horizontal postion
    public int getX() {
        return x;
    }

    //  find out current vertical postion
    public int getY() {
        return y;
    }

    //  find out the height of object
    public int getHeight() {
        return height;
    }

    //  find out the width of object
    public int getWidth() {
        return width;
    }

    //  ??? get a rectangle what's that mean even? I get returning numbers but how do you return
//  a rectangle ?
    public Rect getRectangle() {
        return new Rect(x, y, x + width, y + height);
    }
}
