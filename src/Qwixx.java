import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Class that creates a Qwixx game using an ActionListener
 *
 * @author Amber Cuijpers, Danielle Lam, Khue Nguyen, Yu-Shan Cho, Yuntong Wu
 */
public class Qwixx extends Component implements ActionListener {
    private HumanPlayer human;
    private AIPlayer ai;
    private ScoreSheetHumanPlayerGUI scoreSheetHumanPlayer;
    private DiceGUI diceGUI;
    private String activePlayer;
    private boolean end;
    private final int NUMBER_OF_COLOR = 4;

    private JFrame frame = new JFrame();
    private final JButton restartGame = new JButton();
    private final JButton gameRules = new JButton();
    private JButton quitGame = new JButton();
    private JButton quitApp = new JButton();
    private JButton turn = new JButton();
    private JButton endGame = new JButton();
    private JButton finish;
    private JButton skip;
    private JButton toss;


    /**
     * Constructor for the Qwixx class that initializes the players, their scoresheets and buttons.
     * @param human The human player playing this Qwixx game.
     */
    public Qwixx(HumanPlayer human) {
        this.human = human;
        this.ai = new AIPlayer();
        this.scoreSheetHumanPlayer = new ScoreSheetHumanPlayerGUI(human);
        this.diceGUI = new DiceGUI();
        this.activePlayer = "";
        this.end = false;
        this.finish = this.scoreSheetHumanPlayer.finished;
        this.finish.addActionListener(this);
        this.skip = this.scoreSheetHumanPlayer.skipRound;
        this.skip.addActionListener(this);
        this.toss = this.diceGUI.nextRoundButton();
        this.toss.addActionListener(this);
        this.human.changeState();
    }

