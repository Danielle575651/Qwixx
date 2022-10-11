import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DiceGUI extends JFrame {
    private final JLabel[] picLabels;
    private final Dice[] diceSet;
    private final int[] points;
    private Map<String, Integer> colToNum;
    private final int FRAME_HEIGHT = 800;
    private final int FRAMe_WIDTH = 1000;

    public DiceGUI() {
        picLabels = new JLabel[6];
        diceSet = new Dice[6];
        addDice(diceSet);
        points = new int[6];

        colToNum = new HashMap<>();
        colToNum = Map.of(
                "red", 2,
                "yellow", 3,
                "green", 4,
                "blue", 5);

        JPanel DicePanel = new JPanel();
        initDicePanel(DicePanel);

        JPanel BPanel = new JPanel();
        BPanel.add(new JButton(new AbstractAction("Roll Dice") {
            @Override
            public void actionPerformed(ActionEvent e) {
                DicePanel.removeAll();
                DicePanel.revalidate();
                DicePanel.repaint();
                fillDicePanel(DicePanel);
            }
        }));
        add(BPanel, DicePanel, FlowLayout.LEFT);

        setSize(FRAMe_WIDTH, FRAME_HEIGHT);
    }

    public static void main(String[] args) {
        DiceGUI frame = new DiceGUI();
        frame.setTitle("Qwixx");
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void initDicePanel(JPanel panel) {
        setLayout(new FlowLayout());
        picLabels[0] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("wQ.jpg"))));
        picLabels[1] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("wQ.jpg"))));
        picLabels[2] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("rQ.jpg"))));
        picLabels[3] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("yQ.jpg"))));
        picLabels[4] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("gQ.jpg"))));
        picLabels[5] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("bQ.jpg"))));

        for (JLabel l : picLabels) {
            panel.add(l);
        }

        add(panel);
    }

    public void fillDicePanel(JPanel panel) {
        setLayout(new FlowLayout());
        rollDice();

        for (int i = 0; i < points.length; i++) {
            if (i == 0) {
                switch (points[i]) {
                    case 1 -> picLabels[0] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("w1.jpg"))));
                    case 2 -> picLabels[0] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("w2.jpg"))));
                    case 3 -> picLabels[0] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("w3.jpg"))));
                    case 4 -> picLabels[0] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("w4.jpg"))));
                    case 5 -> picLabels[0] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("w5.jpg"))));
                    case 6 -> picLabels[0] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("w6.jpg"))));
                }
            } else if (i == 1) {
                switch (points[i]) {
                    case 1 -> picLabels[1] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("w1.jpg"))));
                    case 2 -> picLabels[1] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("w2.jpg"))));
                    case 3 -> picLabels[1] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("w3.jpg"))));
                    case 4 -> picLabels[1] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("w4.jpg"))));
                    case 5 -> picLabels[1] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("w5.jpg"))));
                    case 6 -> picLabels[1] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("w6.jpg"))));
                }
            } else if (i == 2) {
                switch (points[i]) {
                    case 0 -> picLabels[2] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("rC.jpg"))));
                    case 1 -> picLabels[2] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("r1.jpg"))));
                    case 2 -> picLabels[2] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("r2.jpg"))));
                    case 3 -> picLabels[2] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("r3.jpg"))));
                    case 4 -> picLabels[2] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("r4.jpg"))));
                    case 5 -> picLabels[2] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("r5.jpg"))));
                    case 6 -> picLabels[2] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("r6.jpg"))));
                }
            } else if (i == 3) {
                switch (points[i]) {
                    case 0 -> picLabels[3] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("yC.jpg"))));
                    case 1 -> picLabels[3] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("y1.jpg"))));
                    case 2 -> picLabels[3] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("y2.jpg"))));
                    case 3 -> picLabels[3] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("y3.jpg"))));
                    case 4 -> picLabels[3] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("y4.jpg"))));
                    case 5 -> picLabels[3] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("y5.jpg"))));
                    case 6 -> picLabels[3] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("y6.jpg"))));
                }
            } else if (i == 4) {
                switch (points[i]) {
                    case 0 -> picLabels[4] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("gC.jpg"))));
                    case 1 -> picLabels[4] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("g1.jpg"))));
                    case 2 -> picLabels[4] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("g2.jpg"))));
                    case 3 -> picLabels[4] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("g3.jpg"))));
                    case 4 -> picLabels[4] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("g4.jpg"))));
                    case 5 -> picLabels[4] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("g5.jpg"))));
                    case 6 -> picLabels[4] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("g6.jpg"))));
                }
            } else {
                switch (points[i]) {
                    case 0 -> picLabels[5] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("bC.jpg"))));
                    case 1 -> picLabels[5] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("b1.jpg"))));
                    case 2 -> picLabels[5] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("b2.jpg"))));
                    case 3 -> picLabels[5] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("b3.jpg"))));
                    case 4 -> picLabels[5] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("b4.jpg"))));
                    case 5 -> picLabels[5] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("b5.jpg"))));
                    case 6 -> picLabels[5] = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("b6.jpg"))));
                }
            }
        }

        for (JLabel l : picLabels) {
            panel.add(l);
        }

        add(panel);
    }

    public void addDice(Dice[] diceSet) {
        diceSet[0] = new Dice("white");
        diceSet[1] = new Dice("white");
        diceSet[2] = new Dice("red");
        diceSet[3] = new Dice("yellow");
        diceSet[4] = new Dice("green");
        diceSet[5] = new Dice("blue");
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
        // Make the corresponding die a null and its point 0
        diceSet[colToNum.get(d.getColor())] = null;
        points[colToNum.get(d.getColor())] = 0;
    }
}
