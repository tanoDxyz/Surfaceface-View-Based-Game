package xdevs.com.playingwithviews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import xdevs.com.playingwithviews.gameEngine.GameView;

public class GameActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        final GameView gameView = findViewById(R.id.gameView);

        gameView.setScoreView((TextView) findViewById(R.id.scoreView));
        gameView.setPlayerLifeView((TextView) findViewById(R.id.playerLifeView));
    }
}
