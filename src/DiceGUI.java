import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class DiceGUI extends JPanel {
    private final JLabel[] picLabels;
    private final Dice[] diceSet;
    private final int[] points;
    private JButton nextRound;
    private JPanel dicePanel = new JPanel();
    private JPanel BPanel = new JPanel();
    private JPanel mainPanel = new JPanel();

    public DiceGUI() {
        picLabels = new JLabel[6];

        diceSet = new Dice[6];
        addDice();

        points = new int[6];

        dicePanel.setLayout(new GridLayout(1,6));
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

        mainPanel.setLayout(new GridLayout(1,2));
        mainPanel.add(BPanel);
        mainPanel.add(dicePanel);
    }

    public void initDicePanel(JPanel panel) {
        setLayout(new FlowLayout());
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

    public void fillDicePanel(JPanel panel) {
        setLayout(new FlowLayout());
        rollDice();

        for (int i = 0; i < points.length; i++) {
            if (i == 0) {
                switch (points[i]) {
                    case 1 -> picLabels[0] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w1.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 2 -> picLabels[0] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w2.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 3 -> picLabels[0] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w3.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 4 -> picLabels[0] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w4.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 5 -> picLabels[0] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w5.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 6 -> picLabels[0] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w6.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                }
            } else if (i == 1) {
                switch (points[i]) {
                    case 1 -> picLabels[1] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w1.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 2 -> picLabels[1] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w2.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 3 -> picLabels[1] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w3.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 4 -> picLabels[1] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w4.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 5 -> picLabels[1] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w5.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 6 -> picLabels[1] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("w6.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                }
            } else if (i == 2) {
                switch (points[i]) {
                    case 1 -> picLabels[2] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("r1.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 2 -> picLabels[2] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("r2.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 3 -> picLabels[2] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("r3.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 4 -> picLabels[2] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("r4.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 5 -> picLabels[2] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("r5.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 6 -> picLabels[2] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("r6.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                }
            } else if (i == 3) {
                switch (points[i]) {
                    case 1 -> picLabels[3] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("y1.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 2 -> picLabels[3] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("y2.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 3 -> picLabels[3] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("y3.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 4 -> picLabels[3] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("y4.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 5 -> picLabels[3] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("y5.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 6 -> picLabels[3] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("y6.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                }
            } else if (i == 4) {
                switch (points[i]) {
                    case 1 -> picLabels[4] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("g1.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 2 -> picLabels[4] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("g2.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 3 -> picLabels[4] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("g3.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 4 -> picLabels[4] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("g4.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 5 -> picLabels[4] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("g5.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 6 -> picLabels[4] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("g6.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                }
            } else {
                switch (points[i]) {
                    case 1 -> picLabels[5] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("b1.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 2 -> picLabels[5] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("b2.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 3 -> picLabels[5] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("b3.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 4 -> picLabels[5] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("b4.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 5 -> picLabels[5] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("b5.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                    case 6 -> picLabels[5] = new JLabel(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("b6.jpg"))).getImage()
                            .getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH)));
                }
            }
        }

        for (JLabel l : picLabels) {
            panel.add(l);
        }
    }

    public void addDice() {
        diceSet[0] = new Dice(0);
        diceSet[1] = new Dice(1);
        diceSet[2] = new Dice(2);
        diceSet[3] = new Dice(3);
        diceSet[4] = new Dice(4);
        diceSet[5] = new Dice(5);
    }

    public void rollDice() {
        // Only roll the dice which are not null
        for (int i = 0; i < diceSet.length; i++) {
            if (diceSet[i] != null) {
                Dice d = diceSet[i];
                d.rollDice();
                points[i] = d.getValue();
            }
        }
    }

    public void removeDice(Dice d) {
        for (int i = 0; i < diceSet.length; i++) {
            if (diceSet[i].equals(d)) {
                diceSet[i] = null;
            }
        }

        points[d.getColor()] = 0;
    }

    public int[] getCurrentPoints() {
        return this.points;
    }

    public Dice[] getDiceSet() {
        return this.diceSet;
    }

    public JButton nextRoundButton() {
        return this.nextRound;
    }

    public JPanel getDicePanel() {
        return this.mainPanel;
    }

}