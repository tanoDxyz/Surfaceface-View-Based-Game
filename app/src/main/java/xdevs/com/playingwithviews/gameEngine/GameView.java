package xdevs.com.playingwithviews.gameEngine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xdevs.com.playingwithviews.R;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by syedt on 1/24/2018.
 */

public final class GameView extends SurfaceView implements  SurfaceHolder.Callback,OnTouchListener {
    private HumanPlane humanPlane;
    private long score;
    public static final  SecureRandom rndG = new SecureRandom();
    public static long instanceCounter = 0;
    public int gameViewWidth;
    public int gameViewHeight;
    private TextView scoreView;
    private TextView playerLifeView;
    protected final PlanesAndCollisionHandler planesAndCollisionHandler = new PlanesAndCollisionHandler();

    private static final String TAG  = "customSurfaceHolder";
    private GameLoopThread gameLoopThread;
    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        getHolder().addCallback(this);
        setOnTouchListener(this);
        planesAndCollisionHandler.addHumanPlayer(humanPlane = new HumanPlane(this,"human", R.drawable.v_f22));
        humanPlane.setLife(1000);
    }

    public void setPlayerLifeView(TextView view) {
        this.playerLifeView = view;
    }

    public void setScoreView(TextView view) {
        this.scoreView = view;
    }
    public void addScore(int score) {
        this.score +=score;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated: ");
        try {
            System.gc();
            gameViewWidth = getMeasuredWidth();
            gameViewHeight = getMeasuredHeight();
            gameLoopThread = new GameLoopThread(this,60); //60fps
            gameLoopThread.setRunning(true);
            gameLoopThread.start();
        } catch (Exception ex){}
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceCreated: surface changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        System.gc();
        gameLoopThread.setRunning(false);
        boolean retry = true;
        while (retry) {
            try {
                gameLoopThread.join();
                retry = false;
            } catch (InterruptedException e) {}
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        for(Sprite sprite: planesAndCollisionHandler.getNewPlanesAndRemoveOldOnes()) {
            sprite.onDraw(canvas);
        }
    }


    public PlanesAndCollisionHandler getPlanesAndCollisionHandler() {
        return planesAndCollisionHandler;
    }

    private boolean isTouched = false;
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int x = Math.round(event.getX());
        int y = Math.round(event.getY());
        switch (event.getAction()) {

            case ACTION_DOWN:
                isTouched = humanPlane.isTouched(x,y);
                break;
            case ACTION_MOVE:
                if(isTouched) {
                    this.humanPlane.setCords(new Point(x,y));
                }
                break;
            case ACTION_UP:
                isTouched = false;
                break;
        }
        return true;
    }


    public void onDraw(Canvas canvas) {
        humanPlane.onDraw(canvas);
        this.post(new Runnable() {
            @Override
            public void run() {
                playerLifeView.setText("Your Life " + humanPlane.getLife());
                scoreView.setText("Your Score " + score);
            }
        });
    }

    public  class PlanesAndCollisionHandler {

        private List<Sprite> planes = Collections.synchronizedList(new ArrayList<Sprite>(10));


        public List<Sprite> getNewPlanesAndRemoveOldOnes() {

            if(getActivePlanes().size() >= 5) {
                return planes;
            }
                // now create planes
            final int noOfPlanesToCreate = rndG.nextInt(3) + 1;
            for(int i=0;i<noOfPlanesToCreate;++i) {
                final EnemyPlane enemyPlane = new EnemyPlane(GameView.this,"plane-"+(++instanceCounter),null);
                enemyPlane.setYSpeed(rndG.nextInt(10)+1);
                enemyPlane.setCords(new Point(rndG.nextInt(gameViewWidth - enemyPlane.getWidth()) , 0));
                planes.add(enemyPlane);
            }
            // first remove destroyed planes.
            removeDestroyedPlanes();
            return planes;
        }


        public void addHumanPlayer(HumanPlane humanPlane) {
            planes.add(humanPlane);
        }

        public void removeDestroyedPlanes() {
            for(int i=0;i<planes.size();++i) {
                final Sprite sprite = planes.get(i);

                if(sprite != null && sprite.isDestroyed()) {
                    planes.remove(i);
                }
            }
        }


        public List<Sprite> getActivePlanes() {

            final List<Sprite> activePlanes = new ArrayList<>(5);
            for(int i=0;i<planes.size();++i) {
                final Sprite sprite = planes.get(i);

                if(sprite != null && !sprite.isDestroyed()) {
                    activePlanes.add(sprite);
                }
            }
            return activePlanes;

        }

        public List<Sprite> isCollidedWith(Sprite sourceSprite) {

            final int sourceSpriteWidth = sourceSprite.getWidth();
            final int sourceSpriteHeight = sourceSprite.getHeight();
            final List<Sprite> activePlanes = getActivePlanes();

            final List<Sprite> collidedWithPlanes = new ArrayList<>(5);
            for(Sprite targetSprite:activePlanes) {
                if      (
                        (sourceSprite.cords.x >= targetSprite.cords.x
                        &&sourceSprite.cords.x <= targetSprite.cords.x + targetSprite.getWidth()
                        &&sourceSprite.cords.x + sourceSpriteWidth >= targetSprite.cords.x)
                        &&(sourceSprite.cords.y >= targetSprite.cords.y
                        &&sourceSprite.cords.y <= targetSprite.cords.y + targetSprite.getHeight()
                        &&sourceSprite.cords.y + sourceSpriteHeight >= targetSprite.cords.y)
                        &&(!sourceSprite.equals(targetSprite))
                        ){
                    collidedWithPlanes.add(targetSprite);
                }
            }
            return collidedWithPlanes;
        }

    }

}
