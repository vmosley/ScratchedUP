package com.newtime.scratchedUP;

        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Rect;
        import android.graphics.Typeface;
        import android.view.MotionEvent;
        import android.view.SurfaceHolder;
        import android.view.SurfaceView;
        import java.util.ArrayList;
        import java.util.Random;



public class GamePanel extends SurfaceView implements SurfaceHolder.Callback //I understand extends; implements not so much
{
    public static final int WIDTH = 856; //half of ratio of phone width
    public static final int HEIGHT = 480; // other half. still will fill whole phone
    public static final int MOVESPEED = -20; //-5 works right -10 moves missles to right
                                            //actually only background changes,
                                            // giving appearance of speed
    private long smokeStartTime;   //why does smoke need to know when it started?
                                    //to do time related tasks
    private long missileStartTime;  //see above
    private MainThread thread;      //threads are complicated
    private Background bg;          //consists of location and a bitmap, and functions
    private Player player;          //consists
    private ArrayList<Smokepuff> smoke; //more than one smoke cloud, need ArrayList <>
    private ArrayList<Missile> missiles; //see above
    private ArrayList<TopBorder> topborder;//see above
    private ArrayList<BotBorder> botborder; // see above
    //private ArrayList<HealthUnit> healthUnits;
    private Random rand = new Random(); //rand will be random so game's not the same
    private  int maxBorderHeight;
    private int minBorderHeight; //
    private boolean topDown = true; //
    private boolean botDown = true;
    private boolean newGameCreated;
    //increase to slow down difficulty progression, decrease to speed up difficulty progression
    private int progressDenom = 20;
    private Explosion explosion;
    private long startReset;
    private boolean reset;
    private boolean dissapear;
    private boolean started;
    private int best;
    private Bitmap healthUnit;


