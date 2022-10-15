import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

/**
 * Class that contains method for the graphical user interface (GUI) of the score sheet for the human player
 *
 * @author Amber Cuijpers, Danielle Lam, Khue Nguyen, Yu-Shan Cho, Yuntong Wu
 */
public class ScoreSheetHumanPlayerGUI extends Component implements ActionListener {
    public static int maxCrossPerRoundActive = 2;
    public static int maxCrossPerRoundInactive = 1;

    JPanel title_panel = new JPanel();
    JPanel button_panel = new JPanel();
    JPanel penalties_panel = new JPanel();
    JPanel pointsPanel = new JPanel();
    JPanel scorePanel = new JPanel();
    JPanel mainPanel = new JPanel();
    JPanel finishedPanel = new JPanel();
    JPanel skipRoundPanel = new JPanel();

    JLabel title = new JLabel();
    JLabel crossPenalty = new JLabel();
    JTextField inputName = new JTextField();
    JButton[][] buttons = new JButton[4][12];
    JButton[] penalties = new JButton[4];
    JLabel[][] points = new JLabel[2][13];
    JLabel[] signs = new JLabel[6];
    JButton[] pointsScored = new JButton[6];
    JButton finished = new JButton("I am finished");
    JButton skipRound = new JButton("Skip round");

    HumanPlayer player; // Does already contain a score sheet
    // Active human player can choose 2 number, inactive can choose 1
    int numberCrossesInRound;
    int numberCrossesLastRound;
    List<String> allCrossedNumbersInOrder = new ArrayList<>();
    boolean roundIsEnded;

    /**
     * Constructor for the ScoreSheetHumanPlayerGUI, which initializes the values and panels.
     * @param player the HumanPlayer from the HumanPlayer class
     */
    ScoreSheetHumanPlayerGUI(HumanPlayer player) {
        this.player = player; // A Human Player does not have a name until it types its name and the name is set by the setName method.
        roundIsEnded = false;
        numberCrossesLastRound = 0;
        numberCrossesInRound = 0;
        this.player.sheet = new Scoresheet();

        createTitlePanel();
        createButtons();
        createPenalties();
        createPointsPanel();
        createScorePanel();

        crossPenalty.setBackground(new Color(204, 204, 204));
        crossPenalty.setForeground(Color.black);
        crossPenalty.setFont(new Font("MV Boli", Font.PLAIN, 10));
        crossPenalty.setHorizontalAlignment(JLabel.CENTER);
        crossPenalty.setText("Cross a penalty (-5):");
        crossPenalty.setOpaque(true);

        pointsPanel.setLayout(new GridLayout(2, 13));
        pointsPanel.setBackground(new Color(204, 204, 204));
        pointsPanel.setPreferredSize(new Dimension(500, 30));
        pointsPanel.setMinimumSize(pointsPanel.getPreferredSize());

        penalties_panel.setLayout(new GridLayout(1, 4));
        penalties_panel.setBackground(new Color(204, 204, 204));
        penalties_panel.setPreferredSize(new Dimension(300, 40));
        penalties_panel.setMinimumSize(penalties_panel.getPreferredSize());

        JPanel penalties = new JPanel();
        penalties.setLayout(new GridLayout(2, 1));
        penalties.add(crossPenalty);
        penalties.add(penalties_panel);

        JPanel points = new JPanel();
        points.setLayout(new GridLayout(1, 2));
        points.add(pointsPanel);
        points.add(penalties);

        scorePanel.setLayout(new GridLayout(1, 12));
        scorePanel.setBackground(new Color(204, 204, 204));
        scorePanel.setPreferredSize(new Dimension(600, 50));
        scorePanel.setMinimumSize(scorePanel.getPreferredSize());

        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new GridLayout(2, 1));
        lowerPanel.setPreferredSize(new Dimension(600, 50));
        lowerPanel.add(points);
        lowerPanel.add(scorePanel);

        button_panel.setLayout(new GridLayout(4, 12));
        button_panel.setBackground(new Color(204, 204, 204));

        title_panel.add(title, BorderLayout.WEST);
        title_panel.add(inputName);

