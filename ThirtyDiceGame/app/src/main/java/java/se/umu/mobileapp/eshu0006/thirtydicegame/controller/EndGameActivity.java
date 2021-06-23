package java.se.umu.mobileapp.eshu0006.thirtydicegame.controller;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.se.umu.mobileapp.eshu0006.thirtydicegame.R;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Controller class for the end game view.
 */
public class EndGameActivity extends AppCompatActivity {

    /**
     * Method starting the activity specified by setContentView.
     * @param savedInstanceState The Bundle in which the state is saved
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        Intent intent = getIntent();

        Button playAgainButton = (Button) findViewById(R.id.playAgainBtn);
        TextView totalGameScore = (TextView) findViewById(R.id.endScoreTxt);

        Integer score = intent.getIntExtra("totalScore", 0);
        totalGameScore.setText(score.toString());

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainGameActivity();
            }
        });
    }

    /**
     * Method opening a new activity for when a new game is to start.
     * The next activity after the end of one game is possibility to
     * start a new game. Creating a
     */
    private void openMainGameActivity() {
        Intent intent = new Intent(this, MainGameActivity.class);
        startActivity(intent);
    }
}
