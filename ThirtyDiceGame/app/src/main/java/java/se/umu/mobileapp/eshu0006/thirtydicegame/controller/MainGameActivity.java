package java.se.umu.mobileapp.eshu0006.thirtydicegame.controller;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.se.umu.mobileapp.eshu0006.thirtydicegame.R;
import java.se.umu.mobileapp.eshu0006.thirtydicegame.model.OneGame;
import java.se.umu.mobileapp.eshu0006.thirtydicegame.model.OneGameRoundScore;
import java.se.umu.mobileapp.eshu0006.thirtydicegame.model.Score;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Controller class for the main game view.
 */
public class MainGameActivity extends AppCompatActivity {
    static final String STATE_OF_GAME = "GameState";
    static final String NAME_ROLL_BUTTON = "RollButton";

    // Create all game round score options
    OneGameRoundScore g3 = new OneGameRoundScore(false, " Low-(1,2,3)");
    OneGameRoundScore g4 = new OneGameRoundScore(false, " 4-Multipler");
    OneGameRoundScore g5 = new OneGameRoundScore(false, " 5-Multipler");
    OneGameRoundScore g6 = new OneGameRoundScore(false, " 6-Multipler");
    OneGameRoundScore g7 = new OneGameRoundScore(false, " 7-Multipler");
    OneGameRoundScore g8 = new OneGameRoundScore(false, " 8-Multipler");
    OneGameRoundScore g9 = new OneGameRoundScore(false, " 9-Multipler");
    OneGameRoundScore g10 = new OneGameRoundScore(false, "10-Multipler");
    OneGameRoundScore g11 = new OneGameRoundScore(false, "11-Multipler");
    OneGameRoundScore g12 = new OneGameRoundScore(false, "12-Multipler");

    ArrayList<OneGameRoundScore> gameScoreOptions = new ArrayList(Arrays.asList(g3, g4, g5, g6, g7, g8, g9, g10, g11, g12));
    OneGame game = new OneGame(gameScoreOptions); // Initiate a game with score options set

    /**
     * Method starting the activity specified by setContentView.
     * @param savedInstanceState The Bundle in which the state is saved
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        // Initiate UI-objects (that will be used)
        final Button endRoundButton = (Button) findViewById(R.id.endRoundBtn);
        final Button rollButton = (Button) findViewById(R.id.rollBtn);
        final ListView scoreOptionsList = findViewById(R.id.gameRoundsList);
        final OneRowAdapter adapter = new OneRowAdapter(MainGameActivity.this, game.getGameRoundScoreOptions());

        scoreOptionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                scoreOptionsListLogic(view, i, endRoundButton);
            }
        });

        // If any saved instance state (load its data)
        if (savedInstanceState != null) {
            game = (OneGame) savedInstanceState.getSerializable(STATE_OF_GAME);
            updateRollButton((String) savedInstanceState.getSerializable(NAME_ROLL_BUTTON));
            if (game.getCurrentChosenRoundScoreOption() > 0)  // If any score option is selected
                game.getGameRoundScoreOptions().get(game.getCurrentChosenRoundScoreOption()).setSelected(true);

            updateUI();
        }

        // Refresh UI
        updateListOfRoundScores();
        updateDiceImages();
        game.updatePreliminaryScore();

        initialOnEndButtonClick(endRoundButton, rollButton);
        initialRollButtonOnClickListeners(rollButton);
        initialDiceOnClickListeners();
        initialScoreOptionsListOnClickListeners(scoreOptionsList, endRoundButton);
    }

    /**
     * Method saving the state of the game in a Bundle.
     * All Serializable implementing classes will be saved
     * in the Bundle.
     * @param savedInstanceState The Bundle to save the state in
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(STATE_OF_GAME, game);
        savedInstanceState.putSerializable(NAME_ROLL_BUTTON, getRollButtonText());
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Method restoring the game to the state it was saved in.
     * @param savedInstanceState The Bundle in which the state is saved
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        game = (OneGame) savedInstanceState.getSerializable(STATE_OF_GAME);
        updateRollButton((String) savedInstanceState.getSerializable(NAME_ROLL_BUTTON));

        if (game.getCurrentChosenRoundScoreOption() > 0) // If any score option is selected
            game.getGameRoundScoreOptions().get(game.getCurrentChosenRoundScoreOption()).setSelected(true);
        updateUI();
    }

    /**
     * Method checking the combinations for the chosen score option,
     * turning the dice red.
     * @param view           The view activity to get the LayoutInflater object from
     * @param position       The index for te game round score option
     * @param endRoundButton The button for ending the round
     */
    private void scoreOptionsListLogic(View view, int position, Button endRoundButton) {
        final OneRowAdapter adapter = new OneRowAdapter(MainGameActivity.this, game.getGameRoundScoreOptions());
        final ListView scoreOptionsList = (ListView) findViewById(R.id.gameRoundsList);
        scoreOptionsList.setAdapter(adapter);
        OneGameRoundScore oneScoreOption = game.getGameRoundScoreOptions().get(position);

        if (game.getClickableGameRoundState() & !game.getGameRoundScoreOptions().get(position).roundHasBeenPlayed()) { // If a score option has not been played
            game.resetAllSelections();
            oneScoreOption.setSelected(true);
            game.getGameRoundScoreOptions().set(position, oneScoreOption);
            game.setCurrentChosenRoundScoreOption(position);
            endRoundButton.setEnabled(true);
            game.setEndedClickable(true);
            game.getSixDice().resetAllDiceColors();
            if (game.getCurrentChosenRoundScoreOption().equals(0)) { // If the first score options, then it is low
                game.getSixDice().flipDiceColorToRedForLowScore();
                updateDiceImages();
            } else {
                game.getSixDice().flipDiceColorToRedForScore(game.getCurrentChosenRoundScoreOption() + 3); // If any other score option but first, then it is all options except low
                updateDiceImages();
            }
            updateListOfRoundScores();
        } else { // If a score option has been played
            oneScoreOption.setSelected(false);
            game.getGameRoundScoreOptions().set(position, oneScoreOption);
            endRoundButton.setEnabled(false);
            game.setEndedClickable(false);
            view.setSelected(false);
            updateListOfRoundScores();
        }
    }

