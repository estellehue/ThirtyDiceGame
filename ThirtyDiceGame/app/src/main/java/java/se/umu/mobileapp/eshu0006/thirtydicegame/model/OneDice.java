package java.se.umu.mobileapp.eshu0006.thirtydicegame.model;

import java.io.Serializable;

/**
 * Class representing one single dice.
 */
public class OneDice implements Serializable {
    private int value;
    private String color;

    /**
     * Constructor: creating an instance of the object dice.
     */
    public OneDice() {
        this.value = 0;
        this.color = "white";
    }

    public int getDiceValue() {
        return value;
    }

    public void setDiceValue(int value) {
        this.value = value;
    }

    /**
     * Method generating a new random number for the throw of a dice.
     */
    public void throwNewDiceValue() {
        this.value = (int)(Math.random()*6) + 1;
    }

    public String getDiceColor() {
        return color;
    }

    public void setDiceColor(String color) {
        this.color = color;
    }
}


