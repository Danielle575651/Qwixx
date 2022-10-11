import java.util.*;


public class Qwixx {
    private HumanPlayer human;
    private AIPlayer ai;
    private boolean end;
    private ArrayList<Dice> diceSet;
    private Map<String, Integer> colToNum;
    private ScoreSheetHumanPlayerGUI scoreSheetHumanPlayer;
    //private AIGUI aiGUI;

    public Qwixx(HumanPlayer human, AIPlayer ai) {
        this.human = human;
        this.ai = ai;
        //this.aiGUI = new ScoreSheetAIPlayerGUI(); -> The GUI is generated in the AIPlayer object
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
                    // If one player closes a row, then the color should also disappear for the other player after
                    // having chosen their dice combination (including the color that may now disappear)
                    this.human.sheet.removeColor(i);
                    this.ai.sheet.removeColor(i);

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

        win(); // Game has always ended when you reach this point, otherwise you will not break out of the while loop
        this.scoreSheetHumanPlayer.updatePanelWhenFinished();
        this.ai.gui.updatePanelWhenFinished(this.ai.getSheet());
    }

    public void humanFirst() {
        diceSet.get(0).rollDice();
        if (diceSet.get(0).getValue() <= 3) {
            this.human.changeState();
        } else {
            this.ai.changeState();
        }
    }

    public void removeDice(Dice dice) {
        diceSet.remove(dice);
    }

    public void checkEnd() {
        if (this.human.sheet.getPenaltyValue() == 4 || this.ai.sheet.getPenaltyValue() == 4 ||
                this.human.sheet.getLocks() == 2 || this.ai.sheet.getLocks() == 2) {
            // getLocks returns the number of colors that are removed from the game, when 2 colors are removed or
            // one player gets 4 penalties then the game is ended.
            this.end = true;
        }
    }

    public void humanCheck(Dice[] dice) {
        if (scoreSheetHumanPlayer.getRoundIsEnded()) {
            List<String> lastCrossed = scoreSheetHumanPlayer.getLastCrossedNumbers();

            // Each element (indices) consists of 2 numbers (here in String format) where the first is the number of the color
            // and the second is the number crossed. 04 is for example a red 4
            for (String indices : lastCrossed) {
                // If the crossed number is not valid according to the dice value, display an error message
                if (!human.numIsValid(Integer.parseInt(indices.substring(0, 1)), Integer.parseInt(indices.substring(1, 2)), dice, human.isActive)) {
                    scoreSheetHumanPlayer.displayErrorMessageRemote(lastCrossed.size());
                }
            }

            // lastCrossed contains at maximum 2 elements, if more an error message would already be displayed in the GUI
            // If human is not the active player but wants to cross 2 numbers, then also an error message would already be displayed in the GUI
            if (lastCrossed.size() == 2) {
                for (String indices : lastCrossed) {
                    for (String indices2 : lastCrossed) {
                        for (int colorCombination : human.getColorComb(dice)) {
                            int whiteValue = Integer.parseInt(indices.substring(1, 2)); // The value of the white combination
                            int colorValue = Integer.parseInt(indices2.substring(1, 2)); // The value of the colored combination
                            int whiteColorNumber = Integer.parseInt(indices.substring(0, 1)); // The row in which the white combination is crossed
                            int colorNumber = Integer.parseInt(indices2.substring(0, 1)); // The row in which the color combination is crossed

                            // If 2 dices are chosen, then it has to be a white combination and a colored combination, but not only colored com
                            if (!indices.equals(indices2) && human.getWhiteComb(dice) == whiteValue &&
                                    colorCombination == colorValue) {
                                // If the colored and white combination are crossed in the same row, first white and then colored has to be crossed.
                                if (whiteColorNumber == colorNumber && whiteValue > colorValue) {
                                    scoreSheetHumanPlayer.displayErrorMessageOrder(lastCrossed.size());
                                }
                            } else { // If not a combination of white dice values and colored dice values are crossed, but only colored dices are crossed
                                    scoreSheetHumanPlayer.displayErrorMessageOnlyColored(lastCrossed.size());
                            }
                        }
                    }
                }
            }
        }
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
