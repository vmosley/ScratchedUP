package com.newtime.fromscratch;

import android.graphics.Bitmap;
//The only reason this is here is to test out branchA on github
//nope
public class Animation {
    //I think i understand this.
    private Bitmap[] frames;
    private int currentFrame;
    private long startTime;
    private long delay;
    private boolean playedOnce;

    // no idea
    public void setFrames(Bitmap[] frames) {
        this.frames = frames;
        this.currentFrame = 0;
        this.startTime = System.nanoTime();
    }
    // push From BranchA back to Master hopefully
    public void setDelay(long d) {
        this.delay = d; //this.delay and delay are the same class fields
    }

    public void update() {
        long elapsed = (System.nanoTime() - startTime) / 1000000; //always know how much time has elapsed
        if (elapsed > delay) {  //why would elapsed time be greater than delay, what's delay for?
            currentFrame++;   //go to the next frame 1 then 2 etc. (easiest to understand)
            startTime = System.nanoTime(); //oh ok a new start time. will this change elapsed?
        }
        if (currentFrame == frames.length) { // if reached the end of possible frames
            currentFrame = 0; //reset to beginning frame
            playedOnce = true; //playedOnce flag is raised
        }
    }

    public Bitmap getImage() {  //what is this? //How often do we getImage? every update?
                                // No, close; every draw
        return frames[currentFrame]; //frame is a bitmap, we can draw these.
    }

    public int getFrame() { //never used
        return currentFrame; //this is an int ctrl+space told me
    }

    public void setFrame(int i) {
        currentFrame = i;
    } //never used but could be used to manually
                                                    // pick/set/setup a frame

    public boolean playedOnce() {
        return playedOnce;
    } //getPlayedOnce more like isPlayedOnce True OR False
}
