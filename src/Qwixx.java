import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Qwixx {
    private Player player;
    private AIPlayer ai;
    private boolean end;
    private ArrayList<Dice> diceSet;
    private Map<String, Integer> colToNum;

    public Qwixx(Player player, AIPlayer ai) {
        this.player = player;
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
        while (!end) {
            Map<Integer, Integer> toss = new HashMap<>();
            int white1 = 0;
            int white2 = 0;
            for (Dice d : diceSet) {
                d.rollDice();
                if (d.equals(diceSet.get(1))) {
                    white1 = d.getValue();
                }else if (d.equals(diceSet.get(2))) {
                    white2 = d.getValue();
                } else {
                    toss.put(colToNum.get(d.getColor()), d.getValue());
                }
            }



            // Need Code: Ask player and AI if he wants to take the number and if so on what color he wants to cross

            int[] com1 = new int[4];
            int[] com2 = new int[4];
            for (int i = 0; i < com1.length; i++) {
                com1[i] = white1 + toss.get(i);
                com2[i] = white2 + toss.get(i);
            }

            // Need Code: Ask player and AI if he wants to take a number from the two arrays
            // and if so on what color he wants to cross

            // Remove the dice if the corresponding color bar is locked
            for (int i = 0; i < this.player.getScoresheet.getValidRows.length; i++) {
                if (!this.player.getScoresheet.getValidRows[i] || !this.ai.getScoresheet.getValidRows[i]) {
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

    public void removeDice(Dice dice) {
        diceSet.remove(dice);
    }

    public void checkEnd() {
        if (this.player.getScoresheet.getPenaltyValue() == 4 || this.ai.getScoresheet.getPenatyValue() == 4 ||
                this.player.getScoresheet.getLocks == 2 || this.ai.getScoreSheet.getLocks == 2) {
            this.end = true;
        }
    }

    public void win() {
        System.out.println("GAME OVER");
        if (this.player.getScoreSheet.getTotalScore() > this.ai.getScoreSheet.getTotalScore()) {
            System.out.println("You win!");
        } else if (this.player.getScoreSheet.getTotalScore() < this.ai.getScoreSheet.getTotalScore()) {
            System.out.println("AI wins");
        } else {
            System.out.println("Draw");
        }
    }
}



//Need code: we need to program later a pop-up when a clicked number on the scoresheet is not possible with the dices