import java.awt.*;
import javax.swing.*;

public class ScoreSheetAIPlayerGUI {
    JPanel title_panel = new JPanel();
    JPanel button_panel = new JPanel();
    JPanel penalties_panel = new JPanel();
    JPanel pointsPanel = new JPanel();
    JPanel scorePanel = new JPanel();
    JPanel mainPanel = new JPanel();

    JLabel title = new JLabel();
    JLabel crossPenalty = new JLabel();
    JButton[][] buttons = new JButton[4][12];
    JButton[] penalties = new JButton[4];
    JLabel[][] points = new JLabel[2][13];
    JLabel[] signs = new JLabel[6];
    JButton[] pointsScored = new JButton[6];

    ScoreSheetAIPlayerGUI() {
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
        lowerPanel.setPreferredSize(new Dimension(600, 80));
        lowerPanel.add(points);
        lowerPanel.add(scorePanel);

        button_panel.setLayout(new GridLayout(4, 12));
        button_panel.setBackground(new Color(204, 204, 204));

        title_panel.add(title, BorderLayout.WEST);
        title_panel.setBackground(new Color(0, 0, 153));

        //BoxLayout testLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(title_panel, BorderLayout.PAGE_START);
        mainPanel.add(lowerPanel, BorderLayout.PAGE_END);
        mainPanel.add(button_panel);
        //mainPanel.setPreferredSize(new Dimension(600, 140));
    }

    public void crossPenalty(int k) {
        penalties[k].setText("X");
        penalties[k].setFont(new Font("MV Boli", Font.PLAIN, 10));
        penalties[k].setBackground(new Color(204, 204, 204));
        penalties[k].setForeground(Color.black);
    }

    public void crossButton(int i, int j) {
        buttons[i][j].setText("X");
        buttons[i][j].setFont(new Font("MV Boli", Font.PLAIN, 12));

        if (j == 11) {
            buttons[i][j].setHorizontalTextPosition(JButton.CENTER);
            buttons[i][j].setVerticalTextPosition(JButton.CENTER);
            buttons[i][j].setForeground(Color.black);
        }
    }

    public void uncrossButton(int i, int j) {
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
        }
    }

    private void createButtons() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                if (j == 11) {
                    if (i == 0) {
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
                button_panel.add(buttons[i][j]);
                buttons[i][j].setFont(new Font("MV Boli", Font.PLAIN, 12));
                buttons[i][j].setFocusable(false);
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

    private void createPenalties() {
        for (int i = 0; i < 4; i++) {
            penalties[i] = new JButton();
            penalties_panel.add(penalties[i]);
            penalties[i].setFont(new Font("MV Boli", Font.PLAIN, 10));
            penalties[i].setFocusable(false);
            penalties[i].setBackground(new Color(204, 204, 204));
        }
    }

    private void createTitlePanel() {
        title.setBackground(new Color(0, 0, 153));
        title.setForeground(new Color(204, 204, 204));
        title.setFont(new Font("Ink Free", Font.BOLD, 15));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setText("Qwixx Score Sheet from:  AI player");
        title.setOpaque(true);

        title_panel.setLayout(new BorderLayout());
        title_panel.setBounds(0, 0, 600, 20);
    }

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

    public void updatePanelWhenFinished(Scoresheet sheet) {
        for(int i = 0; i < 4; i++) {
            pointsScored[i].setText(String.valueOf(sheet.getScore(i)));
        }

        pointsScored[4].setText(String.valueOf(sheet.getPenaltyValue() * sheet.getPenalty()));
        pointsScored[5].setText(String.valueOf(sheet.getTotalScore()));
    }

    public JPanel getScorePanel() {
        return mainPanel;
    }
}
