package xdevs.com.playingwithviews.gameEngine;

import android.graphics.Canvas;

public class GameLoopThread extends Thread {

    final long FPS;

    private GameView view;

    private boolean running = false;



    public GameLoopThread(GameView view , int fps) {

        this.view = view;
        this.FPS = fps;

    }



    public void setRunning(boolean run) {

        running = run;

    }



    @Override

    public void run() {

        long ticksPS = 1000 / FPS;

        long startTime;

        long sleepTime;

        while (running) {


            Canvas c = null;

            startTime = System.currentTimeMillis();

            try {

                c = view.getHolder().lockCanvas();

                synchronized (view.getHolder()) {

                    if(c!=null) {
                        view.draw(c);
                    }

                }

            } finally {

                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                }

            }

            sleepTime = ticksPS-(System.currentTimeMillis() - startTime); // SystemClock no need cause we are not taking into account the sleep or dream or pause.

            try {

                if (sleepTime > 0)

                    sleep(sleepTime);

                else

                    sleep(10);

            } catch (Exception e) {}

        }

    }

}