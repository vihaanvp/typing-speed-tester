package me.vihaanvp.typingspeedtester.ui;

import me.vihaanvp.typingspeedtester.model.TestResult;

import javax.swing.*;
import java.awt.*;

public class ResultsDialog {
    public static void showResults(JFrame parent, TestResult result) {
        JDialog dialog = new JDialog(parent, "Test Results", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(parent);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Test Complete!", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Results panel
        JPanel resultsPanel = new JPanel(new GridLayout(8, 2, 10, 8));
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Results"));
        
        resultsPanel.add(new JLabel("Test Mode:"));
        resultsPanel.add(new JLabel(result.getTestMode().getDisplayName()));
        
        resultsPanel.add(new JLabel("Difficulty:"));
        resultsPanel.add(new JLabel(result.getDifficulty()));
        
        resultsPanel.add(new JLabel("Time:"));
        resultsPanel.add(new JLabel(String.format("%.1f seconds", result.getTimeSeconds())));
        
        JLabel wpmLabel = new JLabel(String.valueOf(result.getWpm()));
        wpmLabel.setFont(wpmLabel.getFont().deriveFont(Font.BOLD));
        wpmLabel.setForeground(getWpmColor(result.getWpm()));
        resultsPanel.add(new JLabel("Words per Minute:"));
        resultsPanel.add(wpmLabel);
        
        JLabel accLabel = new JLabel(result.getAccuracy() + "%");
        accLabel.setFont(accLabel.getFont().deriveFont(Font.BOLD));
        accLabel.setForeground(getAccuracyColor(result.getAccuracy()));
        resultsPanel.add(new JLabel("Accuracy:"));
        resultsPanel.add(accLabel);
        
        resultsPanel.add(new JLabel("Errors:"));
        resultsPanel.add(new JLabel(String.valueOf(result.getErrorCount())));
        
        resultsPanel.add(new JLabel("Characters Typed:"));
        resultsPanel.add(new JLabel(String.valueOf(result.getCharactersTyped())));
        
        resultsPanel.add(new JLabel("CPM:"));
        resultsPanel.add(new JLabel(String.format("%.0f", result.getCharactersPerMinute())));
        
        mainPanel.add(resultsPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(okButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    private static Color getWpmColor(int wpm) {
        if (wpm >= 60) return new Color(0, 150, 0); // Green
        else if (wpm >= 40) return new Color(255, 165, 0); // Orange
        else return new Color(200, 0, 0); // Red
    }
    
    private static Color getAccuracyColor(int accuracy) {
        if (accuracy >= 95) return new Color(0, 150, 0); // Green
        else if (accuracy >= 85) return new Color(255, 165, 0); // Orange
        else return new Color(200, 0, 0); // Red
    }
}