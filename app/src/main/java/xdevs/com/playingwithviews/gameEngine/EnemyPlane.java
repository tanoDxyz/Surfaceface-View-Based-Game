package xdevs.com.playingwithviews.gameEngine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import java.util.List;

import xdevs.com.playingwithviews.R;

import static xdevs.com.playingwithviews.gameEngine.GameView.instanceCounter;
import static xdevs.com.playingwithviews.gameEngine.GameView.rndG;

/**
 * Created by syedt on 1/24/2018.
 */
// TODO: 1/26/2018 after you are done with the game enable gc background thread...
public final class EnemyPlane extends Sprite {


    private Bullet bullet;
    private int ySpeed = 1;

    public EnemyPlane(GameView gameView,String name, Bitmap bitmap, Point cords) {
        super(gameView,name, bitmap, cords);
    }

    public EnemyPlane(GameView gameView, String name, int id, Point cords) {
        super(gameView, name, id, cords);
    }
    /**
     * the upper two constructors are for loading variable planes.
     * this one is good for one loading one plane
     * @param gameView
     * @param cords
     */
    public EnemyPlane(GameView gameView,String name , Point cords) {

        this.gameView = gameView;
        if(bitmap == null) {
            bitmap=Utills.getBitmap(gameView.getContext(),R.drawable.v_enemy_plane);
            setBitmap(bitmap);
        }
        this.cords = cords;
        this.name = name;
    }

    public void setYSpeed(int speed) {
        this.ySpeed = speed;
    }
    public int getYSpeed() {
        return ySpeed;
    }


    public void fire(PlanePod planePod , Bullet.BulletType bulletType ) {

        int id = -1;
        int life = -1;

        if(bulletType == Bullet.BulletType.MACHINE_GUN) {
            life = 100;
        }

        Point bulletLaunchPadCords = null;
        if(planePod == PlanePod.CENTER) {
            bulletLaunchPadCords = getSpecialBulletLaunchPad(cords.x,cords.y);
        } else if(planePod == PlanePod.LEFT) {
            bulletLaunchPadCords = getLeftBulletLaunchPad(cords.x,cords.y);
        } else {
            bulletLaunchPadCords = getRightBulletLaunchPad(cords.x,cords.y);
        }
        bullet = new Bullet(gameView,"Enemy_bullet-" + (++instanceCounter) ,bulletLaunchPadCords);
        bullet.setLife(life);
        bullet.setYSpeed(ySpeed + 2);
    }
    @Override
    public void onDraw(Canvas canvas) {

        if(cords.y > gameView.gameViewHeight ) {
            kill();
            return;
        }

        if(bullet == null || bullet.isDestroyed()) {
            final int bulletLoad = rndG.nextInt(2);
            if (bulletLoad == 1) {
                final int bulletPod = rndG.nextInt(3) + 1;
                switch (bulletPod) {
                    case 1:
                        fire(PlanePod.LEFT, Bullet.BulletType.MACHINE_GUN);
                        break;
                    case 2:
                        fire(PlanePod.RIGHT, Bullet.BulletType.MACHINE_GUN);
                        break;
                    case 3:
                        fire(PlanePod.CENTER, Bullet.BulletType.MACHINE_GUN);
                        break;
                    default:
                }
            }
        }
        if( bullet != null && ! bullet.isDestroyed()) {
            bullet.onDraw(canvas);
        }


        // check if plane hit some one
        causeDamageToHumanPlayer();
        canvas.drawBitmap(bitmap,cords.x,cords.y,null);
        cords.y += ySpeed;

    }
    public void causeDamageToHumanPlayer() {
        final List<Sprite> collidedWith = gameView.planesAndCollisionHandler.isCollidedWith(this);
        for(Sprite sprite:collidedWith) {
            System.out.println("START ---------------------------------------------------------------");
            System.out.println("Enemy Plane is " + this.toString());

            if(sprite instanceof HumanPlane) {
                sprite.causeDamage(life);
                this.causeDamage(life);
                gameView.addScore(10);
                System.out.println("yes collided with human player");
            }
            System.out.println("END ---------------------------------------------------------------");
        }
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
        final int specialY = y + (getHeight() - 15);
        return new Point(specialX, specialY);
    }
}