    /**
     * Method for the clickable action in the ListView of all score options.
     * Adding an event listener to the ListView. Method contains the logic
     * for what is to happen when an item in the list is clicked.
     * @param scoreOptionsList The list of score options to listen for clicks in
     * @param endRoundButton   The button for ending the round
     */
    private void initialScoreOptionsListOnClickListeners(final ListView scoreOptionsList, final Button endRoundButton) {
        scoreOptionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                scoreOptionsListLogic(view, position, endRoundButton);
            }
        });
    }

    /**
     * Method for clicking action of the rollButton.
     * Adding an event listener to the button. Method contains the logic
     * for what is to happen when the button is clicked.
     * @param rollButton The button to listen for clicks in
     */
    private void initialRollButtonOnClickListeners(final Button rollButton) {
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rollButton.getText().equals("Start Round") || rollButton.getText().equals("Start Game")) {
                    game.plusRoundNumber();
                    updateRound();
                }
                updateRollButton("Roll dice");

                if (game.getCurrentThrowNumber() > 3)   // If throw number reached 3, no more throws
                    game.setCurrentThrowNumber(0);

                game.setClickableGameRound(true);
                game.getSixDice().reRollAllNotMarkedGreyDice();
                updateDiceImages();
                game.plusThrowNumber();
                updateThrow();
                game.updatePreliminaryScore();

                if (game.getCurrentThrowNumber().equals(3)) { // If on last throw in round, the third, no more throws after
                    rollButton.setEnabled(false);
                    game.setClickableDice(false);
                }
                game.setClickableDice(true);
                updateListOfRoundScores();
            }
        });
    }

    /**
     * Method for the clickable action of the endRoundButton.
     * Adding an event listener to the button. Method contains the logic
     * for what is to happen when the button is clicked.
     * @param endRoundButton The button to listen for clicks in
     * @param rollButton     The button for rolling the dice
     */
    private void initialOnEndButtonClick(final Button endRoundButton, final Button rollButton) {
        endRoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRollButton("Start Round");
                game.getGameRoundScoreOptions().get(game.getCurrentChosenRoundScoreOption()).setRoundPlayed(true);
                endRoundButton.setEnabled(false);
                game.setEndedClickable(false);
                Integer currentEndScore = 0;

                if (game.getCurrentChosenRoundScoreOption().equals(0))  // If round low is chosen, calculate score for low
                    currentEndScore = Score.getSumOfScoreForLow(game.getSixDice().getSixDiceValues());
                else
                    currentEndScore = Score.getSumOfScore(game.getSixDice().getSixDiceValues(), game.getCurrentChosenRoundScoreOption() + 3); // +3 to get the requiredSum correct

                game.setGameRoundEndScore(game.getCurrentChosenRoundScoreOption(), currentEndScore);
                game.setCurrentScore(currentEndScore + game.getCurrentScore());
                updateCurrentScore();

                if (game.getCurrentRoundNumber().equals(9))
                    updateEndButton("End Game");
                if (game.getCurrentRoundNumber().equals(10)) { // If all 10 rounds are played, end game and start new activity
                    rollButton.setEnabled(false);
                    openEndGameActivity();
                    return;
                }

                game.resetSixDice();
                rollButton.setEnabled(true);
                game.setCurrentThrowNumber(0);
                game.setClickableDice(false);
                game.setClickableGameRound(false);
                updateThrow();
                updateRound();
                game.getSixDice().resetAllDiceColors();
                updateDiceImages();
                game.updatePreliminaryScore();
                updateListOfRoundScores();
            }
        });
    }

    /**
     * Method for the clickable action of the six dice image buttons.
     * Adding an event listener to the buttons. Method contains the logic
     * for what is to happen when any of the six dice are clicked.
     */
    private void initialDiceOnClickListeners() {
        final ImageButton diceButton0 = findViewById(R.id.imageDice0);
        final ImageButton diceButton1 = findViewById(R.id.imageDice1);
        final ImageButton diceButton2 = findViewById(R.id.imageDice2);
        final ImageButton diceButton3 = findViewById(R.id.imageDice3);
        final ImageButton diceButton4 = findViewById(R.id.imageDice4);
        final ImageButton diceButton5 = findViewById(R.id.imageDice5);

        diceButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.getClickableDiceState()) {
                    game.getSixDice().flipDiceColorWhiteGrey(0);
                    updateDiceImages();
                }
            }
        });
        diceButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.getClickableDiceState()) {
                    game.getSixDice().flipDiceColorWhiteGrey(1);
                    updateDiceImages();
                }
            }
        });
        diceButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.getClickableDiceState()) {
                    game.getSixDice().flipDiceColorWhiteGrey(2);
                    updateDiceImages();
                }
            }
        });
        diceButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.getClickableDiceState()) {
                    game.getSixDice().flipDiceColorWhiteGrey(3);
                    updateDiceImages();
                }
            }
        });
        diceButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.getClickableDiceState()) {
                    game.getSixDice().flipDiceColorWhiteGrey(4);
                    updateDiceImages();
                }
            }
        });
        diceButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.getClickableDiceState()) {
                    game.getSixDice().flipDiceColorWhiteGrey(5);
                    updateDiceImages();
                }
            }
        });
    }

    /**
     * Method opening a new activity for when the main game is over.
     * The next activity after the game is the end of the game activity.
     */
    private void openEndGameActivity() {
        Intent intent = new Intent(this, EndGameActivity.class);
        Integer test = game.getCurrentScore();
        intent.putExtra("totalScore", test);
        startActivity(intent);
    }

    /**
     * Method calling all updating methods for the User Interface.
     */
    private void updateUI() {
        updateThrow();
        updateRound();
        updateCurrentScore();
        updateDiceImages();
        updateRollButtonEnabled();
        updateEndButtonEnabled();
        updateBlackCheckMarks();
        updateGreenCheckMarks();
        updateListOfRoundScores();
    }

    /**
     * Method updating the UI - the throw number for the current round
     */
    private void updateThrow() {
        final TextView throwText = (TextView) findViewById(R.id.throwTxt);
        throwText.setText("Throw #" + game.getCurrentThrowNumber());
    }

    /**
     * Method updating the UI - the game round number
     */
    private void updateRound() {
        final TextView roundText = (TextView) findViewById(R.id.rndTxt);
        roundText.setText("Round #" + game.getCurrentRoundNumber());
    }

    /**
     * Method updating the UI - the current total score count
     */
    private void updateCurrentScore() {
        final TextView totalScore = (TextView) findViewById(R.id.totalScoreTxt);
        totalScore.setText("Score: " + game.getCurrentScore().toString());
    }

    /**
     * Method updating the UI - the six dice images
     */
    private void updateDiceImages() {
        final ImageView imageDice0 = (ImageView) findViewById(R.id.imageDice0);
        final ImageView imageDice1 = (ImageView) findViewById(R.id.imageDice1);
        final ImageView imageDice2 = (ImageView) findViewById(R.id.imageDice2);
        final ImageView imageDice3 = (ImageView) findViewById(R.id.imageDice3);
        final ImageView imageDice4 = (ImageView) findViewById(R.id.imageDice4);
        final ImageView imageDice5 = (ImageView) findViewById(R.id.imageDice5);

        imageDice0.setImageResource(getResources().getIdentifier(game.getSixDice().getDiceColorAndValue(0), "drawable", getPackageName()));
        imageDice1.setImageResource(getResources().getIdentifier(game.getSixDice().getDiceColorAndValue(1), "drawable", getPackageName()));
        imageDice2.setImageResource(getResources().getIdentifier(game.getSixDice().getDiceColorAndValue(2), "drawable", getPackageName()));
        imageDice3.setImageResource(getResources().getIdentifier(game.getSixDice().getDiceColorAndValue(3), "drawable", getPackageName()));
        imageDice4.setImageResource(getResources().getIdentifier(game.getSixDice().getDiceColorAndValue(4), "drawable", getPackageName()));
        imageDice5.setImageResource(getResources().getIdentifier(game.getSixDice().getDiceColorAndValue(5), "drawable", getPackageName()));
    }

    /**
     * Method updating the UI - the roll button enabled, until last throw
     */
    private void updateRollButtonEnabled() {
        final Button rollButton = (Button) findViewById(R.id.rollBtn);
        if (game.getCurrentThrowNumber() < 3)  // If not at last throw
            rollButton.setEnabled(true);
        else
            rollButton.setEnabled(false);

    }

    /**
     * Method updating the UI - the end button enabled
     */
    private void updateEndButtonEnabled() {
        final Button endButton = (Button) findViewById(R.id.endRoundBtn);
        endButton.setEnabled(game.getEndedClickableState());
    }

    /**
     * Method updating the UI - the black check marks for the score
     */
    private void updateBlackCheckMarks() {
        if (game.getCurrentChosenRoundScoreOption() > -1) { // If a chosen score option exists
            OneGameRoundScore oneScoreOption = game.getGameRoundScoreOptions().get(game.getCurrentChosenRoundScoreOption());
            oneScoreOption.setSelected(true);
            game.getGameRoundScoreOptions().set(game.getCurrentChosenRoundScoreOption(), oneScoreOption);
        }
    }

    /**
     * Method updating the UI - the green check marks for the score
     */
    private void updateGreenCheckMarks() {
        for (int i = 0; i < this.game.getGameRoundScoreOptions().size(); i++) {
            if (game.getGameRoundScoreOptions().get(i).roundHasBeenPlayed()) {  // If any score option has already been played
                OneGameRoundScore oneScoreOption = game.getGameRoundScoreOptions().get(i);
                oneScoreOption.setRoundPlayed(true);
                game.getGameRoundScoreOptions().set(i, oneScoreOption);

            }
        }
    }

    /**
     * Method updating the UI - the list of game round score options
     */
    private void updateListOfRoundScores() {
        final OneRowAdapter adapter = new OneRowAdapter(MainGameActivity.this, game.getGameRoundScoreOptions());
        final ListView scoreOptionsList = (ListView) findViewById(R.id.gameRoundsList);
        scoreOptionsList.setAdapter(adapter);
        game.updateGameRoundScoreOptionNames();
    }

    /**
     * Method updating the UI - the text on the roll button
     * @param text The text to update the button with
     */
    private void updateRollButton(String text) {
        final Button rollButton = (Button) findViewById(R.id.rollBtn);
        rollButton.setText(text);
    }

    /**
     * Method updating the UI - the text on the end button
     * @param text The text to update the button with
     */
    private void updateEndButton(String text) {
        final Button endRoundButton = (Button) findViewById(R.id.endRoundBtn);
        endRoundButton.setText(text);
    }

    /**
     * Method for getting the text on the roll button.
     * @return The text on the roll button
     */
    private String getRollButtonText() {
        final Button rollButton = (Button) findViewById(R.id.rollBtn);
        return rollButton.getText().toString();
    }

}












