        finished.setFocusable(false);
        finished.addActionListener(this);
        finishedPanel.add(finished);
        penalties.setFont(new Font("MV Boli", Font.PLAIN, 10));
        penalties.setFocusable(false);
        penalties.setBackground(new Color(204, 204, 204));

        skipRound.setFocusable(false);
        skipRound.addActionListener(this);
        skipRoundPanel.add(skipRound);
        skipRound.setFont(new Font("MV Boli", Font.PLAIN, 10));
        skipRound.setFocusable(false);
        skipRound.setBackground(new Color(204, 204, 204));

        JPanel messages = new JPanel();
        messages.setLayout(new GridLayout(1, 2));
        messages.add(finishedPanel);
        messages.add(skipRoundPanel);
        messages.setPreferredSize(new Dimension(600, 15));
        messages.setMinimumSize(messages.getPreferredSize());

        JPanel bottom = new JPanel(new GridLayout(2,1));
        bottom.add(lowerPanel);
        bottom.add(messages);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(title_panel, BorderLayout.PAGE_START);
        mainPanel.add(bottom, BorderLayout.PAGE_END);
        mainPanel.add(button_panel);
    }

    /**
     * {@inheritDoc}
     * Method which crosses or uncrosses buttons when it is clicked and checks whether it is allowed to click that
     * button
     * @param e allows you to access the properties of ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        roundIsEnded = false;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 11; j++) { // A lock cannot be crossed by the human player itself
                if (e.getSource() == buttons[i][j]) {
                    int value;

                    if (i == 0 || i == 1) {
                        value = 2 + j;
                    } else {
                        value = 12 - j;
                    }
                    String indexStored = "" + i + value;

                    // Player wants to uncross the button
                    if (buttons[i][j].getText().equals("X")) {
                        List<String> crossedThisRound = allCrossedNumbersInOrder.subList(allCrossedNumbersInOrder.size()
                                - numberCrossesInRound, allCrossedNumbersInOrder.size());
                        boolean canUncross = false;

                        // Can only uncross button (i,j) that has been crossed in this round
                        for (String cross : crossedThisRound) {
                            if (Integer.parseInt(cross.substring(0, 1)) == i && Integer.parseInt(cross.substring(1)) == value) {
                                canUncross = true;
                            }
                        }

                        if (canUncross && player.sheet.getValidRow(i)) {
                            uncrossButton(i, j);
                            // If player wants to uncross 12 or 2 and this is allowed then lock also gets uncrossed
                            if (j == 10 && buttons[i][11].getText().equals("X")) {
                                uncrossButton(i, 11);
                                player.sheet.removeCross(i, 11);
                            }

                            player.sheet.removeCross(i, j);
                            numberCrossesInRound--;
                            allCrossedNumbersInOrder.remove(indexStored);
                        } else {
                            JOptionPane.showMessageDialog(this, "Uncrossing this number is not allowed",
                                    "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (player.sheet.getValidRow(i)){ // Player wants to cross the button and check if the color is still existing
                        crossButton(i, j);
                        // Checks if a number can be crossed based on the logic of the score sheet of the human player
                        // If an active player has 2 or more crosses he/she cannot cross anymore. If an inactive player
                        // has 1 or more crosses he/she cannot cross anymore.
                        if (player.sheet.canCross(i, j) &&
                                ((player.isActive && (numberCrossesInRound < maxCrossPerRoundActive)) ||
                                        (!player.isActive && numberCrossesInRound < maxCrossPerRoundInactive))) {
                            player.sheet.cross(i, j);
                            numberCrossesInRound++;
                            allCrossedNumbersInOrder.add(indexStored);

                            if (j == 10) { // If last number is crossed then (and this allowed) then lock can also be crossed. canCross already checks if a 12 or 2 are allowed to be crossed
                                // A lock does not count into the number of crosses done in a round
                                // If column 10 is crossed in Scoresheet and this is allowed then column 11 is automatically crossed
                                crossButton(i, 11);
                            }
                        } else if (!player.sheet.getValidRow(i)) {
                            JOptionPane.showMessageDialog(this, "This color has disappeared!",
                                    "ERROR", JOptionPane.ERROR_MESSAGE);
                        } else { // If a player may not cross the button, then it is also not stored in lastCrossedNumbers
                            uncrossButton(i, j);

                            if (!player.sheet.canCross(i, j)) {
                                JOptionPane.showMessageDialog(this, "Crossing this number is not allowed, please cross another number",
                                        "ERROR", JOptionPane.ERROR_MESSAGE);
                                // Display an error message that crossing this number is not allowed
                            } else if (!((player.isActive && (numberCrossesInRound < maxCrossPerRoundActive)) ||
                                    (!player.isActive && numberCrossesInRound < maxCrossPerRoundInactive))){
                                JOptionPane.showMessageDialog(this, "The maximum number of allowed crosses for this round is reached",
                                        "ERROR", JOptionPane.ERROR_MESSAGE);
                                // Display an error message that the maximum number of allowed crosses per round is reached
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Method that puts a cross for a penalty.
     * @param k indicates at which index a penalty needs to be crossed
     */
    public void crossPenalty(int k) {
        player.sheet.addPenalty();
        penalties[k].setText("X");
        penalties[k].setFont(new Font("MV Boli", Font.PLAIN, 10));
        penalties[k].setBackground(new Color(204, 204, 204));
        penalties[k].setForeground(Color.black);
    }

    /**
     * Method puts a cross a button for a given row and column.
     * @param i index which indicates at which row the cross needs to be put
     * @param j index which indicates at which column the cross needs to be
     */
    private void crossButton(int i, int j) {
        buttons[i][j].setText("X");
        buttons[i][j].setFont(new Font("MV Boli", Font.PLAIN, 12));
        buttons[i][j].setForeground(Color.BLACK);

        if (j == 11) {
            buttons[i][j].setHorizontalTextPosition(JButton.CENTER);
            buttons[i][j].setVerticalTextPosition(JButton.CENTER);
            buttons[i][j].setForeground(Color.black);
        }
    }

    /**
     * Method which uncrosses a button for a given row and column.
     * @param i index which indicates at which row the cross needs to be removed
     * @param j index which indicates at which column the cross needs to be removed
     */
    private void uncrossButton(int i, int j) {
        int value;

        if (j == 11) {
            buttons[i][j].setHorizontalTextPosition(JButton.CENTER);
            buttons[i][j].setVerticalTextPosition(JButton.CENTER);
            buttons[i][j].setText(" ");
        } else {
            if (i == 0 || i == 1) {
                value = 2 + j;
            } else {
                value = 12 - j;
            }

            buttons[i][j].setText(String.valueOf(value));
            buttons[i][j].setFont(new Font("MV Boli", Font.PLAIN, 12));
            if (i == 0) {
                buttons[i][j].setForeground(new Color(204, 0, 0));
            } else if (i == 1) {
                buttons[i][j].setForeground(new Color(255, 204, 0));
            } else if (i == 2) {
                buttons[i][j].setForeground(new Color(0, 153, 0));
            } else {
                buttons[i][j].setForeground(new Color(0, 0, 204));
            }
        }
    }

    /**
     * Method which create the buttons for every element, which means we get four rows and twelve columns with buttons.
     */
    private void createButtons() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                if (j == 11) {
                    if (i == 0) {
                        //for the last column, the locks are added
                        buttons[i][j] = new JButton(new ImageIcon(((new ImageIcon(
                                "src/Red_Lock.png").getImage()
                                .getScaledInstance(30, 30,
                                        java.awt.Image.SCALE_SMOOTH)))));
                    } else if (i == 1) {
                        buttons[i][j] = new JButton(new ImageIcon(((new ImageIcon(
                                "src/Yellow_Lock.png").getImage()
                                .getScaledInstance(30, 30,
                                        java.awt.Image.SCALE_SMOOTH)))));
                    } else if (i == 2) {
                        buttons[i][j] = new JButton(new ImageIcon(((new ImageIcon(
                                "src/Green_Lock.png").getImage()
                                .getScaledInstance(30, 30,
                                        java.awt.Image.SCALE_SMOOTH)))));
                    } else {
                        buttons[i][j] = new JButton(new ImageIcon(((new ImageIcon(
                                "src/Blue_Lock.png").getImage()
                                .getScaledInstance(30, 30,
                                        java.awt.Image.SCALE_SMOOTH)))));
                    }
                } else {
                    buttons[i][j] = new JButton();
                }
                buttons[i][j].setFocusable(false);
                buttons[i][j].addActionListener(this);
                button_panel.add(buttons[i][j]);
                buttons[i][j].setFont(new Font("MV Boli", Font.PLAIN, 12));

                int value = j + 2;

                if (i == 0) {
                    buttons[i][j].setBackground(new Color(255, 102, 102));
                    buttons[i][j].setForeground(new Color(204, 0, 0));
                    if (j != 11) {
                        buttons[i][j].setText(String.valueOf(value));
                    }
                } else if (i == 1) {
                    buttons[i][j].setBackground(new Color(255, 255, 153));
                    buttons[i][j].setForeground(new Color(255, 204, 0));
                    if (j != 11) {
                        buttons[i][j].setText(String.valueOf(value));
                    }
                } else if (i == 2) {
                    buttons[i][j].setBackground(new Color(102, 255, 102));
                    buttons[i][j].setForeground(new Color(0, 153, 0));
                    if (j != 11) {
                        buttons[i][j].setText(String.valueOf(12 - j));
                    }
                } else {
                    buttons[i][j].setBackground(new Color(51, 204, 255));
                    buttons[i][j].setForeground(new Color(0, 0, 204));
                    if (j != 11) {
                        buttons[i][j].setText(String.valueOf(12 - j));
                    }
                }
            }
        }
    }

    /**
     * Method which creates four buttons for the penalties.
     */
    private void createPenalties() {
        for (int i = 0; i < 4; i++) {
            penalties[i] = new JButton();
            penalties_panel.add(penalties[i]);
            penalties[i].setFont(new Font("MV Boli", Font.PLAIN, 10));
            penalties[i].setFocusable(false);
            penalties[i].setBackground(new Color(204, 204, 204));
            penalties[i].addActionListener(this);
        }
    }

    /**
     * Method which creates the Panel where the title is added with a name which the human player can give.
     */
    private void createTitlePanel() {
        title.setBackground(new Color(0, 0, 153));
        title.setForeground(new Color(204, 204, 204));
        title.setFont(new Font("Ink Free", Font.BOLD, 15));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setText("Qwixx Score Sheet from (type your name):");
        title.setOpaque(true);

        inputName.setBackground(new Color(0, 0, 153));
        inputName.setForeground(new Color(204, 204, 204));
        inputName.setFont(new Font("Ink Free", Font.BOLD, 15));
        inputName.setOpaque(true);
        player.setName(inputName.getText()); // Sets the name of the human player

        title_panel.setLayout(new BorderLayout());
        title_panel.setBounds(0, 0, 600, 20);
    }

    /**
     * Method which creates a panel where is shown how many points for how many crosses per row can be earned
     */
    private void createPointsPanel() {
        int value = 0;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 13; j++) {
                if (i == 0) {
                    if (j == 0) {
                        points[i][j] = new JLabel("Crosses");
                    } else {
                        points[i][j] = new JLabel(j + "x");
                        points[i][j].setBackground(new Color(204, 204, 204));
                        points[i][j].setBackground(Color.black);
                    }
                } else if (j == 0) {
                    points[i][j] = new JLabel("Points");
                } else {
                    value += j;
                    points[i][j] = new JLabel(String.valueOf(value));
                    points[i][j].setBackground(new Color(204, 204, 204));
                    points[i][j].setBackground(Color.black);
                }

                pointsPanel.add(points[i][j]);
                points[i][j].setFont(new Font("MV Boli", Font.PLAIN, 10));
                points[i][j].setFocusable(false);
                points[i][j].setBackground(new Color(204, 204, 204));
            }
        }
    }

    /**
     * Method which creates the panel where the total score of the player will be displayed (at the end of the game).
     */
    private void createScorePanel() {
        signs[0] = new JLabel("Score");
        signs[0].setBackground(new Color(204, 204, 204));
        signs[0].setForeground(Color.black);
        signs[0].setFocusable(false);
        signs[0].setFont(new Font("MV Boli", Font.PLAIN, 10));
        scorePanel.add(signs[0]);

        pointsScored[0] = new JButton();
        pointsScored[0].setBackground(Color.WHITE);
        pointsScored[0].setForeground(Color.black); // Text color
        pointsScored[0].setBorder(BorderFactory.createLineBorder(new Color(204, 0, 0))); // Border color
        pointsScored[0].setFocusable(false);
        pointsScored[0].setFont(new Font("MV Boli", Font.PLAIN, 12));
        scorePanel.add(pointsScored[0]);

        signs[1] = new JLabel("+");
        signs[1].setBackground(new Color(204, 204, 204));
        signs[1].setForeground(Color.black);
        signs[1].setFocusable(false);
        signs[1].setFont(new Font("MV Boli", Font.PLAIN, 10));
        scorePanel.add(signs[1]);

        pointsScored[1] = new JButton();
        pointsScored[1].setBackground(Color.WHITE);
        pointsScored[1].setForeground(Color.black);
        pointsScored[1].setBorder(BorderFactory.createLineBorder(new Color(255, 204, 0)));
        pointsScored[1].setFocusable(false);
        pointsScored[1].setFont(new Font("MV Boli", Font.PLAIN, 12));
        scorePanel.add(pointsScored[1]);

        signs[2] = new JLabel("+");
        signs[2].setBackground(new Color(204, 204, 204));
        signs[2].setForeground(Color.black);
        signs[2].setFocusable(false);
        signs[2].setFont(new Font("MV Boli", Font.PLAIN, 10));
        scorePanel.add(signs[2]);

        pointsScored[2] = new JButton();
        pointsScored[2].setBackground(Color.white);
        pointsScored[2].setForeground(Color.black);
        pointsScored[2].setBorder(BorderFactory.createLineBorder(new Color(0, 153, 0)));
        pointsScored[2].setFocusable(false);
        pointsScored[2].setFont(new Font("MV Boli", Font.PLAIN, 12));
        scorePanel.add(pointsScored[2]);

        signs[3] = new JLabel("+");
        signs[3].setBackground(new Color(204, 204, 204));
        signs[3].setForeground(Color.black);
        signs[3].setFocusable(false);
        signs[3].setFont(new Font("MV Boli", Font.PLAIN, 10));
        scorePanel.add(signs[3]);

        pointsScored[3] = new JButton();
        pointsScored[3].setBackground(Color.white);
        pointsScored[3].setForeground(Color.black);
        pointsScored[3].setBorder(BorderFactory.createLineBorder(new Color(0, 0, 204)));
        pointsScored[3].setFocusable(false);
        pointsScored[3].setFont(new Font("MV Boli", Font.PLAIN, 12));
        scorePanel.add(pointsScored[3]);

        signs[4] = new JLabel("-");
        signs[4].setBackground(new Color(204, 204, 204));
        signs[4].setForeground(Color.black);
        signs[4].setFocusable(false);
        signs[4].setFont(new Font("MV Boli", Font.PLAIN, 10));
        scorePanel.add(signs[4]);

        // This are the total penalties
        pointsScored[4] = new JButton();
        pointsScored[4].setBackground(Color.white);
        pointsScored[4].setForeground(Color.BLACK);
        pointsScored[4].setBorder(BorderFactory.createLineBorder(Color.darkGray));
        pointsScored[4].setFocusable(false);
        pointsScored[4].setFont(new Font("MV Boli", Font.PLAIN, 12));
        scorePanel.add(pointsScored[4]);

        signs[5] = new JLabel("=");
        signs[5].setBackground(new Color(204, 204, 204));
        signs[5].setForeground(Color.black);
        signs[5].setFocusable(false);
        signs[5].setFont(new Font("MV Boli", Font.PLAIN, 10));
        scorePanel.add(signs[5]);

        // Total points scored
        pointsScored[5] = new JButton();
        pointsScored[5].setBackground(Color.white);
        pointsScored[5].setForeground(Color.black);
        pointsScored[5].setBorder(BorderFactory.createLineBorder(Color.darkGray));
        pointsScored[5].setFocusable(false);
        pointsScored[5].setFont(new Font("MV Boli", Font.PLAIN, 12));
        scorePanel.add(pointsScored[5]);
    }

    /**
     * Method that updates the panel for every row when the player is finished.
     */
    public void updatePanelWhenFinished() {
        for(int i = 0; i < 4; i++) {
            pointsScored[i].setText(String.valueOf(player.sheet.getScore(i)));
        }

        pointsScored[4].setText(String.valueOf(player.sheet.getPenaltyValue() * player.sheet.getPenalty()));
        pointsScored[5].setText(String.valueOf(player.sheet.getTotalScore()));
    }

    /**
     * Method that returns the score sheet of the HumanPlayer.
     * @return a panel containing the score sheet of the human player
     */
    public JPanel getScoreSheetHumanPlayer() {
        return mainPanel;
    }

    /**
     * Method that returns a list with the latest numbers that are crossed.
     * @return a list with the numbers that are crossed most recently (the last one)
     */
    public List<String> getLastCrossedNumbers() {
        // When the round is finished (Skip round or Finish is clicked) but we want to check on the crosses in last round
        // to see if they correspond to the dice values.
        if (allCrossedNumbersInOrder.size() > 2) {
            return allCrossedNumbersInOrder.subList(allCrossedNumbersInOrder.size() - numberCrossesLastRound, allCrossedNumbersInOrder.size());
        } else if (allCrossedNumbersInOrder.size() == 2) {
            if (numberCrossesLastRound == 2) {
                return allCrossedNumbersInOrder;
            } else {
                return allCrossedNumbersInOrder.subList(1, 2);
            }
        }
        return allCrossedNumbersInOrder;
    }

    /**
     * Method which gives an error when one of the numbers that is crossed in the last round is not valid when comparing
     * it to the values on the dices.
     * @param i an integer which indicates the number of crosses that has been made in the last round which were incorrect
     */
    public void displayErrorMessageRemote(int i) {
        // The human player already clicked on finished, but made a mistake, then all the last crossed numbers are added
        // such that a modification can be made by the human player
        numberCrossesInRound = i;
        JOptionPane.showMessageDialog(this, "The number(s) you have just crossed are not valid " +
                        "(e.g. they do not correspond to the dice values). Uncross the button you have just clicked and " +
                        "make sure to cross the number that corresponds to the dice values and hit the finish button or " +
                        "skip this round.",
                "ERROR", JOptionPane.ERROR_MESSAGE);

    }

    /**
     * Method that displays an error when the order in which the crosses are placed in the same round are not correct.
     * @param i the number of crosses in the last round
     */
    public void displayErrorMessageOrder(int i) {
        numberCrossesInRound = i;
        JOptionPane.showMessageDialog(this, "The order in which you crossed the number is not"  + "\n"+
                        " correct. If you want to cross numbers in the same row, you first have to cross the combination" + "\n" +
                        " of the white dice and then a colored combination.",
                "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Method that displays an error when the cross is not valid.
     * @param i the number of crosses in the last round
     */
    public void displayErrorMessageOnlyColored(int i) {
        numberCrossesInRound = i;
        JOptionPane.showMessageDialog(this, "The numbers you have just crossed are not valid. "  + "\n"+
                        "Uncross the button you have just clicked and make sure to choose first a combination of the " + "\n" +
                        "white dice and then a combination of a white and colored die." + "\n" +"Do not forget to hit the finish button.",
                "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Method that returns the amount of crosses a player has done in one single round.
     * @return the amount of crosses that a player placed in one round
     */
    public int getCrossesInRound() {
        return this.numberCrossesInRound;
    }

    /**
     * Method that sets the amount of crosses that are done in one round.
     * @param i integer which indicates how many crosses in one round are placed
     */
    public void setCrossesInRound(int i) {
        this.numberCrossesInRound = i;
    }

    /**
     * Method which sets the amount of crosses that are done in the last round.
     * @param i integer which indicates how many crosses are placed in the last round
     */
    public void setCrossesLastRound(int i) {
        this.numberCrossesLastRound = i;
    }

    /**
     * Method which sets the round to being ended.
     */
    public void setRoundIsEnded() {
        this.roundIsEnded = true;
    }
}
