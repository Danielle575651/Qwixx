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
    private JButton finish;
    private JButton skip;
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
    }

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
            if (this.human.getName().equals(" ")) {
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

    public boolean humanCheck(int[] points) {
        List<String> lastCrossed = scoreSheetHumanPlayer.getLastCrossedNumbers();

        // Each element (indices) consists of 2 numbers (here in String format) where the first is the number of the color
        // and the second is the number crossed. 04 is for example a red 4
        for (String indices : lastCrossed) {
            // If the crossed number is not valid according to the dice value, display an error message
            if (human.numIsValid(Integer.parseInt(indices.substring(0, 1)), Integer.parseInt(indices.substring(1)), points, human.isActive) == false) {
                scoreSheetHumanPlayer.displayErrorMessageRemote(lastCrossed.size());
                return false;
            }
        }

        // lastCrossed contains at maximum 2 elements, if more an error message would already be displayed in the GUI
        // If human is not the active player but wants to cross 2 numbers, then also an error message would already be displayed in the GUI
        if (lastCrossed.size() == 2) {
            for (String lastCross : lastCrossed) {
                for (String lastCross2 : lastCrossed) {
                    if (!lastCross.equals(lastCross2)) { // Do not compare the same crosses
                        // First we check if the crossed number corresponds to the white dice value and a colored dice value
                        int whiteDiceValue = human.getWhiteComb(points);
                        int[] colorDiceValues = human.getColorComb(points);
                        int colorFirstCross = Integer.parseInt(lastCross.substring(0, 1)); // White dice can be used at any color
                        int valueFirstCross = Integer.parseInt(lastCross.substring(1));
                        int colorSecondCross = Integer.parseInt(lastCross2.substring(0, 1));
                        int valueSecondCross = Integer.parseInt(lastCross2.substring(1));

                        if (whiteDiceValue == valueFirstCross &&
                                (colorDiceValues[colorSecondCross] == valueSecondCross || colorDiceValues[colorSecondCross + 4] == valueSecondCross)) {
                            if (colorFirstCross == 0 || colorFirstCross == 1) {
                                if (colorFirstCross == colorSecondCross && whiteDiceValue > valueSecondCross) {
                                    scoreSheetHumanPlayer.displayErrorMessageOrder(lastCrossed.size());
                                    return false;
                                }
                            } else if (colorFirstCross == 2 || colorFirstCross == 3) {
                                if (colorFirstCross == colorSecondCross && whiteDiceValue < valueSecondCross) {
                                    scoreSheetHumanPlayer.displayErrorMessageOrder(lastCrossed.size());
                                    return false;
                                }
                            }
                            return true;
                        }
                    }
                }
            }
        }

            if (lastCrossed.size() == 2) {
                for (String lastCross : lastCrossed) {
                    for (String lastCross2 : lastCrossed) {
                        if (!lastCross.equals(lastCross2)) { // Do not compare the same crosses
                            // First we check if the crossed number corresponds to the white dice value and a colored dice value
                            int[] colorDiceValues = human.getColorComb(points);
                            int colorFirstCross = Integer.parseInt(lastCross.substring(0, 1)); // White dice can be used at any color
                            int valueFirstCross = Integer.parseInt(lastCross.substring(1));
                            int colorSecondCross = Integer.parseInt(lastCross2.substring(0, 1));
                            int valueSecondCross = Integer.parseInt(lastCross2.substring(1));

                            if ((colorDiceValues[colorFirstCross] == valueFirstCross || colorDiceValues[colorFirstCross + 4] == valueFirstCross)
                                    && (colorDiceValues[colorSecondCross] == valueSecondCross || colorDiceValues[colorSecondCross + 4] == valueSecondCross)) {
                                scoreSheetHumanPlayer.displayErrorMessageOnlyColored(lastCrossed.size());
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }


        public void restartTheGame (HumanPlayer human){
            this.human = new HumanPlayer(human.getName());
            //this.ai = new AIPlayer();
            //this.scoreSheetHumanPlayer = new ScoreSheetHumanPlayerGUI(human);
            //this.diceGUI = new DiceGUI();
            end = false;
            this.human.changeState();
        }

        public void createGUI () {
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
            frame.setVisible(true);
            frame.setResizable(false);
        }

        @Override
        public void actionPerformed (ActionEvent e){
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
            } else if (e.getSource() == this.finish) {
                // At least one number has been crossed and thus a round can be closed
                if (scoreSheetHumanPlayer.getCrossesInRound() > 0) {
                    scoreSheetHumanPlayer.setCrossesLastRound(scoreSheetHumanPlayer.getCrossesInRound()); // Store the number of crosses in last round in case something went wrong when checking in Qwixx class
                    scoreSheetHumanPlayer.setCrossesInRound(0); // A new round is started and thus the counting of crosses is restarted
                    scoreSheetHumanPlayer.setRoundIsEnded();
                } else {
                    JOptionPane.showMessageDialog(this, "No number has been crossed. Cross a number or click skip round",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    // Display error message: No number has been crossed. Cross a number or click skip round
                }

                if (!end && humanCheck(diceGUI.getCurrentPoints())) {
                    if (this.human.isActive) {
                        // Should add a condition that AI only generates new dice values when human has finished round with valid dice values
                        this.ai.bestChoiceNonActive(diceGUI.getCurrentPoints());
                        diceGUI.nextRoundButton().doClick();
                    } else {
                        this.ai.bestChoiceActive(diceGUI.getCurrentPoints());
                        //humanCheck(diceGUI.getCurrentPoints());
                    }
                    // If the choice of the dice for the human player is correct, then we change the active player
                    this.human.changeState();
                    this.ai.changeState();
                    updateActivePlayer();
                    this.turn.setText(this.activePlayer);
                    playGame();
                    checkEnd();

                    if (end) {
                        this.scoreSheetHumanPlayer.updatePanelWhenFinished();
                        this.ai.gui.updatePanelWhenFinished(this.ai.getSheet());
                    }
                }
            } else if (e.getSource() == this.skip) {
                // Indeed no numbers has been crossed and thus a round can legally be skipped
                if (scoreSheetHumanPlayer.getCrossesInRound() == 0) {
                    human.skipRound(human.isActive);
                    scoreSheetHumanPlayer.setRoundIsEnded();

                    if (human.isActive) { // A player cannot cross a penalty itself, only hit skip round button.
                        int k = 0;
                        // As long as the penalty buttons are crossed, a new penalty cannot be crossed
                        while (scoreSheetHumanPlayer.penalties[k].getText().equals("X")) {
                            k++;
                        }
                        scoreSheetHumanPlayer.crossPenalty(k);
                    }

                    if (!end) {
                        if (this.human.isActive) {
                            // Should add a condition that AI only generates new dice values when human has finished round with valid dice values
                            this.ai.bestChoiceNonActive(diceGUI.getCurrentPoints());
                            diceGUI.nextRoundButton().doClick();
                        } else {
                            this.ai.bestChoiceActive(diceGUI.getCurrentPoints());
                            //humanCheck(diceGUI.getCurrentPoints());
                        }
                        // If the choice of the dice for the human player is correct, then we change the active player
                        this.human.changeState();
                        this.ai.changeState();
                        updateActivePlayer();
                        this.turn.setText(this.activePlayer);
                        playGame();
                        checkEnd();

                        if (end) {
                            this.scoreSheetHumanPlayer.updatePanelWhenFinished();
                            this.ai.gui.updatePanelWhenFinished(this.ai.getSheet());
                        }
                    } else {
                        this.scoreSheetHumanPlayer.updatePanelWhenFinished();
                        this.ai.gui.updatePanelWhenFinished(this.ai.getSheet());
                    }
                } else { // In case a number is crossed and also the skipRound button has been clicked:
                    JOptionPane.showMessageDialog(this, "Remove the crosses if you want to skip this round or click the Finish button",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    // Display an error message:  Remove the crosses if you want to skip this round or click the Finish button.
                }
            } else if (e.getSource() == this.gameRules) {
                JOptionPane.showMessageDialog(this, "Game rules",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }

        public static void main (String[]args){
            HumanPlayer human = new HumanPlayer("");
            AIPlayer ai = new AIPlayer();
            Qwixx qwixxGame = new Qwixx(human, ai);
            qwixxGame.createGUI();
            qwixxGame.playGame();
        }
    }
