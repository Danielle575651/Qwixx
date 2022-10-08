import java.util.*;


public class Qwixx {
    private HumanPlayer human;
    private AIPlayer ai;
    private boolean end;
    private ArrayList<Dice> diceSet;
    private Map<String, Integer> colToNum;
    private boolean humanFirst;
    private ScoreSheetHumanPlayerGUI scoreSheetHumanPlayer;
    private AIGUI aiGUI;

    public Qwixx(HumanPlayer human, AIPlayer ai) {
        this.human = human;
        this.ai = ai;
        this.aiGUI = new AIGUI();
        this.scoreSheetHumanPlayer = new ScoreSheetHumanPlayerGUI(human);
        end = false;

        Dice white1 = new Dice("white");
        Dice white2 = new Dice("white");
        Dice red = new Dice("red");
        Dice yellow = new Dice("yellow");
        Dice green = new Dice("green");
        Dice blue = new Dice("blue");
        diceSet = new ArrayList<>();
        Collections.addAll(diceSet, white1, white2, red, yellow, green, blue);

        colToNum = Map.of(
                "red", 0,
                "yellow", 1,
                "green", 2,
                "blue", 3);
    }

    public void playGame() {
        humanFirst();
        while (!end) {
            Dice[] dice = new Dice[diceSet.size()];
            for (int i = 0; i < diceSet.size(); i++) {
                dice[i] = diceSet.get(i);
            }

            // If it is the human players turn
            if (this.human.getState()) {
                this.human.tossDice(dice);
                humanCheck(dice);
                this.ai.bestChoiceNonActive(dice);
            } else {
                this.ai.tossDice(dice);
                this.ai.bestChoiceActive(dice);
                humanCheck(dice);
            }

            for (int i = 0; i < 4; i++) {
                if (!this.human.sheet.getValidRow(i) || !this.ai.sheet.getValidRow(i)) {
                    for (Dice d : diceSet) {
                        if (colToNum.get(d.getColor()) == i) {
                            removeDice(d);
                        }
                    }
                }
            }

            checkEnd();
            this.human.changeState();
            this.ai.changeState();
        }

        if (end) {
            win();
        }
    }

    public void humanFirst() {
        diceSet.get(0).rollDice();
        if (diceSet.get(0).getValue() <= 3) {
            humanFirst = true;
            this.human.changeState();
        } else {
            humanFirst = false;
            this.ai.changeState();
        }
    }

    public void removeDice(Dice dice) {
        diceSet.remove(dice);
    }

    public void checkEnd() {
        if (this.human.sheet.getPenaltyValue() == 4 || this.ai.sheet.getPenaltyValue() == 4 ||
                this.human.sheet.getLocks() == 2 || this.ai.sheet.getLocks() == 2) {
            this.end = true;
        }
    }

    public boolean humanCheck(Dice[] dice) {
        Set<String> lastCrossed = scoreSheetHumanPlayer.getLastCrossedNumbers();

        // Each element (indices) consists of 2 numbers (here in String format) where the first is the number of the color
        // and the second is the number crossed.
        for (String indices : lastCrossed) {
            // If the crossed number is not valid according to the dice value, display an error message (Something like):
            // The number(s) you have just crossed are not valid (e.g. they do not correspond to the dice values). Uncross the button you have just clicked and make sure to cross the number that corresponds to the dice values and hit the finish button or skip this round.
            if (!human.numIsValid(Integer.valueOf(indices.substring(0)), Integer.valueOf(indices.substring(1)), dice, human.isActive)) {
                scoreSheetHumanPlayer.displayErrorMessageRemote();
                return false;
            }
        }

        // lastCrossed contains at maximum 2 elements, if more an error message would already be displayed in the GUI
        if (lastCrossed.size() == 2) {
            for (String indices : lastCrossed) {
                for (String indices2 : lastCrossed) {
                    for (int colorCombination : human.getColorComb(dice)) {
                        int whiteValue = Integer.valueOf(indices.substring(1)); // The value of the white combination
                        int colorValue = Integer.valueOf(indices2.substring(1)); // The value of the colored combination
                        int whiteColorNumber = Integer.valueOf(indices.substring(0)); // The row in which the white combination is crossed
                        int colorNumber = Integer.valueOf(indices2.substring(0)); // The row in which the color combination is crossed

                        // If 2 dices are chosen, then it has to be a white combination and a colored combination, but not only colored com
                        if (!indices.equals(indices2) && human.getWhiteComb(dice) == whiteValue &&
                                colorCombination == colorValue) {
                            // If the colored and white combination are crossed in the same row, first white and then colored has to be crossed.
                            if (whiteColorNumber == colorNumber && whiteValue > colorValue) {
                                // Display error message: The order in which you crossed the number is not correct. If you want to cross numbers in the same row, you first have to cross the combination of the white dice and then a colored combination.
                                scoreSheetHumanPlayer.displayErrorMessageOrder();
                                return false;
                            }
                            return true;
                        } else { // If not a combination of white dice values and colored dice values are crossed, but only colored dices are crossed
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public void win() {
//        System.out.println("GAME OVER");
//        if (this.player.sheet.getTotalScore() > this.ai.sheet.getTotalScore()) {
//            System.out.println("You win!");
//        } else if (this.player.sheet.getTotalScore() < this.ai.sheet.getTotalScore()) {
//            System.out.println("AI wins");
//        } else {
//            System.out.println("Draw");
//        }
    }
}
