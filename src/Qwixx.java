import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Random;


public class Qwixx extends Component implements ActionListener {
    private JFrame frame = new JFrame();
    private HumanPlayer human;
    private AIPlayer ai;
    private boolean end;
    private ScoreSheetHumanPlayerGUI scoreSheetHumanPlayer;
    private DiceGUI diceGUI;
    private final int NUMBER_OF_COLOR = 4;
    private String activePlayer;

    private JButton restartGame = new JButton();
    private JButton gameRules = new JButton();
    private JButton quitGame = new JButton();
    private JButton quitApp = new JButton();
    private JPanel infoPanel = new JPanel();
    private JButton finish = new JButton();
    private JButton skip = new JButton();
    private JButton turn = new JButton();
    private JButton toss;
    private int clicksOnRestartGame = 0;
    private boolean humanTossYet = false;

    private JButton endGame = new JButton();

    public Qwixx(HumanPlayer human) {
        this.human = human;
        this.ai = new AIPlayer();
        this.scoreSheetHumanPlayer = new ScoreSheetHumanPlayerGUI(human);
        this.diceGUI = new DiceGUI();
        this.activePlayer = "";
        end = false;
        finish = scoreSheetHumanPlayer.finished;
        finish.addActionListener(this);
        this.skip = scoreSheetHumanPlayer.skipRound;
        this.skip.addActionListener(this);
        toss = diceGUI.nextRoundButton();
        toss.addActionListener(this);
        this.human.changeState();
    }
///
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
                this.human.sheet.getLocks() >= 2 || this.ai.sheet.getLocks() >= 2) {
            // getLocks returns the number of colors that are removed from the game, when 2 colors are removed or
            // one player gets 4 penalties then the game is ended.
            this.end = true;
        }
    }

    public void updateActivePlayer() {
        if (this.human.isActive()) {
            this.activePlayer = this.human.getName();
        } else if (this.ai.isActive()) {
            this.activePlayer = this.ai.getName();
        }
    }

    public boolean humanCheck(int[] points) {
        List<String> lastCrossed = scoreSheetHumanPlayer.getLastCrossedNumbers();

        // Each element (indices) consists of 2 numbers (here in String format) where the first is the number of the color
        // and the second is the number crossed. 04 is for example a red 4
        for (String indices : lastCrossed) {
            // If the crossed number is not valid according to the dice value, display an error message
            if (human.numIsValid(Integer.parseInt(indices.substring(0, 1)), Integer.parseInt(indices.substring(1)), points, human.isActive()) == false) {
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

    public void restartTheGame(HumanPlayer human) {
        this.human = new HumanPlayer(human.getName());
        this.ai = new AIPlayer();
        this.scoreSheetHumanPlayer = new ScoreSheetHumanPlayerGUI(this.human);
        this.diceGUI = new DiceGUI();
        end = false;
        this.human.changeState();

        humanTossYet = false;
        quitGame = new JButton();
        quitApp = new JButton();
        endGame = new JButton();
        this.scoreSheetHumanPlayer.setCrossesInRound(0);

        this.finish = this.scoreSheetHumanPlayer.finished;
        this.finish.addActionListener(this);
        this.skip = this.scoreSheetHumanPlayer.skipRound;
        this.skip.addActionListener(this);
        toss = this.diceGUI.nextRoundButton();
        toss.addActionListener(this);
        this.playGame();
    }

    public void createStartScreen() {
        JFrame startFrame = new JFrame();
        startFrame.pack();
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(new Dimension(600, 800));
        startFrame.getContentPane().setBackground(new Color(204, 204, 204));
        startFrame.setLayout(new BorderLayout());
        startFrame.setVisible(true);

        restartGame.setText("Click to start a new game");
        gameRules.setText("Click here to view the rules of Qwixx");
        restartGame.addActionListener(this);
        gameRules.addActionListener(this);
        quitApp.setText("Quit application");
        quitApp.addActionListener(this);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1));
        infoPanel.add(restartGame);
        infoPanel.add(gameRules);
        infoPanel.add(quitApp);
        //infoPanel.add(turn);
        infoPanel.setSize(new Dimension(100, 100));

        JPanel middlePanel = new JPanel();
        BoxLayout layout = new BoxLayout(middlePanel, BoxLayout.X_AXIS);
        middlePanel.setLayout(layout);
        middlePanel.add(infoPanel);

        startFrame.add(middlePanel, BorderLayout.CENTER);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        startFrame.setVisible(true);
        startFrame.setResizable(false);
    }

    public void createGUI() {
        frame = new JFrame();
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

        quitGame.setText("You want to quit this game? Click me!");
        gameRules.setText("Click here to view the rules of Qwixx");
        this.turn = new JButton(this.activePlayer + " will play first");
        endGame.setText("Game has not ended yet");
        quitGame.addActionListener(this);
        gameRules.addActionListener(this);
        endGame.addActionListener(this);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(4, 1));
        infoPanel.add(quitGame);
        infoPanel.add(gameRules);
        infoPanel.add(turn);
        //infoPanel.add(endGame);
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
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == restartGame) {
            String humanName = JOptionPane.showInputDialog("What's your name? Our AI would love to make new friend!");
            if (humanName == null) {
                return;
            }
            this.human.changeName(humanName);

            String[] option = new String[] {"Yes!", "Nope, let AI play first", "Choose randomly"};
            int n = JOptionPane.showOptionDialog(this, "Do you want to play first?",
                    "Choosing Active Player", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[0]);

            boolean humanFirst = false;
            if (n == 0) {
                this.activePlayer = this.human.getName();
                humanFirst = true;
            } else if (n == 1) {
                this.activePlayer = this.ai.getName();
            } else if (n == 2) {
                int player = (int)Math.round(Math.random());
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
            //System.out.println("1,"+ this.activePlayer);
            restartTheGame(this.human);
            //System.out.println("2,"+ this.activePlayer);
            this.createGUI();
            //System.out.println("3,"+ this.activePlayer);
            //this.turn.setText("This turn belongs to " + this.activePlayer);

            if(humanFirst) {
                this.human.changeState();
            } else {
                this.ai.changeState();
            }

            /**
            if (this.activePlayer.equals(this.ai.name)) {
                this.ai.isActivePlayer();
                this.human.isNotActivePlayer();
                diceGUI.enableToss();
                diceGUI.nextRoundButton().doClick();
                diceGUI.disableToss();
            }*/

            if (!humanFirst) {
                this.ai.changeState();
                diceGUI.enableToss();
                diceGUI.nextRoundButton().doClick();
                diceGUI.disableToss();
            } else {
                this.human.changeState();
            }

            return;
        }

        if (e.getSource() == quitGame) {
            String[] option = new String[] {"Actually, no!", "Yes :("};
            int n = JOptionPane.showOptionDialog(this, "Are you 100% sure?",
                    "Quit Game", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
            if (n == JOptionPane.YES_NO_OPTION) {
                return;
            }
            else {
                //frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                //frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                frame.dispose();
            }
        }

        if (e.getSource() == quitApp) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }

        if (e.getSource() == this.finish) {
            // At least one number has been crossed and thus a round can be closed
            if (scoreSheetHumanPlayer.getCrossesInRound() > 0) {
                scoreSheetHumanPlayer.setCrossesLastRound(scoreSheetHumanPlayer.getCrossesInRound()); // Store the number of crosses in last round in case something went wrong when checking in Qwixx class
                scoreSheetHumanPlayer.setCrossesInRound(0); // A new round is started and thus the counting of crosses is restarted
                scoreSheetHumanPlayer.setRoundIsEnded();
            } else {
                JOptionPane.showMessageDialog(this, "No number has been crossed. Cross a number or click skip round",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
                // Display error message: No number has been crossed. Cross a number or click skip round
                return;
            }

            if (!end && humanCheck(diceGUI.getCurrentPoints())) {
                if (this.human.isActive()) {
                    // Should add a condition that AI only generates new dice values when human has finished round with valid dice values
                    this.ai.bestChoiceNonActive(diceGUI.getCurrentPoints());
                    diceGUI.enableToss();
                    diceGUI.nextRoundButton().doClick();
                    diceGUI.disableToss();
                } else {
                    diceGUI.enableToss();
                    this.ai.bestChoiceActive(diceGUI.getCurrentPoints());
                    //humanCheck(diceGUI.getCurrentPoints());
                }

                // If the choice of the dice for the human player is correct, then we change the active player
                this.human.changeState();
                this.ai.changeState();
                updateActivePlayer();
                this.turn.setText("This turn belongs to " + this.activePlayer);
                playGame();
                checkEnd();

                if (end) {
                    this.scoreSheetHumanPlayer.updatePanelWhenFinished();
                    this.ai.gui.updatePanelWhenFinished(this.ai.getSheet());
                    endGame.doClick();
                }
            }
        }

        else if (e.getSource() == this.skip) {
            // Indeed no numbers has been crossed and thus a round can legally be skipped
            if (scoreSheetHumanPlayer.getCrossesInRound() == 0) {
                human.skipRound(human.isActive());
                scoreSheetHumanPlayer.setRoundIsEnded();

                if (human.isActive()) { // A player cannot cross a penalty itself, only hit skip round button.
                    int k = 0;
                    // As long as the penalty buttons are crossed, a new penalty cannot be crossed
                    while (scoreSheetHumanPlayer.penalties[k].getText().equals("X")) {
                        k++;
                        System.out.println("4, " + this.human.sheet.getPenaltyValue());
                    }
                    scoreSheetHumanPlayer.crossPenalty(k);
                }

                if (!end) {
                    if (this.human.isActive()) {
                        // Should add a condition that AI only generates new dice values when human has finished round with valid dice values
                        this.ai.bestChoiceNonActive(diceGUI.getCurrentPoints());
                        diceGUI.enableToss();
                        diceGUI.nextRoundButton().doClick();
                        diceGUI.disableToss();
                    } else {
                        diceGUI.enableToss();
                        this.ai.bestChoiceActive(diceGUI.getCurrentPoints());
                        //humanCheck(diceGUI.getCurrentPoints());
                    }
                    // If the choice of the dice for the human player is correct, then we change the active player
                    this.human.changeState();
                    this.ai.changeState();
                    updateActivePlayer();
                    this.turn.setText("This turn belongs to " + this.activePlayer);
                    playGame();
                    checkEnd();

                    if (end) {
                        this.scoreSheetHumanPlayer.updatePanelWhenFinished();
                        this.ai.gui.updatePanelWhenFinished(this.ai.getSheet());
                        endGame.doClick();
                    }
                } else {
                    this.scoreSheetHumanPlayer.updatePanelWhenFinished();
                    this.ai.gui.updatePanelWhenFinished(this.ai.getSheet());
                    endGame.doClick();
                }
            } else { // In case a number is crossed and also the skipRound button has been clicked:
                JOptionPane.showMessageDialog(this, "Remove the crosses if you want to skip this round or click the Finish button",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }

        else if (e.getSource() == this.gameRules) {
            JOptionPane.showMessageDialog(this,
                    "1. When you roll the dice in a round:"+ "\n"+
                            "  -First, sum up the points of two white dice, and you can cross the corresponding number in one of the four rows"+ "\n"+
                            "  -Second, sum up one of the white die and one of the colored die, and you can cross the corresponding number in the row with the same color."+ "\n"+
                            "  -Note that, the order cannot be changed. However, you can choose to cross either one or two times,"+ "\n"+
                            "   but if you do not cross any, you will get a penalty." + "\n"+ "\n"+
                            "2. When it is not your round to roll the dice:"+ "\n"+
                            "   You can only choose the sum of the two white dice, and cross that number in one of the four rows. If you do not cross any, you will not get a penalty."+ "\n"
                            + "\n"+"3. The numbers must be crossed out from left to right in each of the four colored rows."
                            + "\n" + "\n"+"4. The rightest number in each row can only be crossed when it is at least the 6th cross in that row."+ "\n"+
                            "   If you cross the rightest number, you can then lock the row! (Crossing a lock also gives you an additional cross in that row)"+ "\n" + "\n"+
                            "5. When a row is locked, the corresponding colored die is removed from the game, and the players cannot cross that colored row anymore." + "\n"+ "\n"+
                            "6. The game ends when two rows are locked, or one player has get 4 penalties."+ "\n"+ "\n" +
                            "7. You can find the scoring table below the color rows on the score sheet. Each penalty costs you 5 points."+"\n"+
                            "   The player with the most points wins the game! ",
                    "Warning", JOptionPane.WARNING_MESSAGE);

        }

        else if (e.getSource() == this.toss) {
            diceGUI.disableToss(); // Ensure that the human only tosses once per round
        }

        else if (e.getSource() == endGame) {
            if (this.ai.sheet.getTotalScore() == this.human.sheet.getTotalScore()) {
                JOptionPane.showMessageDialog(this, "It's a tie!",
                        "Game Ends!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String message;

            if(this.ai.sheet.getTotalScore() < this.human.sheet.getTotalScore()) {
                message = "Congratulation, you beat the AI! Want to play another game?";
                endGame.setText("You win!");
            } else {
                message = "Oh no, our AI won this game. Want to try again?";
                endGame.setText("AI win...");
            }

            String[] option = new String[] {"Sure!", "No, thanks!"};
            int m = JOptionPane.showOptionDialog(this, message,
                    "Game Ends", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
            if (m == 0) {
                frame.dispose();
                restartGame.doClick();
            } else {
                //frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                //frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                frame.dispose();
            }
        }
    }

    public static void main(String[] args) {
        HumanPlayer human = new HumanPlayer("");
        //AIPlayer ai = new AIPlayer();
        Qwixx qwixxGame = new Qwixx(human);
        qwixxGame.createStartScreen();
        //qwixxGame.playGame();
    }
}