    public GamePanel(Context context)
    {
        super(context);

        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry && counter<1000)
        {
            counter++;
            try{thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;

            }catch(InterruptedException e){e.printStackTrace();}

        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.grassbg1));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.helicopter)
                ,65, 25, 3);
        healthUnit = BitmapFactory.decodeResource(getResources(), R.drawable.health);
        smoke = new ArrayList<Smokepuff>();
        missiles = new ArrayList<Missile>();
        topborder = new ArrayList<TopBorder>();
        botborder = new ArrayList<BotBorder>();
        //healthUnits = new ArrayList<HealthUnit>();
        smokeStartTime=  System.nanoTime();
        missileStartTime = System.nanoTime();

        thread = new MainThread(getHolder(), this);
        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction()==MotionEvent.ACTION_DOWN){ //finger is on screen
            if(!player.getPlaying() && newGameCreated && reset)
            {
                player.setPlaying(true);
                player.setUp(true);
            }
            if(player.getPlaying())
            {

                if(!started)started = true;
                reset = false;
                player.setUp(true);
            }
            return true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP) //finger not on screen
        {
            player.setUp(false);
            return true; //what does this do?
        }

        return super.onTouchEvent(event);
    }

    public void update()
    {
        if(player.getPlaying()) {

            this.updatePlayer();
            bg.update();
            player.update();

            //calculate the threshold of height the border can have based on the score
            //max and min border heart are updated, and the border switched direction when either max or
            //min is met

            maxBorderHeight = 30+player.getScore()/progressDenom;

            //cap max border height so that borders can only take up a total of 1/2 the screen
            if(maxBorderHeight > HEIGHT/4)maxBorderHeight = HEIGHT/4;
            minBorderHeight = 5+player.getScore()/progressDenom;

            //update top border
            this.updateTopBorder();

            //update bottom border
            this.updateBottomBorder();

            //update missles
            this.updateMissles();

            //update smoke
            this.updateSmoke();

            //update healthbar
            //this.updateHealthBar();
        }
        else{
            player.resetDY(); // set's dy back to 0. I guess this set's player back in midscreen
            if(!reset)
            {
                newGameCreated = false;
                startReset = System.nanoTime();
                reset = true;
                dissapear = true;   //
                explosion = new Explosion(BitmapFactory.decodeResource
                        (getResources(),R.drawable.explosion),player.getX(),
                        player.getY()-30, 100, 100, 25);
            }

            explosion.update();
            long resetElapsed = (System.nanoTime()-startReset)/1000000;

            if(resetElapsed > 2500 && !newGameCreated)
            {
                newGame();
            }
        }
    }
    public void updatePlayer(){
        if(botborder.isEmpty()) //if no borders
        {
            player.setPlaying(false); //player can't play
            return;
        }
        if(topborder.isEmpty())
        {
            player.setPlaying(false);
            return;
        }
    }
    private void updateSmoke() {
        //add smoke puffs on timer
        long elapsed = (System.nanoTime() - smokeStartTime)/1000000;
        if(elapsed > 120){
            smoke.add(new Smokepuff(player.getX(), player.getY()+10));
            smokeStartTime = System.nanoTime();
        }

        for(int i = 0; i<smoke.size();i++)
        {
            smoke.get(i).update();
            if(smoke.get(i).getX()<-10)
            {
                smoke.remove(i);
            }
        }
    }

    public boolean collision(GameObject a, GameObject b)
    {
        if(Rect.intersects(a.getRectangle(), b.getRectangle()))
        {
            return true;
        }
        return false;
    }

    public void updateMissles(){
        //add missiles on timer
        long missileElapsed = (System.nanoTime()-missileStartTime)/1000000;
        if(missileElapsed >(2000 - player.getScore()/4)){


            //first missile always goes down the middle
            if(missiles.size() == 0)
            {
                missiles.add(new Missile(BitmapFactory.decodeResource(getResources(),R.drawable
                        .missile)
                        ,WIDTH + 10 ,HEIGHT/2, 45, 15, player.getScore(), 13));
            }
            else
            {

                missiles.add(new Missile(BitmapFactory.decodeResource(getResources(),R.drawable
                        .missile)
                        ,WIDTH+10
                        ,(int)(rand.nextDouble()*(HEIGHT - (maxBorderHeight * 2))+maxBorderHeight)
                        ,45,15, player.getScore(),13));
            }

            //reset timer
            missileStartTime = System.nanoTime();
        }
        //loop through every missile and check collision and remove
        for(int i = 0; i<missiles.size();i++)
        {
            //update missile
            missiles.get(i).update();

            if(collision(missiles.get(i),player))
            {
                missiles.remove(i);
                player.decreaseHealth();
                if(player.getHealth()<0) {
                    player.setPlaying(false);
                }


                break;
            }
            //remove missile if it is way off the screen
            if(missiles.get(i).getX()<-100)
            {
                missiles.remove(i);
                break;
            }
        }
    }

    @Override
    public void draw(Canvas canvas)
    {
        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);

        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            if(!dissapear) {
                player.draw(canvas); //if not dissapeared draw player on canvas? but why
                                    // what's dissapeared
            }
            //draw smokepuffs
            for(Smokepuff sp: smoke)
            {
                sp.draw(canvas);
            }
            //draw missiles
            for(Missile m: missiles)
            {
                m.draw(canvas);
            }


            //draw topborder
            for(TopBorder tb: topborder)
            {
                tb.draw(canvas);
            }

            //draw botborder
            for(BotBorder bb: botborder)
            {
                bb.draw(canvas);
            }

           // for(player.getHealth)

            //draw explosion
            if(started)
            {
                explosion.draw(canvas);
            }
            drawText(canvas);
            canvas.restoreToCount(savedState);

        }
    }

