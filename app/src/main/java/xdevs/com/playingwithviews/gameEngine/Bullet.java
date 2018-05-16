package xdevs.com.playingwithviews.gameEngine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import java.util.List;

import xdevs.com.playingwithviews.R;

/**
 * Created by syedt on 1/24/2018.
 */

public final class Bullet extends Sprite {


    private int ySpeed;
    private Motion motion = Motion.DOWNWARD;

    public Bullet(GameView gameView, String name, Bitmap bitmap, Point cords) {
        super(gameView, name, bitmap, cords);
    }

    public Bullet(GameView gameView, String name, int id, Point cords) {
        super(gameView, name, id, cords);
    }

    public Bullet(GameView gameView,String name , Point cords) {

        this.gameView = gameView;
        if(bitmap == null) {
            bitmap=Utills.getBitmap(gameView.getContext(), R.drawable.small_circle_bullet);
            setBitmap(bitmap);
        }
        this.name = name;
        this.cords = cords;
    }
    public void setYSpeed(int speed) {
        this.ySpeed = speed;
    }
    public void setBulletMotion(Motion mt) {
        this.motion = mt;
    }
    @Override
    public void onDraw(Canvas canvas) {

        final int height = getHeight();
        if(cords.y > gameView.gameViewHeight + height || cords.y < 0 - height  ) {
            kill();
            return;
        }

        canvas.drawBitmap(bitmap,cords.x,cords.y,null);
        // bullet damage caused based on its tile type
        causeDamage();
        if(motion == Motion.UPWARD) {
            cords.y -= ySpeed;
        } else {
            cords.y += ySpeed;
        }

    }

    private void causeDamage() {
        final List<Sprite> collidedWith = gameView.planesAndCollisionHandler.isCollidedWith(this);
        for(Sprite sprite:collidedWith) {
            if(sprite instanceof HumanPlane && spriteType == Type.AI ) {
                sprite.causeDamage(life);
                this.causeDamage(life);
            }
            if(sprite instanceof EnemyPlane && spriteType == Type.HUMAN) {
                sprite.causeDamage(life);
                this.causeDamage(life);
                gameView.addScore(10);
            }
        }
    }
    public enum BulletType {
        MACHINE_GUN,
        MISSILE
    }
}
