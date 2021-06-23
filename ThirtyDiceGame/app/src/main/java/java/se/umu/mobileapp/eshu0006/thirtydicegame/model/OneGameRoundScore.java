package java.se.umu.mobileapp.eshu0006.thirtydicegame.model;

import java.io.Serializable;

/**
 * Class representing one single game round score option.
 * Each game round consist of a score to which the dice combinations must sum up.
 * The round's score options are from low, being 3, to 12. The number of game
 * rounds is 10 as well as the number of score options.
 */
public class OneGameRoundScore implements Serializable {
    private String name;            // Name
    private String startName;
    private int preliminaryScore;   // Calculated preliminary score
    private int endScore;           // Final chosen score for this round
    private boolean roundPlayed;    // Has this round been played
    private boolean isSelected;     // If game round is selected or not

    /**
     * Constructor: creating an instance of one game round score option.
     * @param b True or False for if the game round is selected
     * @param name The name of the game round
     */
    public OneGameRoundScore(boolean b, String name) {
        preliminaryScore = 0;
        endScore = 0;
        this.startName = name; // The neme of the score option before actual score values are added to the name
        this.name = name;
        this.roundPlayed = false;
        this.isSelected = b;
    }

    public int getPreliminaryScore() {
        return preliminaryScore;
    }

    public void setPreliminaryScore(int preScore) {
        this.preliminaryScore = preScore;
    }

    public int getEndScore() {
        return endScore;
    }

    public void setEndScore(int endScore) {
        this.endScore = endScore;
    }

    public String getStartName() {
        return startName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoundPlayed(boolean roundPlayed) {
        this.roundPlayed = roundPlayed;
    }

    public boolean roundIsBeingPlayed() {
        return roundPlayed;
    }

    public boolean roundHasBeenPlayed() {
        return roundPlayed;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
