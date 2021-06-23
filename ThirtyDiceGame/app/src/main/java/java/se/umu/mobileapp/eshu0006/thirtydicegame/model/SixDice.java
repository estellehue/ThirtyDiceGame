package java.se.umu.mobileapp.eshu0006.thirtydicegame.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class representing a set of six dice.
 */
public class SixDice implements Serializable{
    private ArrayList<OneDice> sixDice;

    /**
     * Constructor: creating an instance of a set of six dice.
     */
    public SixDice() {
        sixDice = new ArrayList(Arrays.asList(new OneDice(), new OneDice(), new OneDice(), new OneDice(), new OneDice(), new OneDice()));
    }

    /**
     * Method return a string representation of the specified dice.
     * String printed in the list of game round scores.
     * @param i The index of the dice to get the string representation of.
     * @return The string representation of the specified dice.
     */
    public String getDiceColorAndValue(int i) {
        String diceStringRepresentation = sixDice.get(i).getDiceColor() + sixDice.get(i).getDiceValue();
        return diceStringRepresentation;
    }

    /**
     * Method collecting the value of the six dice in an array.
     * @return diceValues An array containing the value of the six dice.
     */
    public ArrayList<Integer> getSixDiceValues() {
        ArrayList<Integer> diceValues = new ArrayList();
        for (OneDice d : sixDice)
            diceValues.add(d.getDiceValue());
        return diceValues;
    }

    /**
     * Method resetting the color of all six dice to white.
     */
    public void resetAllDiceColors() {
        for (OneDice d : sixDice)
            d.setDiceColor("white");
    }

    /**
     * Method resetting the dice to empty white images.
     */
    public void resetAllDiceToEmpty() {
        for (OneDice d : sixDice) {
            d.setDiceColor("white");
            d.setDiceValue(0);
        }
    }

    /**
     * Method re-rolling all not marked dice. The marked dice, in grey, remain unrolled.
     */
    public void reRollAllNotMarkedGreyDice() {
        for (OneDice d : sixDice) {
            if (!d.getDiceColor().equals("grey")) {
                d.throwNewDiceValue();
                d.setDiceColor("white");
            }
        }
    }

    /**
     * Method activated when a dice image is clicked.
     * The method turns the dice grey if white or red.
     * If the dice is grey already, the method turns it white.
     * @param i The index for the dice to change color on.
     */
    public void flipDiceColorWhiteGrey(int i) {
        if(sixDice.get(i).getDiceColor().equals("white"))
            sixDice.get(i).setDiceColor("grey");

        else if(sixDice.get(i).getDiceColor().equals("grey"))
            sixDice.get(i).setDiceColor("white");

        else { // if dice color is red, make it grey and all other white
            for (OneDice d : sixDice)
                d.setDiceColor("white");
            sixDice.get(i).setDiceColor("grey");
        }
    }

    /**
     * Method activated when the low score options is clicked.
     * The method turns the dice red if the dice is of
     * value 3 or lower.
     */
    public void flipDiceColorToRedForLowScore() {
        for (OneDice d : sixDice) {
            int maxLimitForLowScore = 4;
            if (d.getDiceValue() < maxLimitForLowScore)
                d.setDiceColor("red");
        }
    }

    /**
     * Method activated when the different score options are clicked, except low.
     * The method turns the dice red if it is part of the best combination for the requiredSum.
     * The dice for the combination are matched to the current dice face values to identify
     * which dice to turn red.
     * ex. if requiredSum is 7, then flipDiceColorToRedForScore(9) results in:
     * current dice faces = [1, 2, 3, 4, 5, 6]
     * best combination for sum 9 = [[3, 6], [4, 5]]
     * -> red dices = [3, 4, 5, 6]
     *
     * @param requiredSum The required sum for the combinations to have
     */
    public void flipDiceColorToRedForScore(int requiredSum) {
        ArrayList<Integer> bestDiceCombination = Score.getDiceUsedForScore(getSixDiceValues(), requiredSum); // Gets the best dice combinations for the score of the requiredSum
        ArrayList<Integer> copyDiceFaces = new ArrayList<>(getSixDiceValues());
        ArrayList<Integer> diceValuesChecked = new ArrayList();

        for (Integer value : bestDiceCombination)               // loop through the values of the best dice combination
            for (Integer i = 0; i < copyDiceFaces.size(); i++)  // loop through the current dice faces
                if (value.equals(copyDiceFaces.get(i)))         // if the bestDiceCombination dice value matches the copyDiceFaces dice value
                    if (!diceValuesChecked.contains(i)) {       // and if the dice value has not already been checked
                        diceValuesChecked.add(i);
                        sixDice.get(i).setDiceColor("red");     // set the dice to color red
                        break;
                    }
    }

}