    /**
     * This method checks if a die needs to be removed from the Qwixx game.
     */
    public void checkRemoveDice() {
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

    /**
     * When a player has four penalties or two colors are locked, the game ends.
     */
    public void checkEnd() {
        if (this.human.sheet.getPenaltyValue() == 4 || this.ai.sheet.getPenaltyValue() == 4 ||
                this.human.sheet.getLocks() >= 2 || this.ai.sheet.getLocks() >= 2) {
            this.end = true;
        }
    }

    /**
     * This method updates the active players such that changed on the button this.turn whose turn it is.
     */
    public void updateActivePlayer() {
        if (this.human.isActive()) {
            this.activePlayer = this.human.getName();
        } else if (this.ai.isActive()) {
            this.activePlayer = this.ai.getName();
        }
    }

    /**
     * The crosses that were put by the human player are checked with the corresponding dice values of this round
     * @param points Dice values of this round
     * @return True is returned when the crosses put by the human player correspond to the dice values.
     */
    public boolean humanCheck(int[] points) {
        /* Each element (indices) consists of 2 numbers (here in String format) where the first is the number of the
         * color and the second is the number crossed. 04 is for example a red 4.
         * First we add back a color, when it is locked in this round
         */
        List<String> lastCrossed = this.scoreSheetHumanPlayer.getLastCrossedNumbers();

        // In case we crossed a 12 or 2, the color is locked but we still want to check the crosses with
        // the dice values and therefore it is added back
        boolean crossedRowR = false;
        boolean crossedRowY = false;
        boolean crossedRowG = false;
        boolean crossedRowB = false;

        for (String indices : lastCrossed) {
            int row = Integer.parseInt(indices.substring(0, 1));
            int column = Integer.parseInt(indices.substring(1));

            // Color is red or yellow
            if (row == 0 || row == 1) {
                // If we have crossed an twelve and the color is locked, then we want to add back this color temporarily.
                if (column == 12 && !this.human.sheet.getValidRow(row)) {
                    this.human.sheet.addColor(row);

                    if (row == 0) {
                        crossedRowR = true;
                    } else {
                        crossedRowY = true;
                    }
                }
            } else if (row == 2 | row == 3) {
                if (column == 2 && !this.human.sheet.getValidRow(row)) {
                    this.human.sheet.addColor(row);

                    if (row == 2) {
                        crossedRowG = true;
                    } else {
                        crossedRowB = true;
                    }
                }
            }
        }

        // If the crossed number is not valid according to the dice value, display an error message
        for (String indices : lastCrossed) {
            int row = Integer.parseInt(indices.substring(0, 1));
            int column = Integer.parseInt(indices.substring(1));

            if (!this.human.numIsValid(row, column, points, this.human.isActive())) {
                this.scoreSheetHumanPlayer.displayErrorMessageRemote(lastCrossed.size());
                return false;
            }
        }

        /* lastCrossed contains at maximum 2 elements, if more an error message would already be displayed in the GUI.
        * If human is not the active player but wants to cross 2 numbers, then also an error message would already be
        * displayed in the GUI.
         */
        if (lastCrossed.size() == 2) {
            for (String lastCross : lastCrossed) {
                for (String lastCross2 : lastCrossed) {
                    if (!lastCross.equals(lastCross2)) { // Do not compare the same crosses
                        // First we check if the crossed number corresponds to the white dice value and a colored dice value
                        int whiteDiceValue = this.human.getWhiteComb(points);
                        int[] colorDiceValues = this.human.getColorComb(points);
                        int colorFirstCross = Integer.parseInt(lastCross.substring(0, 1)); // White dice can be used at any color
                        int valueFirstCross = Integer.parseInt(lastCross.substring(1));
                        int colorSecondCross = Integer.parseInt(lastCross2.substring(0, 1));
                        int valueSecondCross = Integer.parseInt(lastCross2.substring(1));

                        if (whiteDiceValue == valueFirstCross &&
                                (colorDiceValues[colorSecondCross] == valueSecondCross ||
                                        colorDiceValues[colorSecondCross + 4] == valueSecondCross)) {
                            if (colorFirstCross == 0 || colorFirstCross == 1) {
                                if (colorFirstCross == colorSecondCross && whiteDiceValue > valueSecondCross) {
                                    this.scoreSheetHumanPlayer.displayErrorMessageOrder(lastCrossed.size());
                                    return false;
                                }
                            } else if (colorFirstCross == 2 || colorFirstCross == 3) {
                                if (colorFirstCross == colorSecondCross && whiteDiceValue < valueSecondCross) {
                                    this.scoreSheetHumanPlayer.displayErrorMessageOrder(lastCrossed.size());
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
                        int[] colorDiceValues = this.human.getColorComb(points);
                        int colorFirstCross = Integer.parseInt(lastCross.substring(0, 1));
                        int valueFirstCross = Integer.parseInt(lastCross.substring(1));
                        int colorSecondCross = Integer.parseInt(lastCross2.substring(0, 1));
                        int valueSecondCross = Integer.parseInt(lastCross2.substring(1));

                        if ((colorDiceValues[colorFirstCross] == valueFirstCross ||
                                colorDiceValues[colorFirstCross + 4] == valueFirstCross)
                                && (colorDiceValues[colorSecondCross] == valueSecondCross
                                || colorDiceValues[colorSecondCross + 4] == valueSecondCross)) {
                            this.scoreSheetHumanPlayer.displayErrorMessageOnlyColored(lastCrossed.size());
                            return false;
                        }
                    }
                }
            }
        }

        // Remove the row again that was locked in
        if (crossedRowR || crossedRowY || crossedRowG || crossedRowB) {
            if (crossedRowR) {
                this.human.sheet.removeColor(0);
            } else if (crossedRowY) {
                this.human.sheet.removeColor(1);
            } else if (crossedRowG) {
                this.human.sheet.removeColor(2);
            } else {
                this.human.sheet.removeColor(3);
            }
        }
        return true;
    }

    /**
     * The game is restarted and all values are reset.
     * @param human The human player that plays this new Qwixx game.
     */
    public void restartTheGame(HumanPlayer human) {
        this.human = new HumanPlayer(human.getName());
        this.ai = new AIPlayer();
        this.scoreSheetHumanPlayer = new ScoreSheetHumanPlayerGUI(this.human);
        this.diceGUI = new DiceGUI();
        this.end = false;

        this.quitGame = new JButton();
        this.quitApp = new JButton();
        this.endGame = new JButton();
        this.scoreSheetHumanPlayer.setCrossesInRound(0);

        this.finish = this.scoreSheetHumanPlayer.finished;
        this.finish.addActionListener(this);
        this.skip = this.scoreSheetHumanPlayer.skipRound;
        this.skip.addActionListener(this);
        this.toss = this.diceGUI.nextRoundButton();
        this.toss.addActionListener(this);
        this.checkRemoveDice();
    }

    /**
     * The start screen GUI is created, where the human player can choose to start the game, view the rules or exit
     * the application.
     */
    public void createStartScreen() {
        JFrame startFrame = new JFrame();
        startFrame.pack();
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(new Dimension(600, 800));
        startFrame.getContentPane().setBackground(new Color(204, 204, 204));
        startFrame.setLayout(new BorderLayout());
        startFrame.setVisible(true);

        this.restartGame.setText("Click to start a new game");
        this.gameRules.setText("Click here to view the rules of Qwixx");
        this.quitApp.setText("Quit application");
        this.restartGame.addActionListener(this);
        this.gameRules.addActionListener(this);
        this.quitApp.addActionListener(this);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1));
        infoPanel.add(this.restartGame);
        infoPanel.add(this.gameRules);
        infoPanel.add(this.quitApp);
        infoPanel.setSize(new Dimension(100, 100));

        JPanel middlePanel = new JPanel();
        BoxLayout layout = new BoxLayout(middlePanel, BoxLayout.X_AXIS);
        middlePanel.setLayout(layout);
        middlePanel.add(infoPanel);

        startFrame.add(middlePanel, BorderLayout.CENTER);
        startFrame.setVisible(true);
        startFrame.setResizable(false);
    }

    /**
     * The GUI is created in which the Qwixx game can be played.
     */
    public void createGUI() {
        this.frame = new JFrame();
        this.frame.pack();
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(new Dimension(600, 800));
        this.frame.getContentPane().setBackground(new Color(204, 204, 204));
        this.frame.setLayout(new BorderLayout());
        this.frame.setVisible(true);

        JPanel humanPanel = this.scoreSheetHumanPlayer.getScoreSheetHumanPlayer();
        humanPanel.setMinimumSize(humanPanel.getPreferredSize());
        JPanel aiPanel = this.ai.getGUI().getScorePanel();
        aiPanel.setMinimumSize(humanPanel.getPreferredSize());

        JPanel dicePanel = this.diceGUI.getDicePanel();
        dicePanel.setSize(new Dimension(500, 100));

        this.quitGame.setText("You want to quit this game? Click me!");
        this.quitGame.setBackground(Color.white);
        this.gameRules.setText("Click here to view the rules of Qwixx");
        this.gameRules.setBackground(Color.white);
        this.turn = new JButton(this.activePlayer + " will play first");
        this.turn.setBackground(Color.white);
        this.turn.setBorder(BorderFactory.createLineBorder(Color.red));
        this.endGame.setText("Game has not ended yet");
        this.quitGame.addActionListener(this);
        this.gameRules.addActionListener(this);
        this.endGame.addActionListener(this);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1));
        infoPanel.add(this.quitGame);
        infoPanel.add(this.gameRules);
        infoPanel.add(this.turn);
        infoPanel.setSize(new Dimension(100, 100));

        JPanel middlePanel = new JPanel();
        BoxLayout layout = new BoxLayout(middlePanel, BoxLayout.X_AXIS);
        middlePanel.setLayout(layout);
        middlePanel.add(infoPanel);
        middlePanel.add(dicePanel);

        this.frame.add(aiPanel, BorderLayout.PAGE_END);
        this.frame.add(humanPanel, BorderLayout.PAGE_START);
        this.frame.add(middlePanel, BorderLayout.CENTER);
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.frame.setVisible(true);
        this.frame.setResizable(false);
    }

