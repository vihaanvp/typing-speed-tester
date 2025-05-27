package me.vihaanvp.typingspeedtester.ui;

import me.vihaanvp.typingspeedtester.logic.TypingTestManager;

import javax.swing.*;

public class ResultsDialog {
    public static void showResults(JFrame parent, TypingTestManager testManager) {
        String msg = String.format(
                "<html><h2>Test Complete!</h2>" +
                        "<b>Time:</b> %.1fs<br>" +
                        "<b>WPM:</b> %d<br>" +
                        "<b>Accuracy:</b> %d%%<br>" +
                        "<b>Errors:</b> %d</html>",
                testManager.getElapsedTimeSec(),
                testManager.getWpm(),
                testManager.getAccuracy(),
                testManager.getErrorCount()
        );
        JOptionPane.showMessageDialog(parent, msg, "Results", JOptionPane.INFORMATION_MESSAGE);
    }
}