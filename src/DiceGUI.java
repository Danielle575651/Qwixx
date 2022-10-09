import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class DiceGUI extends JFrame {
    private final JLabel[] picLabels;
    private JPanel dicePanel;
    private Dice[] diceSet;
    private int[] points;
    private Map<String, Integer> colToNum;

    public DiceGUI() {
        picLabels = new JLabel[6];
        dicePanel = new JPanel();
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

        JPanel panel = new JPanel();
        panel.add(new JButton(new AbstractAction("Roll") {
            @Override
            public void actionPerformed(ActionEvent e) {
                DicePanel.removeAll();
                DicePanel.revalidate();
                DicePanel.repaint();
                fillDicePanel(DicePanel);
            }
        }));
        add(panel);

        setSize(800, 500);

    }

    public static void main(String[] args) {
        DiceGUI frame = new DiceGUI();
        frame.setTitle("Roll Dice");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void initDicePanel(JPanel panel) {
        setLayout(new FlowLayout());
        picLabels[0] = new JLabel(new ImageIcon(getClass().getResource("wQ.jpg")));
        picLabels[1] = new JLabel(new ImageIcon(getClass().getResource("wQ.jpg")));
        picLabels[2] = new JLabel(new ImageIcon(getClass().getResource("rQ.jpg")));
        picLabels[3] = new JLabel(new ImageIcon(getClass().getResource("yQ.jpg")));
        picLabels[4] = new JLabel(new ImageIcon(getClass().getResource("gQ.jpg")));
        picLabels[5] = new JLabel(new ImageIcon(getClass().getResource("bQ.jpg")));

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
                    case 0:
                        picLabels[0] = new JLabel(new ImageIcon(getClass().getResource("wC.jpg")));
                        break;
                    case 1:
                        picLabels[0] = new JLabel(new ImageIcon(getClass().getResource("w1.jpg")));
                        break;
                    case 2:
                        picLabels[0] = new JLabel(new ImageIcon(getClass().getResource("w2.jpg")));
                        break;
                    case 3:
                        picLabels[0] = new JLabel(new ImageIcon(getClass().getResource("w3.jpg")));
                        break;
                    case 4:
                        picLabels[0] = new JLabel(new ImageIcon(getClass().getResource("w4.jpg")));
                        break;
                    case 5:
                        picLabels[0] = new JLabel(new ImageIcon(getClass().getResource("w5.jpg")));
                        break;
                    case 6:
                        picLabels[0] = new JLabel(new ImageIcon(getClass().getResource("w6.jpg")));
                        break;
                }
            } else if (i == 1) {
                switch (points[i]) {
                    case 0:
                        picLabels[1] = new JLabel(new ImageIcon(getClass().getResource("wC.jpg")));
                        break;
                    case 1:
                        picLabels[1] = new JLabel(new ImageIcon(getClass().getResource("w1.jpg")));
                        break;
                    case 2:
                        picLabels[1] = new JLabel(new ImageIcon(getClass().getResource("w2.jpg")));
                        break;
                    case 3:
                        picLabels[1] = new JLabel(new ImageIcon(getClass().getResource("w3.jpg")));
                        break;
                    case 4:
                        picLabels[1] = new JLabel(new ImageIcon(getClass().getResource("w4.jpg")));
                        break;
                    case 5:
                        picLabels[1] = new JLabel(new ImageIcon(getClass().getResource("w5.jpg")));
                        break;
                    case 6:
                        picLabels[1] = new JLabel(new ImageIcon(getClass().getResource("w6.jpg")));
                        break;
                }
            } else if (i == 2) {
                switch (points[i]) {
                    case 0:
                        picLabels[2] = new JLabel(new ImageIcon(getClass().getResource("rC.jpg")));
                        break;
                    case 1:
                        picLabels[2] = new JLabel(new ImageIcon(getClass().getResource("r1.jpg")));
                        break;
                    case 2:
                        picLabels[2] = new JLabel(new ImageIcon(getClass().getResource("r2.jpg")));
                        break;
                    case 3:
                        picLabels[2] = new JLabel(new ImageIcon(getClass().getResource("r3.jpg")));
                        break;
                    case 4:
                        picLabels[2] = new JLabel(new ImageIcon(getClass().getResource("r4.jpg")));
                        break;
                    case 5:
                        picLabels[2] = new JLabel(new ImageIcon(getClass().getResource("r5.jpg")));
                        break;
                    case 6:
                        picLabels[2] = new JLabel(new ImageIcon(getClass().getResource("r6.jpg")));
                        break;
                }
            } else if (i == 3) {
                switch (points[i]) {
                    case 0:
                        picLabels[3] = new JLabel(new ImageIcon(getClass().getResource("yC.jpg")));
                        break;
                    case 1:
                        picLabels[3] = new JLabel(new ImageIcon(getClass().getResource("y1.jpg")));
                        break;
                    case 2:
                        picLabels[3] = new JLabel(new ImageIcon(getClass().getResource("y2.jpg")));
                        break;
                    case 3:
                        picLabels[3] = new JLabel(new ImageIcon(getClass().getResource("y3.jpg")));
                        break;
                    case 4:
                        picLabels[3] = new JLabel(new ImageIcon(getClass().getResource("y4.jpg")));
                        break;
                    case 5:
                        picLabels[3] = new JLabel(new ImageIcon(getClass().getResource("y5.jpg")));
                        break;
                    case 6:
                        picLabels[3] = new JLabel(new ImageIcon(getClass().getResource("y6.jpg")));
                        break;
                }
            } else if (i == 4) {
                switch (points[i]) {
                    case 0:
                        picLabels[4] = new JLabel(new ImageIcon(getClass().getResource("gC.jpg")));
                        break;
                    case 1:
                        picLabels[4] = new JLabel(new ImageIcon(getClass().getResource("g1.jpg")));
                        break;
                    case 2:
                        picLabels[4] = new JLabel(new ImageIcon(getClass().getResource("g2.jpg")));
                        break;
                    case 3:
                        picLabels[4] = new JLabel(new ImageIcon(getClass().getResource("g3.jpg")));
                        break;
                    case 4:
                        picLabels[4] = new JLabel(new ImageIcon(getClass().getResource("g4.jpg")));
                        break;
                    case 5:
                        picLabels[4] = new JLabel(new ImageIcon(getClass().getResource("g5.jpg")));
                        break;
                    case 6:
                        picLabels[4] = new JLabel(new ImageIcon(getClass().getResource("g6.jpg")));
                        break;
                }
            } else if (i == 5) {
                switch (points[i]) {
                    case 0:
                        picLabels[5] = new JLabel(new ImageIcon(getClass().getResource("bC.jpg")));
                        break;
                    case 1:
                        picLabels[5] = new JLabel(new ImageIcon(getClass().getResource("b1.jpg")));
                        break;
                    case 2:
                        picLabels[5] = new JLabel(new ImageIcon(getClass().getResource("b2.jpg")));
                        break;
                    case 3:
                        picLabels[5] = new JLabel(new ImageIcon(getClass().getResource("b3.jpg")));
                        break;
                    case 4:
                        picLabels[5] = new JLabel(new ImageIcon(getClass().getResource("b4.jpg")));
                        break;
                    case 5:
                        picLabels[5] = new JLabel(new ImageIcon(getClass().getResource("b5.jpg")));
                        break;
                    case 6:
                        picLabels[5] = new JLabel(new ImageIcon(getClass().getResource("b6.jpg")));
                        break;
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
