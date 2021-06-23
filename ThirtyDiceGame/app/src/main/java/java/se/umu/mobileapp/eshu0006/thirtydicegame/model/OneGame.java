package java.se.umu.mobileapp.eshu0006.thirtydicegame.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class representing one game of Thirty.
 */
public class OneGame implements Serializable {
    private ArrayList<OneGameRoundScore> gameRoundScoreOptions;
    private SixDice sixDice;
    private int currentThrowNumber;  // 3 throws per game round
    private int currentRoundNumber;  // 10 rounds per game
    private int currentScore;
    private int currentChosenRoundScoreOption;
    private boolean endedClickable;
    private boolean clickableDice;
    private boolean clickableGameRound;

    /**
     * Constructor: creating an instance of one game of Thirty.
     * @param scoreOptions The array of score options from low to 12
     */
    public OneGame(ArrayList<OneGameRoundScore> scoreOptions) {
        this.sixDice = new SixDice();
        this.gameRoundScoreOptions = scoreOptions;
        this.currentThrowNumber = 0;
        this.currentRoundNumber = 0;
        this.currentScore = 0;
        this.currentChosenRoundScoreOption = -1;
        this.clickableDice = false;
        this.clickableGameRound = false;
    }

    public ArrayList<OneGameRoundScore> getGameRoundScoreOptions() {
        return gameRoundScoreOptions;
    }

    public SixDice getSixDice() {
        return sixDice;
    }

    public void resetSixDice() {
        sixDice.resetAllDiceToEmpty();
    }

    public Integer getCurrentThrowNumber() {
        return currentThrowNumber;
    }

    public void setCurrentThrowNumber(int throwNumber) {
        this.currentThrowNumber = throwNumber;
    }

    /**
     * Method incrementing the number of throws.
     */
    public void plusThrowNumber() {
        this.currentThrowNumber++;
    }

    public Integer getCurrentRoundNumber() {
        return currentRoundNumber;
    }

    /**
     * Method incrementing the number of rounds.
     */
    public void plusRoundNumber() {
        this.currentRoundNumber++;
    }

    public Integer getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public Integer getCurrentChosenRoundScoreOption() {
        return currentChosenRoundScoreOption;
    }

    public void setCurrentChosenRoundScoreOption(int currentScoreChoice) {
        this.currentChosenRoundScoreOption = currentScoreChoice;
    }

    public boolean getEndedClickableState() {
        return endedClickable;
    }

    public void setEndedClickable(boolean endClickable) {
        this.endedClickable = endClickable;
    }

    public boolean getClickableDiceState() {
        return clickableDice;
    }

    public void setClickableDice(boolean clickableDice) {
        this.clickableDice = clickableDice;
    }

    public boolean getClickableGameRoundState() {
        return clickableGameRound;
    }

    public void setClickableGameRound(boolean clickableGameRound) {
        this.clickableGameRound = clickableGameRound;
    }

    /**
     * Method updating the name of the game's score option with the new
     * preliminary and end score.
     */
    public void updateGameRoundScoreOptionNames() {
        for (int i = 0; i < this.gameRoundScoreOptions.size(); i++) {
            String pre = "";
            String end = "";

            if (gameRoundScoreOptions.get(i).getPreliminaryScore() < 10)  // If score is one digit; for prettier printout, added 0
                pre = "0" + gameRoundScoreOptions.get(i).getPreliminaryScore();
            else
                pre = "" + gameRoundScoreOptions.get(i).getPreliminaryScore();

            if (gameRoundScoreOptions.get(i).getEndScore() < 10)          // If score is one digit; for prettier printout, added 0
                end = "0" + gameRoundScoreOptions.get(i).getEndScore();
            else
                end = "" + gameRoundScoreOptions.get(i).getEndScore();

            getGameRoundScoreOptions().get(i).setName(gameRoundScoreOptions.get(i).getStartName() + " \t|\t " + pre + " \t|\t " + end); // Uses start name without scores included, as in the new set names
        }
    }

    /**
     * Method setting the preliminary score for the specified, by index, score option.
     * @param index The index for the game round score option
     * @param score The preliminary score to set
     */
    public void setGameRoundPreliminaryScore(int index, int score) {
        this.getGameRoundScoreOptions().get(index).setPreliminaryScore(score);
    }

    /**
     * Method setting the end score for the specified, by index, score option.
     * @param index The index for the game round score option
     * @param score The end score to set
     */
    public void setGameRoundEndScore(int index, int score) {
        this.getGameRoundScoreOptions().get(index).setEndScore(score);
    }

    /**
     * Method updating the all game round score options current preliminary score.
     */
    public void updatePreliminaryScore() {
        for (int i = 0; i < 10; i++)
            if (!getGameRoundScoreOptions().get(i).roundIsBeingPlayed() & clickableGameRound) {// If round is not being played (dice rolling) and the round is clickable
                if (i == 0) {                                                                  // If game round score option is low
                    int sumOfLowScore = Score.getSumOfScoreForLow(sixDice.getSixDiceValues());
                    setGameRoundPreliminaryScore(0, sumOfLowScore);
                } else {
                    int requiredSum = i + 3;
                    int sumOfScore = Score.getSumOfScore(sixDice.getSixDiceValues(), requiredSum);
                    setGameRoundPreliminaryScore(i, sumOfScore);
                }
            }
    }

    /**
     * Method resetting all selections to false, for each game score option.
     */
    public void resetAllSelections() {
        for (OneGameRoundScore gameScoreOption : gameRoundScoreOptions)
            gameScoreOption.setSelected(false);

    }

}
