package xdevs.com.playingwithviews.gameEngine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

/**
 * Created by syedt on 1/24/2018.
 */

public abstract class Sprite {

    /**
     * most behaviour is omitted from this
     * class ...
     * todo always give virgin names.
     */
    protected  Bitmap bitmap;
    protected String name;
    protected Point cords;
    protected GameView gameView;
    protected Status status = Status.ALIVE;
    protected Type spriteType = Type.AI;
    protected int life = 100;


    public boolean equals(Object object) {

        if(!(object instanceof Sprite)) {
            return false;
        }

        Sprite other  = (Sprite) object;
        return (name.equalsIgnoreCase(other.name) && cords.equals(other.cords) && !( other instanceof EnemyPlane));
    }
    public Sprite(GameView gameView , String name, Bitmap bitmap, Point cords) {

       this.bitmap = bitmap;
       this.name = name;
        this.cords = cords;
        this.gameView = gameView;

    }
    public Sprite(){}

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }



    public synchronized int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void setSpriteType(Type type) {
        this.spriteType = type;
    }

    public Type getSpriteType() {
        return this.spriteType;
    }
    public boolean isDestroyed() {
        return status == Status.DEAD;
    }


    public void setCords(Point cords) {
        this.cords = cords;
    }
    public synchronized void causeDamage(int damage) {
        if(status == Status.ALIVE) {
            if((life-=damage) <= 0) {
                status = Status.DEAD;
            }
        }
    }

    public void kill() {
        life = 0;
        status = Status.DEAD;
    }




    public Point getCurrentCoordinates() {
        return this.cords;
    }
    public Sprite(GameView gameView, String name, int id, Point cords) {
        this(gameView,name,Utills.getBitmap(gameView.getContext(),id), cords);
    }

    public String toString() {
        return "Name = " + name + '\n' +
                "Coordinates = " + cords + '\n' +
                "Sprite Type = " + spriteType + '\n' +
                "Status = " + status + '\n' +
                "Life = " + life + '\n';
    }
    public abstract void onDraw(Canvas canvas);


    public enum Status {
        ALIVE,
        DEAD
    }

    public enum Type {
        AI,
        HUMAN
        // these two are enough
    }

    public enum Motion {
        UPWARD,
        DOWNWARD
        // for the time being these two are sufficienttttt
    }

    public enum PlanePod {
        LEFT,
        RIGHT,
        CENTER
    }
}
