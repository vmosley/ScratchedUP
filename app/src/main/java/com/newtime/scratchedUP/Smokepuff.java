package com.newtime.scratchedUP;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

//nope
public class Smokepuff extends GameObject {
    public int r; // radius

    public Smokepuff(int x, int y) { //constructed with two parameters, <arguments are actual>
        this.r = 25; //As of now, the only place to set size is in the class, here itself
        super.x = x; //super mean is like this but for invisible class fields( from extends)
        super.y = y;
    }

    public void update() {
        x -= 10;
    } //every update move a little more left
                                      //wait do these ever disappear or stay in memory forever?

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.argb(44,100,14,23));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x - r, y - r, r, paint);
        //canvas.drawCircle(x - r + 2, y - r - 2, r, paint);
        //canvas.drawCircle(x - r + 4, y - r + 1, r, paint);
    }
}
