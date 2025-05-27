package me.vihaanvp.typingspeedtester.ui;

import me.vihaanvp.typingspeedtester.logic.HighScoreManager;

import javax.swing.*;
import java.awt.*;

public class HighScorePanel extends JPanel {
    private JTextArea scoresArea;

    public HighScorePanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 0));
        JLabel lbl = new JLabel("High Scores");
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        add(lbl, BorderLayout.NORTH);

        scoresArea = new JTextArea();
        scoresArea.setEditable(false);
        add(new JScrollPane(scoresArea), BorderLayout.CENTER);
    }

    public void refresh(HighScoreManager highScoreManager) {
        scoresArea.setText(highScoreManager.getHighScoresText());
    }
}