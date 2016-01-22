package com.newtime.scratchedUP;

import android.graphics.Bitmap;
//Testing github connection
//Name probably should be Animator
//
public class Animator {

    private Bitmap[] frames;  //Get image with frames in it
    private int currentFrame; //to always be aware where you are within the animation sequence
    private long startTime;   //Has to do with speed of animation
    private long delay;       //See above
    private boolean playedOnce; //Has to do with repeating animation loop

    //implicit constructor

    // no idea
    public void setFrames(Bitmap[] frames) { //takes an array of images as argument
        this.frames = frames;       //parameter 'frames' equals class 'frames' or this.frames
        this.currentFrame = 0;      //current frame = first image in array will start at 0; not 1
        this.startTime = System.nanoTime(); //time frame of reference comes from actual time
                                            //interesting how this will change almost instantly
    }

    public void setDelay(long d) {
        this.delay = d; //parameter passed to this.delay
    }

    public void update() { //every time we update we will 'elapsed' will know how much time has passed
        long elapsed = (System.nanoTime() - startTime) / 1000000; //Some time has passed, always know how much time has elapsed
                                                                  //1000000000 vs 1000000 giving smoother frame rate?
        if (elapsed > delay) {  //if we updated enough then eventally elapsed time will be greater than delay
            currentFrame++;   //enough time passed? ok go to next frame
            startTime = System.nanoTime(); //oh ok a new start time. will this change elapsed?
        } // is there a way to use break? I don't use break enough to know it's uses.
            //probably not. 'if' statements arent loops.
        if (currentFrame == frames.length) { // if reached the end of possible frames
            currentFrame = 0; //reset to beginning frame
            playedOnce = true; //isPlayedOnce flag is raised
        }
    }

    public Bitmap getImage() {  //what is this? //How often do we getImage? every update?
                                // No, close; every draw
        return frames[currentFrame]; //frame is a bitmap, we can draw these.
    }

    public int getFrame() { //never used, ignore for now
        return currentFrame; //this is an int ctrl+space told me
    }

    public void setFrame(int i) { //never used, ignore for now
        currentFrame = i;
    } //never used but could be used to manually pick/set/setup a frame

    public boolean isPlayedOnce() {
        return this.playedOnce;
    } //getPlayedOnce more like isPlayedOnce True OR False

    public void setPlayedOnce(boolean playedOnce)
    {this.playedOnce = playedOnce;}
}
