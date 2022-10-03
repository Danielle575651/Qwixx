import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class ScoreSheetGUI implements ActionListener {
    JFrame frame = new JFrame();
    JPanel title_panel = new JPanel();
    JPanel button_panel = new JPanel();
    JLabel textfield = new JLabel();
    JButton[][] buttons = new JButton[4][12];

    ScoreSheetGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.getContentPane().setBackground(new Color(204, 204, 204));
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        textfield.setBackground(new Color(0,0,153));
        textfield.setForeground(Color.white);
        textfield.setFont(new Font("Ink Free", Font.BOLD, 75));
        textfield.setHorizontalAlignment(JLabel.CENTER);
        textfield.setText("Qwixx");
        textfield.setOpaque(true);

        title_panel.setLayout(new BorderLayout());
        title_panel.setBounds(0, 0, 800, 50);

        createButtons();

        button_panel.setLayout(new GridLayout(4, 12));
        button_panel.setBackground(new Color(204,204, 204));

        title_panel.add(textfield);
        frame.add(title_panel, BorderLayout.NORTH);
        frame.add(button_panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                if (e.getSource() == buttons[i][j]) {
                    buttons[i][j].setText("X");
                    buttons[i][j].setFont(new Font("MV Boli", Font.PLAIN, 30));
                    if (j == 11) {
                        buttons[i][j].setHorizontalTextPosition(JButton.CENTER);
                        buttons[i][j].setVerticalTextPosition(JButton.CENTER);
                        buttons[i][j].setForeground(Color.black);
                    }
                }
            }
        }
    }

    public void createButtons() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                if (j == 11) {
                    if (i == 0) {
                        buttons[i][j] = new JButton(new ImageIcon(((new ImageIcon(
                                "src/Red_Lock.png").getImage()
                                .getScaledInstance(64, 64,
                                        java.awt.Image.SCALE_SMOOTH)))));
                    } else if (i == 1) {
                        buttons[i][j] = new JButton(new ImageIcon(((new ImageIcon(
                                "src/Yellow_Lock.png").getImage()
                                .getScaledInstance(64, 64,
                                        java.awt.Image.SCALE_SMOOTH)))));
                    } else if (i == 2) {
                        buttons[i][j] = new JButton(new ImageIcon(((new ImageIcon(
                                "src/Green_Lock.png").getImage()
                                .getScaledInstance(64, 64,
                                        java.awt.Image.SCALE_SMOOTH)))));
                    } else {
                        buttons[i][j] = new JButton(new ImageIcon(((new ImageIcon(
                                "src/Blue_Lock.png").getImage()
                                .getScaledInstance(64, 64,
                                        java.awt.Image.SCALE_SMOOTH)))));
                    }
                } else {
                    buttons[i][j] = new JButton();
                }
                button_panel.add(buttons[i][j]);
                buttons[i][j].setFont(new Font("MV Boli", Font.PLAIN, 20));
                buttons[i][j].setFocusable(false);
                buttons[i][j].addActionListener(this);
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
                } else  {
                    buttons[i][j].setBackground(new Color(51, 204, 255));
                    buttons[i][j].setForeground(new Color(0, 0, 204));
                    if (j != 11) {
                        buttons[i][j].setText(String.valueOf(12 - j));
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        ScoreSheetGUI newSheet = new ScoreSheetGUI();

    }
}
