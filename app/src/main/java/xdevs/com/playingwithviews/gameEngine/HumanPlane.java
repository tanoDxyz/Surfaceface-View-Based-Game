package xdevs.com.playingwithviews.gameEngine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import static xdevs.com.playingwithviews.gameEngine.GameView.instanceCounter;

/**
 * Created by syedt on 1/25/2018.
 */

public final class HumanPlane extends Sprite {

    private Bullet[] bullets = new Bullet[3];
    public HumanPlane(GameView gameView, String name, Bitmap bitmap, Point cords) {
        super(gameView, name, bitmap, cords);
        setSpriteType(Type.HUMAN);
    }

    public HumanPlane(GameView gameView, String name, int id, Point cords) {
        super(gameView, name, id, cords);
        setSpriteType(Type.HUMAN);
    }


    public HumanPlane(GameView gameView , String name , int id ) {
        final Context context = gameView.getContext();
        this.gameView = gameView;
        this.name = name;
        this.bitmap = Utills.getBitmap(context,id);
        this.cords = new Point(10,800);
        setSpriteType(Type.HUMAN);

    }

    private synchronized void fire() {

        for(int i=0;i<bullets.length;++i) {
            if(bullets[i] == null || bullets[i].isDestroyed()) {
                Point bulletLaunchPad = null;
                if(i == 0) {
                    bulletLaunchPad = getSpecialBulletLaunchPad(cords.x,cords.y);
                } else if(i == 1) {
                    bulletLaunchPad = getLeftBulletLaunchPad(cords.x,cords.y);
                } else {
                    bulletLaunchPad = getRightBulletLaunchPad(cords.x,cords.y);
                }
                bullets[i] = new Bullet(gameView,"human_bullet-"+(++instanceCounter),bulletLaunchPad);
                bullets[i].setLife(life);
                bullets[i].setBulletMotion(Motion.UPWARD);
                bullets[i].setYSpeed(3);
                bullets[i].setSpriteType(Type.HUMAN);
            }
        }
    }


    @Override
    public synchronized void onDraw(Canvas canvas) {

        if(isDestroyed()) {
            return;
        }

        // first bullet fire code and than
        fire();

        // bullet drawCode
        for(Bullet bullet:bullets) {
            bullet.onDraw(canvas);
        }
        canvas.drawBitmap(bitmap,cords.x,cords.y,null);
    }



    public synchronized boolean isTouched(int x , int y) {

        return ((x >= this.cords.x && x <= this.cords.x + getWidth())
                && (y >= this.cords.y && y <= this.cords.y + getHeight()));
    }

    public  Point getLeftBulletLaunchPad(int x , int y) {

        final int leftX = x + (getWidth()/4);
        final int leftY = y + (getHeight()/2);

        return new Point(leftX,leftY);
    }

    public  Point getRightBulletLaunchPad(int x , int y) {
        final int width = getWidth();
        final int rightX = x + width - (width/3);
        final int rightY = y + (getHeight()/2);

        return new Point(rightX, rightY);
    }

    public  Point getSpecialBulletLaunchPad(int x , int y) {
        final int specialX = x + (getWidth()/2 - 5);
        final int specialY = y + (15);
        return new Point(specialX, specialY);
    }
}
