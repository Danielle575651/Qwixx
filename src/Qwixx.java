import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class Qwixx extends Component implements ActionListener {
    private HumanPlayer human;
    private AIPlayer ai;
    private boolean end;
    private ScoreSheetHumanPlayerGUI scoreSheetHumanPlayer;
    private DiceGUI diceGUI;
    private final int NUMBER_OF_COLOR = 4;
    private String activePlayer;

    private JButton restartGame = new JButton();
    private JButton gameRules = new JButton();
    private JPanel infoPanel = new JPanel();
    private JButton finish = new JButton();
    private JButton skip = new JButton();
    private JButton turn = new JButton();
    private int clicksOnRestartGame = 0;

    public Qwixx(HumanPlayer human, AIPlayer ai) {
        this.human = human;
        this.ai = ai;
        this.scoreSheetHumanPlayer = new ScoreSheetHumanPlayerGUI(human);
        this.diceGUI = new DiceGUI();
        this.activePlayer = "Game is not started yet.";
        end = false;
        //humanFirst();
        finish = scoreSheetHumanPlayer.finished;
        finish.addActionListener(this);
        skip = scoreSheetHumanPlayer.skipRound;
        skip.addActionListener(this);
        this.human.changeState();
    }

    public void playGame() {
        //humanFirst();
        //updateTurnButton();

        while (!end) {
            if (this.human.getState()) {
                //System.out.println("human turn");
                //if (this.endRound) {
                // If it is the human players turn
                System.out.println("Human turn");
                //humanCheck(diceGUI.getCurrentPoints());
                //this.ai.bestChoiceNonActive(diceGUI.getCurrentPoints());
                //}
            } else {
                diceGUI.nextRoundButton().doClick();
                this.ai.bestChoiceActive(diceGUI.getCurrentPoints());

                //    humanCheck(diceGUI.getCurrentPoints());
                //    System.out.println("check");

            }

            for (int i = 0; i < NUMBER_OF_COLOR; i++) {
                if (!this.human.sheet.getValidRow(i) || !this.ai.sheet.getValidRow(i)) {
                    // If one player closes a row, then the color should also disappear for the other player after
                    // having chosen their dice combination (including the color that may now disappear)
                    this.human.sheet.removeColor(i);
                    this.ai.sheet.removeColor(i);

                    // Also remove the corresponding die from the game
                    for (Dice d : this.diceGUI.getDiceSet()) {
                        if (!d.isRemoved()) {
                            if (d.getColor() == i + 2) {
                                diceGUI.removeDice(d);
                            }
                        }
                    }
                }
            }

            checkEnd();
            this.human.changeState();
            this.ai.changeState();
            //updateTurnButton();
        }
        this.scoreSheetHumanPlayer.updatePanelWhenFinished();
        this.ai.gui.updatePanelWhenFinished(this.ai.getSheet());

    }

    public void playGame2() {
        //updateTurnButton();

        for (int i = 0; i < NUMBER_OF_COLOR; i++) {
            if (!this.human.sheet.getValidRow(i) || !this.ai.sheet.getValidRow(i)) {
                // If one player closes a row, then the color should also disappear for the other player after
                // having chosen their dice combination (including the color that may now disappear)
                this.human.sheet.removeColor(i);
                this.ai.sheet.removeColor(i);

                // Also remove the corresponding die from the game
                for (Dice d : this.diceGUI.getDiceSet()) {
                    if (!d.isRemoved()) {
                        if (d.getColor() == i + 2) {
                            diceGUI.removeDice(d);
                        }
                    }
                }
            }
            // updateTurnButton();
        }
    }


    //public void humanFirst() {
    //Dice d = this.diceGUI.getDiceSet()[0];
    //d.rollDice();
    //if (d.getValue() <= 3) {
    //    this.human.changeState();
    //} else {
    //    this.ai.changeState();
    //}
    //   this.human.changeState();
    //}


    public void checkEnd() {
        if (this.human.sheet.getPenaltyValue() == 4 || this.ai.sheet.getPenaltyValue() == 4 ||
                this.human.sheet.getLocks() == 2 || this.ai.sheet.getLocks() == 2) {
            // getLocks returns the number of colors that are removed from the game, when 2 colors are removed or
            // one player gets 4 penalties then the game is ended.
            this.end = true;
        }
    }

    public void updateActivePlayer() {
        if (this.human.isActive) {
            if (this.human.getName() == " ") {
                this.activePlayer = "Human player";
            } else {
                this.activePlayer = this.human.getName();
            }
        } else if (this.ai.isActive) {
            this.activePlayer = this.ai.getName();
        } else {
            this.activePlayer = "Game is not started yet.";
        }
    }


    /*public void updateTurnButton() {
        this.infoPanel.removeAll();
        this.infoPanel.revalidate();
        this.infoPanel.repaint();
        infoPanel.setLayout(new GridLayout(3, 1));
        infoPanel.add(restartGame);
        infoPanel.add(gameRules);
        JLabel turn = new JLabel();
        updateActivePlayer();
        turn.setText("Turn of: " + this.activePlayer);
        infoPanel.add(turn);
        infoPanel.setSize(new Dimension(100, 100));
    }
*/
    public boolean humanCheck(int[] points) {
        //if (scoreSheetHumanPlayer.getRoundIsEnded()) {
        List<String> lastCrossed = scoreSheetHumanPlayer.getLastCrossedNumbers();

        // Each element (indices) consists of 2 numbers (here in String format) where the first is the number of the color
        // and the second is the number crossed. 04 is for example a red 4
        for (String indices : lastCrossed) {
            // If the crossed number is not valid according to the dice value, display an error message
            if (!human.numIsValid(Integer.parseInt(indices.substring(0, 1)), Integer.parseInt(indices.substring(1)), points, human.isActive)) {
                scoreSheetHumanPlayer.displayErrorMessageRemote(lastCrossed.size());
                return false;
            }
        }

        // lastCrossed contains at maximum 2 elements, if more an error message would already be displayed in the GUI
        // If human is not the active player but wants to cross 2 numbers, then also an error message would already be displayed in the GUI
        if (lastCrossed.size() == 2) {
            for (String indices : lastCrossed) {
                for (String indices2 : lastCrossed) {
                    for (int colorCombination : human.getColorComb(points)) {
                        int whiteValue = Integer.parseInt(indices.substring(1)); // The value of the white combination
                        int colorValue = Integer.parseInt(indices2.substring(1)); // The value of the colored combination
                        int whiteColorNumber = Integer.parseInt(indices.substring(0, 1)); // The row in which the white combination is crossed
                        int colorNumber = Integer.parseInt(indices2.substring(0, 1)); // The row in which the color combination is crossed

                        // If 2 dices are chosen, then it has to be a white combination and a colored combination, but not only colored com
                        if (!indices.equals(indices2) && human.getWhiteComb(points) == whiteValue &&
                                colorCombination == colorValue) {
                            // If the colored and white combination are crossed in the same row, first white and then colored has to be crossed.
                            if (whiteColorNumber == colorNumber && whiteValue > colorValue) {
                                scoreSheetHumanPlayer.displayErrorMessageOrder(lastCrossed.size());
                                return false;
                            }
                        } else { // If not a combination of white dice values and colored dice values are crossed, but only colored dices are crossed
                            scoreSheetHumanPlayer.displayErrorMessageOnlyColored(lastCrossed.size());
                            return false;
                        }
                    }
                }
            }
        }
        //}
        return true;
    }


    public void restartTheGame(HumanPlayer human) {
        this.human = new HumanPlayer(human.getName());
        this.ai = new AIPlayer();
        this.scoreSheetHumanPlayer = new ScoreSheetHumanPlayerGUI(human);
        this.diceGUI = new DiceGUI();
        end = false;
        this.human.changeState();
        //startGame = false;
        //playGame();
    }

    public void createGUI() {
        JFrame frame = new JFrame();
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 800));
        frame.getContentPane().setBackground(new Color(204, 204, 204));
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        JPanel humanPanel = this.scoreSheetHumanPlayer.getScoreSheetHumanPlayer();
        humanPanel.setMinimumSize(humanPanel.getPreferredSize());
        JPanel aiPanel = this.ai.gui.getScorePanel();
        aiPanel.setMinimumSize(humanPanel.getPreferredSize());

        JPanel dicePanel = this.diceGUI.getDicePanel();
        dicePanel.setSize(new Dimension(500, 100));

        restartGame.setText("Click to start a new game");
        gameRules.setText("Click here to view the rules of Qwixx");
        this.turn = new JButton(this.activePlayer);
        restartGame.addActionListener(this);
        gameRules.addActionListener(this);
        this.turn.addActionListener(this);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1));
        infoPanel.add(restartGame);
        infoPanel.add(gameRules);
        infoPanel.add(turn);
        infoPanel.setSize(new Dimension(100, 100));

        JPanel middlePanel = new JPanel();
        BoxLayout layout = new BoxLayout(middlePanel, BoxLayout.X_AXIS);
        middlePanel.setLayout(layout);
        middlePanel.add(infoPanel);
        middlePanel.add(dicePanel);

        frame.add(aiPanel, BorderLayout.PAGE_END);
        frame.add(humanPanel, BorderLayout.PAGE_START);
        frame.add(middlePanel, BorderLayout.CENTER);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == restartGame) {
            clicksOnRestartGame++;

            if (clicksOnRestartGame % 2 != 0) {
                JOptionPane.showMessageDialog(this, "When clicking this button you will restart the game. Click again if you are sure that you want to restart.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                restartTheGame(this.human);
                this.activePlayer = this.human.getName();
                this.turn.setText(this.activePlayer);
            }
        } else if (e.getSource() == this.finish || e.getSource() == this.skip) {
            if (!end) {
                if (this.human.isActive) {
                    if (humanCheck(diceGUI.getCurrentPoints())) { // Should add a condition that AI only generates new dice values when human has finished round with valid dice values
                        this.ai.bestChoiceNonActive(diceGUI.getCurrentPoints());
                        diceGUI.nextRoundButton().doClick();
                    }
                } else {
                    this.ai.bestChoiceActive(diceGUI.getCurrentPoints());
                    humanCheck(diceGUI.getCurrentPoints());
                }
                this.human.changeState();
                this.ai.changeState();
                updateActivePlayer();
                this.turn.setText(this.activePlayer);
                playGame2();
            } else {
                this.scoreSheetHumanPlayer.updatePanelWhenFinished();
                this.ai.gui.updatePanelWhenFinished(this.ai.getSheet());
            }

            checkEnd();

            if (end) {
                this.scoreSheetHumanPlayer.updatePanelWhenFinished();
                this.ai.gui.updatePanelWhenFinished(this.ai.getSheet());
            }
        } else if (e.getSource() == this.gameRules) {
            JOptionPane.showMessageDialog(this, "Game rules",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        HumanPlayer human = new HumanPlayer("");
        AIPlayer ai = new AIPlayer();
        Qwixx qwixxGame = new Qwixx(human, ai);
        qwixxGame.createGUI();
        qwixxGame.playGame2();
    }
}
