import java.util.Random;

/**
 * A class that generates a die with specified color, random number and removal status
 *
 * @author Amber Cuijpers, Danielle Lam, Khue Nguyen, Yu-Shan Cho, Yuntong Wu
 */
public class Dice {
    // Use an integer number to represent the color of a die
    private int color;
    // An integer shows the point on the die when it is tossed
    private int value;
    // A maximum point a die has
    private final int MAX_POINTS = 6;
    // A minimum point a die has
    private final int MIN_POINTS = 1;
    // The state of the die whether it is removed or not
    private boolean isRemoved;

    /**
     * Construct a die with specified color, 0 - white (for the first white die), 1 - white (for the second white die), 2 - red, 3 - yellow, 4 - green, 5 - blue
     * Here we use the above numbers to represent the color each die has
     *
     * @param color specified color of the dice
     */
    public Dice(int color) {
        this.color = color;
        isRemoved = false;
    }

    /**
     * Assign a random integer number between 1 and 6 to the die
     */
    public void rollDice() {
        Random rand = new Random();
        this.value = this.MIN_POINTS + rand.nextInt(this.MAX_POINTS);
    }

    /**
     * Get the color of the die
     *
     * @return color of the die
     */
    public int getColor() {
        return this.color;
    }


    /**
     * Get the number on the top side of the die
     *
     * @return the number on the top side of the die
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Get the state of the die - whether it is removed or not
     *
     * @return the state of the die
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
