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
    private int clicksOnRestartGame = 0;

    public Qwixx(HumanPlayer human, AIPlayer ai) {
        this.human = human;
        this.ai = ai;
        this.scoreSheetHumanPlayer = new ScoreSheetHumanPlayerGUI(human);
        this.diceGUI = new DiceGUI();
        this.activePlayer = "Game is not started yet.";
        end = false;
    }

    public JButton getRestartGameButton() {
        return restartGame;
    }

    public void playGame() {
        if (getRestartGameButton().getModel().isPressed()) {
            humanFirst();

            while (!end) {
                if (this.human.getState()) {
                    if (diceGUI.nextRoundButton().getModel().isPressed()) {
                        // If it is the human players turn
                        humanCheck(diceGUI.getCurrentPoints());
                        this.ai.bestChoiceNonActive(diceGUI.getCurrentPoints());
                    }
                } else {
                    diceGUI.tossDiceAI();
                    this.ai.bestChoiceActive(diceGUI.getCurrentPoints());
                    humanCheck(diceGUI.getCurrentPoints());
                }

                for (int i = 0; i < NUMBER_OF_COLOR; i++) {
                    if (!this.human.sheet.getValidRow(i) || !this.ai.sheet.getValidRow(i)) {
                        // If one player closes a row, then the color should also disappear for the other player after
                        // having chosen their dice combination (including the color that may now disappear)
                        this.human.sheet.removeColor(i);
                        this.ai.sheet.removeColor(i);

                        // Also remove the corresponding die from the game
                        for (Dice d : this.diceGUI.getDiceSet()) {
                            if (d.getColor() == i + 2) {
                                diceGUI.removeDice(d);
                            }
                        }
                    }
                }

                checkEnd();
                this.human.changeState();
                this.ai.changeState();
                updateActivePlayer();
            }


            this.scoreSheetHumanPlayer.updatePanelWhenFinished();
            this.ai.gui.updatePanelWhenFinished(this.ai.getSheet());
        }
    }


    public void humanFirst() {
        Dice d = this.diceGUI.getDiceSet()[0];
        d.rollDice();
        if (d.getValue() <= 3) {
            this.human.changeState();
            updateActivePlayer();
        } else {
            this.ai.changeState();
            updateActivePlayer();
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
            this.activePlayer = this.human.getName();
        } else if (this.ai.isActive) {
            this.activePlayer = this.ai.getName();
        } else {
            this.activePlayer = "Game is not started yet.";
        }
    }

    public void humanCheck(int[] points) {
        if (scoreSheetHumanPlayer.getRoundIsEnded()) {
            List<String> lastCrossed = scoreSheetHumanPlayer.getLastCrossedNumbers();

            // Each element (indices) consists of 2 numbers (here in String format) where the first is the number of the color
            // and the second is the number crossed. 04 is for example a red 4
            for (String indices : lastCrossed) {
                // If the crossed number is not valid according to the dice value, display an error message
                if (!human.numIsValid(Integer.parseInt(indices.substring(0, 1)), Integer.parseInt(indices.substring(1, 2)), points, human.isActive)) {
                    scoreSheetHumanPlayer.displayErrorMessageRemote(lastCrossed.size());
                }
            }

            // lastCrossed contains at maximum 2 elements, if more an error message would already be displayed in the GUI
            // If human is not the active player but wants to cross 2 numbers, then also an error message would already be displayed in the GUI
            if (lastCrossed.size() == 2) {
                for (String indices : lastCrossed) {
                    for (String indices2 : lastCrossed) {
                        for (int colorCombination : human.getColorComb(points)) {
                            int whiteValue = Integer.parseInt(indices.substring(1, 2)); // The value of the white combination
                            int colorValue = Integer.parseInt(indices2.substring(1, 2)); // The value of the colored combination
                            int whiteColorNumber = Integer.parseInt(indices.substring(0, 1)); // The row in which the white combination is crossed
                            int colorNumber = Integer.parseInt(indices2.substring(0, 1)); // The row in which the color combination is crossed

                            // If 2 dices are chosen, then it has to be a white combination and a colored combination, but not only colored com
                            if (!indices.equals(indices2) && human.getWhiteComb(points) == whiteValue &&
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

    public void restartTheGame(HumanPlayer human) {
        this.human = new HumanPlayer(human.getName());
        this.ai = new AIPlayer();
        this.scoreSheetHumanPlayer = new ScoreSheetHumanPlayerGUI(human);
        this.diceGUI = new DiceGUI();
        end = false;
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
        JTextArea turn = new JTextArea("Turn of: " + this.activePlayer);
        restartGame.addActionListener(this);
        gameRules.addActionListener(this);

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
        frame.setUndecorated(true);
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
            }
        } else {
            JOptionPane.showMessageDialog(this, "Game rules",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        HumanPlayer human = new HumanPlayer("");
        AIPlayer ai = new AIPlayer();
        Qwixx qwixxGame = new Qwixx(human, ai);
        qwixxGame.createGUI();
        qwixxGame.playGame();
    }
}