import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Qwixx {
    private HumanPlayer human;
    private AIPlayer ai;
    private boolean end;
    private ArrayList<Dice> diceSet;
    private Map<String, Integer> colToNum;
    private boolean humanFirst;

    private AIGUI aiGUI;

    public Qwixx(HumanPlayer human, AIPlayer ai) {
        this.human = human;
        this.ai = ai;
        this.aiGUI = new AIGUI();
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
        humanFirst();
        while (!end) {
            Dice[] dice = new Dice[diceSet.size()];
            for(int i = 0; i < diceSet.size(); i++) {
                dice[i] = diceSet.get(i);
            }

            if(this.human.getState()) {
                this.human.tossDice(dice);
                // let human do stuff
                this.ai.bestChoiceNonActive(dice);
            } else {
                this.ai.tossDice(dice);
                // let human do stuff
                this.ai.bestChoiceActive(dice);
            }

            for (int i = 0; i < 4; i++) {
                if (!this.human.sheet.getValidRow(i) || !this.ai.sheet.getValidRow(i)) {
                    for (Dice d: diceSet) {
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
        if(diceSet.get(0).getValue() <= 3) {
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
