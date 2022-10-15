import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

/**
 * This is a GUI for the 6 dice. The play can toss the dice and use the corresponding points to make a move on the
 * score sheet.  The colored dice can also be removed from the game. In that case, both players cannot use the dice anymore.
 *
 * @author Amber Cuijpers, Danielle Lam, Khue Nguyen, Yu-Shan Cho, Yuntong Wu
 */
public class DiceGUI extends JPanel {
    ///An array of the 6 dice used in the game. The first two dice are white, followed by red, yellow, green and blue.
    private final JLabel[] picLabels;
    ///An array of the 6 dice used in the game. The first two dice are white, followed by red, yellow, green and blue.
    private final Dice[] diceSet;
    //An array of the points of the corresponding dice.
    private final int[] points;
    //A button for the players to press to toss the dice.
    private JButton nextRound;
    //A panel containing the pictures of the 6 dice.
    private JPanel dicePanel = new JPanel();
    //A panel containing the toss dice button.

    private JPanel BPanel = new JPanel();
    //The main panel combining the above two panels

    private JPanel mainPanel = new JPanel();

    /**
     * A constructor that initialize the Dice GUI by creating the main panel and adding the components to the panel
     */
    public DiceGUI() {
        picLabels = new JLabel[6];
        diceSet = new Dice[6];
        addDice();
        points = new int[6];

        dicePanel.setLayout(new GridLayout(1, 6));
        initDicePanel(dicePanel);

        nextRound = new JButton(new AbstractAction("Toss dice") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dicePanel.removeAll();
                dicePanel.revalidate();
                dicePanel.repaint();
                fillDicePanel(dicePanel);
            }
        });
        BPanel.add(nextRound);

        mainPanel.setLayout(new GridLayout(1, 2));
        mainPanel.add(BPanel);
        mainPanel.add(dicePanel);
    }

    /**
     * Initialize the dice panel by adding 6 question marks when the game has not started
     * @param panel the panel for the 6 pictures to be added
     */
    public void initDicePanel(JPanel panel) {
        setLayout(new FlowLayout());
        // Do a switch statement to add the question marks to the corresponding position in the panel
        picLabels[0] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("wQ.jpg"))).getImage()
                .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
        picLabels[1] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("wQ.jpg"))).getImage()
                .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
        picLabels[2] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("rQ.jpg"))).getImage()
                .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
        picLabels[3] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("yQ.jpg"))).getImage()
                .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
        picLabels[4] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("gQ.jpg"))).getImage()
                .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
        picLabels[5] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("bQ.jpg"))).getImage()
                .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));

        for (JLabel l : picLabels) {
            panel.add(l, BorderLayout.SOUTH);
        }

        add(panel);
    }

    /**
     * Fill the panel with dice of corresponding points when the dice are tossed
     * @param panel the panel to be filled with dice. In this project, it is always the dice panel
     */
    public void fillDicePanel(JPanel panel) {
        setLayout(new FlowLayout());
        rollDice();

        // For each position of the dice, do a switch statement to determine which picture should be put in
        for (int i = 0; i < points.length; i++) {
            if (i == 0) {
                switch (points[i]) {
                    case 1 ->
                            picLabels[0] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w1.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 2 ->
                            picLabels[0] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w2.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 3 ->
                            picLabels[0] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w3.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 4 ->
                            picLabels[0] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w4.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 5 ->
                            picLabels[0] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w5.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 6 ->
                            picLabels[0] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w6.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                }
            } else if (i == 1) {
                switch (points[i]) {
                    // For the colored dice, the points is 0 - which is however impossible, we put the sign of "removed"
                    // at the corresponding position
                    case 1 ->
                            picLabels[1] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w1.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 2 ->
                            picLabels[1] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w2.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 3 ->
                            picLabels[1] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w3.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 4 ->
                            picLabels[1] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w4.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 5 ->
                            picLabels[1] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w5.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 6 ->
                            picLabels[1] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w6.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                }
            } else if (i == 2) {
                switch (points[i]) {
                    case 0 ->
                            picLabels[2] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("rC.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 1 ->
                            picLabels[2] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("r1.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 2 ->
                            picLabels[2] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("r2.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 3 ->
                            picLabels[2] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("r3.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 4 ->
                            picLabels[2] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("r4.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 5 ->
                            picLabels[2] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("r5.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 6 ->
                            picLabels[2] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("r6.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                }
            } else if (i == 3) {
                switch (points[i]) {
                    case 0 ->
                            picLabels[3] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("yC.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 1 ->
                            picLabels[3] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("y1.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 2 ->
                            picLabels[3] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("y2.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 3 ->
                            picLabels[3] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("y3.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 4 ->
                            picLabels[3] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("y4.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 5 ->
                            picLabels[3] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("y5.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 6 ->
                            picLabels[3] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("y6.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                }
            } else if (i == 4) {
                switch (points[i]) {
                    case 0 ->
                            picLabels[4] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("gC.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 1 ->
                            picLabels[4] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("g1.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 2 ->
                            picLabels[4] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("g2.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 3 ->
                            picLabels[4] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("g3.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 4 ->
                            picLabels[4] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("g4.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 5 ->
                            picLabels[4] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("g5.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 6 ->
                            picLabels[4] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("g6.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                }
            } else {
                switch (points[i]) {
                    case 0 ->
                            picLabels[5] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("bC.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 1 ->
                            picLabels[5] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("b1.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 2 ->
                            picLabels[5] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("b2.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 3 ->
                            picLabels[5] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("b3.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 4 ->
                            picLabels[5] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("b4.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 5 ->
                            picLabels[5] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("b5.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 6 ->
                            picLabels[5] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("b6.jpg"))).getImage()
                                    .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                }
            }
        }

        for (JLabel l : picLabels) {
            panel.add(l);
        }
    }

    /**
     * A method that adds all the six dice (two whites, one red, one yellow, one green, one blue) in the dice set.
     */
    public void addDice() {
        diceSet[0] = new Dice(0);
        diceSet[1] = new Dice(1);
        diceSet[2] = new Dice(2);
        diceSet[3] = new Dice(3);
        diceSet[4] = new Dice(4);
        diceSet[5] = new Dice(5);
    }

    /**
     * Method that roll the dice and get the corresponding point of the dice
     */
    public void rollDice() {
        // We roll each of the dice and update its point unless it is not removed from the game
        for (int i = 0; i < diceSet.length; i++) {
            if (!diceSet[i].isRemoved()) {
                Dice d = diceSet[i];
                d.rollDice();
                points[i] = d.getValue();
            }
        }
    }

    /**
     * A method that removes the die from the game and put a removed sign at the position of the removed die
     * @param d the dice to be removed
     */
    public void removeDice(Dice d) {
        // Use a loop to find the dice to be removed and a dice of the same color but different state
        // in the corresponding position
        for (int i = 0; i < diceSet.length; i++) {
            if (diceSet[i].equals(d)) {
                int color = d.getColor();
                Dice dice = new Dice(color);
                dice.changeState();
                diceSet[i] = dice;
            }
        }
        // If the die is removed from the game, we set its point to 0 and it will no longer be updated
        points[d.getColor()] = 0;

        // Do a switch statement to put a removed sign in the position of removed die
        switch (d.getColor()) {
            case 2 ->
                    picLabels[2] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("rC.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
            case 3 ->
                    picLabels[3] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("yC.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
            case 4 ->
                    picLabels[4] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("gC.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
            case 5 ->
                    picLabels[5] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("bC.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
        }

        // Update the panel
        dicePanel.removeAll();
        dicePanel.revalidate();
        dicePanel.repaint();
        for (JLabel l : picLabels) {
            dicePanel.add(l);
        }
    }

    /**
     * A method that returns the current points of the 6 dice
     * @return the points that the 6 dice have
     */
    public int[] getCurrentPoints() {
        return this.points;
    }

    /**
     * Return the dice set in the game
     * @return the 6 dice in the game
     */
    public Dice[] getDiceSet() {
        return this.diceSet;
    }

    /**
     * Remove the toss dice button
     * @return the toss dice button
     */
    public JButton nextRoundButton() {
        return this.nextRound;
    }

    /**
     * A method that returns the dice panel
     * @return
     */
    public JPanel getDicePanel() {
        return this.mainPanel;
    }

    /**
     * A method that disables the nextRound button
     */
    public void disableToss() {
        this.nextRound.setEnabled(false);
    }

    /**
     * A method that (re-)enables the nextRound button
     */
    public void enableToss() {
        this.nextRound.setEnabled(true);
    }
}
