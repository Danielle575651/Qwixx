import java.util.Arrays;

public abstract class Player {
    public Scoresheet sheet;
    private final String name;
    private boolean isActive;

    // For a new player, generate a new score sheet
    public Player(String name) {
        this.sheet = new Scoresheet(name);
        this.name = name;
        isActive = false;
    }

    public final void tossDice(Dice[] dice) {
        for(Dice die : dice) {
            die.rollDice();
        }
    }

    public final void crossNumber(int color, int number) {
        if(color == 0 || color == 1) {
            sheet.cross(color, number - 2);
        } else {
            sheet.cross(color, 12 - number);
        }
    }
    
    public final void changeState() {
        if(isActive) {
            isActive = false;
            return;
        }
        
       isActive = true;
    }

    // Get combination of white dice
    public int getWhiteComb(Dice[] dice) {
        return dice[0].getValue() + dice[1].getValue();
    }

    // Compute the color combination
    public int[] getColorComb(Dice[] dice) {
        int numColor = dice.length - 2; // Number of color dice
        int numCombs = 2 * numColor;
        int[] whiteColor = new int[numCombs];

        // Compute the possible combination
        for(int i = 0; i < 4; i++) {
            if(!sheet.getValidRow(i)) { // if a color is not valid then its combinations = 0
                whiteColor[i] = 0;
                whiteColor[i+4] = 0;
                continue;
            }

            whiteColor[i] = dice[0].getValue() + dice[i].getValue();
            whiteColor[i + 4] = dice[1].getValue() + dice[i].getValue();
        }

        return whiteColor;
    }

    // Check if a value if available in the combination
    public boolean numIsValid(int color, int value, Dice[] dice, boolean active) {
        // for non-active, only check white comb
        int whiteComb = getWhiteComb(dice);
        if(!active && value == whiteComb) {
            return true;
        }
        if(!active && value != whiteComb) {
            return false;
        }

        // for active
        if(value == whiteComb) {
            return true;
        }

        int[] comb = getColorComb(dice);
        // Check if the color is invalid
        if(comb[color] == 0 || comb[color + 4] == 0) {
            return false;
        }
        // Check if the value is valid
        if(value == comb[color] || value == comb[color + 4]) {
            return true;
        } else {
            return false;
        }
    }
}
