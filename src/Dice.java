import java.util.Random;

/**
 * A class that generates a dice with specified color, random number and removal status
 *
 * @author Amber Cuijpers, Danielle Lam, Khue Nguyen, Yu-Shan Cho, Yuntong Wu
 */
public class Dice {
    private int color;
    private int value;
    private final int MAX_POINTS;
    private final int MIN_POINTS;
    private boolean isRemoved;

    /**
     * Construct a die with specified color, 0 - white, 1 - white, 2 - red, 3 - yellow, 4 - green, 5 - blue
     * Here we use the above numbers to represent the color each die has
     * @param color specified color of the dice
     */
    public Dice(int color) {
        this.color = color;
        MAX_POINTS = 6;
        MIN_POINTS = 1;
        isRemoved = false;
    }

    /**
     * Assign a random integer number between 1 and 6 to the dice
     */
    public void rollDice() {
        Random rand = new Random();
        this.value = this.MIN_POINTS + rand.nextInt(this.MAX_POINTS);
    }

    /**
     * Get the color of the dice
     *
     * @return color of the dice
     */
    public int getColor() {
        return this.color;
    }


    /**
     * Get the number on the top side of the dice
     *
     * @return the number on the top side of the dice
     */
    public int getValue() {
        return this.value;
    }
    
    /**
     * Get the state of the dice - whether it is removed or not
     * @return the state of the dice
     */
    public boolean isRemoved() {
        return this.isRemoved;
    }
    
    /**
     * Remove a die by change its state of removal to true
     */
    public void changeState() {
        this.isRemoved = true;
    }
}