//    public void updateHealthBar()
//    {
//        for(int i = 0; i < player.getHealth(); i++)
//        {
//            healthUnits.add(new HealthUnit(
//                    BitmapFactory.decodeResource(getResources(), R.drawable.health), //the resource
//                    5,
//                    //healthUnits.get(healthUnits.size()-1).getX()+5, //x get last image position
//                    15)//y
//            );
//        }
//    }

    public void updateTopBorder()
    {
        //check top border collision
        for(int i = 0; i <topborder.size(); i++) //for
        {
            if(collision(topborder.get(i),player))
                player.setPlaying(false);
        }

        //every 50 points, insert randomly placed top blocks that break the pattern
        if(player.getScore()%50 ==0)
        {
            topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick
            ),topborder.get(topborder.size()-1).getX()+20,0,(int)((rand.nextDouble()*(maxBorderHeight
            ))+1)));
        }
        for(int i = 0; i<topborder.size(); i++)
        {
            topborder.get(i).update();
            if(topborder.get(i).getX()<-20)
            {
                topborder.remove(i);
                //remove element of arraylist, replace it by adding a new one

                //calculate topdown which determines the direction the border is moving (up or down)
                if(topborder.get(topborder.size()-1).getHeight()>=maxBorderHeight)
                {
                    topDown = false;
                }
                if(topborder.get(topborder.size()-1).getHeight()<=minBorderHeight)
                {
                    topDown = true;
                }
                //new border added will have larger height
                if(topDown)
                {
                    topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),
                            R.drawable.brick),topborder.get(topborder.size()-1).getX()+20,
                            0, topborder.get(topborder.size()-1).getHeight()+1));
                }
                //new border added wil have smaller height
                else
                {
                    topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),
                            R.drawable.brick),topborder.get(topborder.size()-1).getX()+20,
                            0, topborder.get(topborder.size()-1).getHeight()-1));
                }

            }
        }
    }


    public void updateBottomBorder()
    {
        //check bottom border collision
        for(int i = 0; i<botborder.size(); i++)
        {
            // if(collision(botborder.get(i), player))
            // player.setPlaying(false);
        }

        //every 40 points, insert randomly placed bottom blocks that break pattern
        if(player.getScore()%50 == 0)
        {
            botborder.add(new BotBorder(
                    BitmapFactory.decodeResource(getResources(), R.drawable.brick), //the resource
                    botborder.get(botborder.size()-1).getX()+50, //x
                    (int)((rand.nextDouble()*maxBorderHeight)+(HEIGHT-maxBorderHeight))//y random
            ));
        }

        //update bottom border
        for(int i = 0; i<botborder.size(); i++)
        {
            botborder.get(i).update();

            //if border is moving off screen, remove it and add a corresponding new one
            if(botborder.get(i).getX()<-20) {
                botborder.remove(i);


                //determine if border will be moving up or down
                if (botborder.get(botborder.size() - 1).getY() <= HEIGHT-maxBorderHeight) {
                    botDown = true;
                }
                if (botborder.get(botborder.size() - 1).getY() >= HEIGHT - minBorderHeight) {
                    botDown = false;
                }

                if (botDown) {
                    botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick
                    ), botborder.get(botborder.size() - 1).getX() + 20, botborder.get(botborder.size() - 1
                    ).getY() + 1));
                } else {
                    botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick
                    ), botborder.get(botborder.size() - 1).getX() + 20, botborder.get(botborder.size() - 1
                    ).getY() - 1));
                }
            }
        }
    }

    public void newGame()
    {
        dissapear = false;  //new game set's dissapear flag to false. what happens when it's true?

        botborder.clear();
        topborder.clear();

        missiles.clear();
        smoke.clear();

        minBorderHeight = 5;
        maxBorderHeight = 30;

        player.resetDY();
        player.resetScore();
        player.setY(HEIGHT/2);

        if(player.getScore()>best)
        {
            best = player.getScore();
        }

        //create initial borders

        //initial top border
        for(int i = 0; i*20<WIDTH+40;i++)
        {
            //first top border create
            if(i==0)
            {
                topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick
                ),i*20,0, 10));
            }
            else
            {
                topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick
                ),i*20,0, topborder.get(i-1).getHeight()+1));
            }
        }
        //initial bottom border
        for(int i = 0; i*20<WIDTH+40; i++)
        {
            //first border ever created
            if(i==0)
            {
                botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick)
                        ,i*20,HEIGHT - minBorderHeight));
            }
            //adding borders until the initial screen is filed
            else
            {
                botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick)
                        ,i * 20, botborder.get(i - 1).getY() - 1));
            }
        }

        newGameCreated = true;


    }
    public void drawText(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("DISTANCE: " + (player.getScore() * 3), 10, HEIGHT - 10, paint);
        canvas.drawText("BEST: " + best, WIDTH - 215, HEIGHT - 10, paint);
        //canvas.drawText("HEALTH: " + player.getHealth(), 25, 400, paint);
        for (int i = 0; i< player.getHealth(); i++)
        {
            canvas.drawBitmap(healthUnit,i*10 ,5,paint);
        }

        //canvas.drawText("Lives " + (player.healthbar), 10, HEIGHT - 100, paint);
        //canvas.drawBitmap(player,33,2);
        if(!player.getPlaying()&&newGameCreated&&reset)
        {
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PRESS TO START", WIDTH/2-50, HEIGHT/2, paint1);

            paint1.setTextSize(20);
            canvas.drawText("PRESS AND HOLD TO GO UP", WIDTH/2-50, HEIGHT/2 + 20, paint1);
            canvas.drawText("RELEASE TO GO DOWN", WIDTH/2-50, HEIGHT/2 + 40, paint1);
        }
    }


}