    /**
     * Method which acts on the clicks done by the human player and performs the associated action.
     * @param e allows you to access the properties of ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.restartGame) {
            String humanName = JOptionPane.showInputDialog("What's your name? Our AI would love to make new friend!");

            if (humanName == null) {
                return;
            }

            this.human.changeName(humanName);

            String[] option = new String[]{"Yes!", "Nope, let AI play first", "Choose randomly"};
            int n = JOptionPane.showOptionDialog(this, "Do you want to play first?",
                    "Choosing Active Player", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, option, option[0]);

            boolean humanFirst = false;

            if (n == 0) {
                this.activePlayer = this.human.getName();
                humanFirst = true;
            } else if (n == 1) {
                this.activePlayer = this.ai.getName();
            } else if (n == 2) {
                int player = (int) Math.round(Math.random());

                if (player == 0) {
                    JOptionPane.showMessageDialog(this, "You will play first!",
                            "Choosing Active Player", JOptionPane.INFORMATION_MESSAGE);
                    this.activePlayer = this.human.getName();
                    humanFirst = true;
                } else {
                    JOptionPane.showMessageDialog(this, "Our AI will play first!",
                            "Choosing Active Player", JOptionPane.INFORMATION_MESSAGE);
                    this.activePlayer = this.ai.getName();
                }
            }

            restartTheGame(this.human);
            this.createGUI();

            if (!humanFirst) {
                this.ai.changeState();
                this.diceGUI.enableToss();
                this.diceGUI.nextRoundButton().doClick();
                this.diceGUI.disableToss();
            } else {
                this.human.changeState();
            }

            return;
        }

        if (e.getSource() == this.quitGame) {
            String[] option = new String[]{"Actually, no!", "Yes :("};
            int n = JOptionPane.showOptionDialog(this, "Are you 100% sure?",
                    "Quit Game", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[0]);

            if (n == JOptionPane.YES_NO_OPTION) {
                return;
            } else {
                this.frame.dispose();
                createStartScreen();
            }
        }

        if (e.getSource() == this.quitApp) {
            System.exit(0);
        }

        if ((e.getSource() == this.finish || e.getSource() == this.skip) && this.toss.getModel().isEnabled()) {
            JOptionPane.showMessageDialog(this, "Toss the dice. It is your turn!",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } else if (e.getSource() == this.finish) {
            // At least one number has been crossed and thus a round can be closed
            if (this.scoreSheetHumanPlayer.getCrossesInRound() > 0) {
                this.scoreSheetHumanPlayer.setCrossesLastRound(this.scoreSheetHumanPlayer.getCrossesInRound());
                this.scoreSheetHumanPlayer.setCrossesInRound(0);
                this.scoreSheetHumanPlayer.setRoundIsEnded();
            } else {
                JOptionPane.showMessageDialog(this, "No number has been crossed. " +
                                "Cross a number or click skip round",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!this.end && humanCheck(this.diceGUI.getCurrentPoints())) {
                if (this.human.isActive()) {
                    this.ai.bestChoiceNonActive(this.diceGUI.getCurrentPoints());
                    this.checkRemoveDice();
                    this.checkEnd();

                    // We check if the end of the game has been reached after the crosses set by the active human
                    // and inactive AI player. In case the end has not been reached the AI can toss the dice.
                    if (!this.end) {
                        this.diceGUI.enableToss();
                        this.diceGUI.nextRoundButton().doClick();
                        this.diceGUI.disableToss();

                        this.human.changeState();
                        this.ai.changeState();
                        updateActivePlayer();
                        this.turn.setText("This turn belongs to " + this.activePlayer);
                    }
                } else {
                    this.ai.bestChoiceActive(this.diceGUI.getCurrentPoints());
                    this.checkRemoveDice();
                    this.checkEnd();

                    if (!this.end) {
                        this.diceGUI.enableToss();
                        this.human.changeState();
                        this.ai.changeState();
                        updateActivePlayer();
                        this.turn.setText("This turn belongs to " + this.activePlayer);
                    }
                }

                if (this.end) {
                    this.scoreSheetHumanPlayer.updatePanelWhenFinished();
                    this.ai.getGUI().updatePanelWhenFinished(this.ai.getSheet());
                    this.endGame.doClick();
                }
            }
        } else if (e.getSource() == this.skip) {
            // Indeed no numbers has been crossed and thus a round can legally be skipped
            if (this.scoreSheetHumanPlayer.getCrossesInRound() == 0) {
                this.human.skipRound(this.human.isActive());
                this.scoreSheetHumanPlayer.setRoundIsEnded();

                if (this.human.isActive()) { // A player cannot cross a penalty itself, only hit skip round button.
                    int k = 0;
                    // As long as the penalty buttons are crossed, a new penalty cannot be crossed
                    while (this.scoreSheetHumanPlayer.penalties[k].getText().equals("X")) {
                        k++;
                    }

                    this.scoreSheetHumanPlayer.crossPenalty(k);
                }

                if (!this.end) {
                    if (this.human.isActive()) {
                        this.ai.bestChoiceNonActive(diceGUI.getCurrentPoints());
                        this.checkRemoveDice();
                        this.checkEnd();

                        // After the human has skipped and the AI has chosen, then it is checked if the game is ended.
                        // If not the AI can toss the dice, since it is its turn.
                        if (!this.end) {
                            this.diceGUI.enableToss();
                            this.diceGUI.nextRoundButton().doClick();
                            this.diceGUI.disableToss();

                            this.human.changeState();
                            this.ai.changeState();
                            updateActivePlayer();
                            this.turn.setText("This turn belongs to " + this.activePlayer);
                        }
                    } else {
                        this.ai.bestChoiceActive(this.diceGUI.getCurrentPoints());
                        this.checkRemoveDice();
                        this.checkEnd();

                        if (!this.end) {
                            this.diceGUI.enableToss();
                            this.human.changeState();
                            this.ai.changeState();
                            updateActivePlayer();
                            this.turn.setText("This turn belongs to " + this.activePlayer);
                        }
                    }

                    if (this.end) {
                        this.scoreSheetHumanPlayer.updatePanelWhenFinished();
                        this.ai.getGUI().updatePanelWhenFinished(this.ai.getSheet());
                        this.endGame.doClick();
                    }
                } else {
                    this.scoreSheetHumanPlayer.updatePanelWhenFinished();
                    this.ai.getGUI().updatePanelWhenFinished(this.ai.getSheet());
                    this.endGame.doClick();
                }
            } else { // In case a number is crossed and also the skipRound button has been clicked:
                JOptionPane.showMessageDialog(this, "Remove the crosses if you want to " +
                                "skip this round or click the Finish button",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == this.gameRules) {
            JOptionPane.showMessageDialog(this,
                    "1. When you roll the dice in a round:" + "\n" +
                            "  -First, sum up the points of two white dice, and you can cross the corresponding number in one of the four rows" + "\n" +
                            "  -Second, sum up one of the white die and one of the colored die, and you can cross the corresponding number in the row with the same color." + "\n" +
                            "  -Note that, the order cannot be changed. However, you can choose to cross either one or two times," + "\n" +
                            "   but if you do not cross any, you will get a penalty." + "\n" + "\n" +
                            "2. When it is not your round to roll the dice:" + "\n" +
                            "   You can only choose the sum of the two white dice, and cross that number in one of the four rows. If you do not cross any, you will not get a penalty." + "\n"
                            + "\n" + "3. The numbers must be crossed out from left to right in each of the four colored rows."
                            + "\n" + "\n" + "4. The rightest number in each row can only be crossed when it is at least the 6th cross in that row." + "\n" +
                            "   If you cross the rightest number, you can then lock the row! (Crossing a lock also gives you an additional cross in that row)" + "\n" + "\n" +
                            "5. When a row is locked, the corresponding colored die is removed from the game, and the players cannot cross that colored row anymore." + "\n" + "\n" +
                            "6. The game ends when two rows are locked, or one player has get 4 penalties." + "\n" + "\n" +
                            "7. You can find the scoring table below the color rows on the score sheet. Each penalty costs you 5 points." + "\n" +
                            "   The player with the most points wins the game! ",
                    "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (e.getSource() == this.toss) {
            this.diceGUI.disableToss(); // Ensure that the human only tosses once per round
        } else if (e.getSource() == this.endGame) {
            if (this.ai.sheet.getTotalScore() == this.human.sheet.getTotalScore()) {
                JOptionPane.showMessageDialog(this, "It's a tie!",
                        "Game Ends!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String message;

            if (this.ai.sheet.getTotalScore() < this.human.sheet.getTotalScore()) {
                message = "Congratulation, you beat the AI! Want to play another game?";
                this.endGame.setText("You win!");
            } else {
                message = "Oh no, our AI won this game. Want to try again?";
                this.endGame.setText("AI win...");
            }

            String[] option = new String[]{"Sure!", "No, thanks!"};
            int m = JOptionPane.showOptionDialog(this, message,
                    "Game Ends", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
            this.frame.dispose();

            if (m == 0) {
                this.restartGame.doClick();
            } else {
                System.exit(0);
            }
        }
    }

    /**
     * This method allows to run the code and play the game in the GUI
     * @param args argument used by main method
     */
    public static void main(String[] args) {
        HumanPlayer human = new HumanPlayer("");
        Qwixx qwixxGame = new Qwixx(human);
        qwixxGame.createStartScreen();
    }
}
