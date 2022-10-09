import java.util.Random;

/**
 * A class that generates a dice with specified color and random number
 *
 * @author Amber Cuijpers, Danielle Lam, Khue Nguyen, Yu-Shan Cho, Yuntong Wu
 */
public class Dice {
    private String color;
    private int value;
    private final int maxPoint;
    private final int minPoint;

    /**
     * Construct a dice with specified color
     * @param color specified color of the dice
     */
    public Dice(String color) {
        this.color = color;
        maxPoint = 6;
        minPoint = 1;
    }

    /**
     * Assign a random integer number between 1 and 6 to the dice
     */
    public void rollDice() {
        Random rand = new Random();
        this.value = this.minPoint + rand.nextInt(this.maxPoint);
    }

    /**
     * Get the color of the dice
     *
     * @return color of the dice
     */
    public String getColor() {
        return this.color;
    }

    /**
     * Get the number of the top side of the dice
     *
     * @return number of the top side of the dice
     */
    public int getValue() {
        return this.value;
    }
}
