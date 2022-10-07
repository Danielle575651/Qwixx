import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Qwixx {
    private final int COLOR_NUMBER = 4;
    private HumanPlayer human;
    private AIPlayer ai;
    private boolean end;
    private ArrayList<Dice> diceSet;
    private Map<String, Integer> colToNum;

    public Qwixx(HumanPlayer human, AIPlayer ai) {
        this.human = human;
        this.ai = ai;
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
                "blue",3);
    }

    public void playGame() {
        startFirst();
        while (!end) {
            if (human.getState()) {
                human.tossDice(diceSet);
                // GUI
                ai.bestChoiceActive(diceSet);
            } else {
                ai.tossDice(diceSet);
                ai.bestChoiceActive(diceSet);
                // GUI
            }


            human.changeState();
            ai.changeState();

            // Check if a  die can be removed from the game
            for (int i = 0; i < COLOR_NUMBER; i++) {
                if (!this.human.sheet.getValidRows(i) || !this.ai.sheet.getValidRows(i)) {
                    for (Dice d: diceSet) {
                        if (colToNum.get(d.getColor()) == i) {
                            removeDice(d);
                        }
                    }
                }
            }
        }

        checkEnd();
        if (end) {
            win();
        }
    }

    public void startFirst() {
        Dice d = diceSet.get(0);
        d.rollDice();
        if (d.getValue() <= 3) {
            human.changeState();
        } else {
            ai.changeState();
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
