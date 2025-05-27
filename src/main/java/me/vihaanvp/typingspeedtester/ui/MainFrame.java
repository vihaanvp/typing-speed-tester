package me.vihaanvp.typingspeedtester.ui;

import me.vihaanvp.typingspeedtester.logic.SentenceProvider;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
    private JComboBox<String> difficultyBox;
    private JTextPane sentencePane;
    private JTextField inputField;
    private JLabel timerLabel, wpmLabel, accuracyLabel, errorLabel;

    private final SentenceProvider sentenceProvider;
    private String sentence = "";
    private long startTime = 0;
    private boolean running = false;
    private boolean timerStarted = false;
    private Timer timer;

    public MainFrame() {
        setTitle("Typing Speed Tester");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 350);
        setLocationRelativeTo(null);
        setJMenuBar(createMenuBar());

        // Use your actual SentenceProvider here
        sentenceProvider = new SentenceProvider();

        initComponents();
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        difficultyBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        JButton startButton = new JButton("Start Test");
        topPanel.add(new JLabel("Difficulty:"));
        topPanel.add(difficultyBox);
        topPanel.add(startButton);

        sentencePane = new JTextPane();
        sentencePane.setEditable(false);
        sentencePane.setFont(new Font("Consolas", Font.PLAIN, 18));
        JScrollPane sentenceScroll = new JScrollPane(sentencePane);

        inputField = new JTextField();
        inputField.setFont(new Font("Consolas", Font.PLAIN, 18));
        inputField.setEnabled(false);

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timerLabel = new JLabel("Time: 0.0s");
        wpmLabel = new JLabel("WPM: ");
        accuracyLabel = new JLabel("Accuracy: ");
        errorLabel = new JLabel("Errors: ");
        infoPanel.add(timerLabel);
        infoPanel.add(wpmLabel);
        infoPanel.add(accuracyLabel);
        infoPanel.add(errorLabel);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(sentenceScroll, BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);
        add(infoPanel, BorderLayout.WEST);

        startButton.addActionListener(e -> startTest());
        inputField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (!running) return;

                // Start timer on first key press
                if (!timerStarted) {
                    startTime = System.currentTimeMillis();
                    timer = new Timer(50, evt -> {
                        if (running && timerStarted)
                            timerLabel.setText(String.format("Time: %.1fs", (System.currentTimeMillis() - startTime) / 1000.0));
                    });
                    timer.start();
                    timerStarted = true;
                }

                updateSentenceHighlight();
                updateStats();
                if (inputField.getText().equals(sentence)) endTest();
            }
        });
    }

    private void startTest() {
        String difficulty = (String) difficultyBox.getSelectedItem();
        sentence = sentenceProvider.getRandomSentence(difficulty);
        sentencePane.setText(sentence);
        inputField.setText("");
        inputField.setEnabled(true);
        inputField.requestFocusInWindow();

        timerLabel.setText("Time: 0.0s");
        wpmLabel.setText("WPM: ");
        accuracyLabel.setText("Accuracy: ");
        errorLabel.setText("Errors: ");

        running = true;
        timerStarted = false;
        if (timer != null) timer.stop();
    }

    private void endTest() {
        // Calculate stats before stopping the timer
        int wpm = calcWPM();
        int accuracy = calcAccuracy();
        int errors = calcErrors();

        running = false;
        timerStarted = false;
        if (timer != null) timer.stop();
        inputField.setEnabled(false);

        JOptionPane.showMessageDialog(this, "Test complete!\nWPM: " + wpm + "\nAccuracy: " + accuracy + "%\nErrors: " + errors);
    }

    private void updateSentenceHighlight() {
        String input = inputField.getText();
        StyledDocument doc = sentencePane.getStyledDocument();
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet correct = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(0, 200, 0));
        AttributeSet error = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.RED);
        AttributeSet normal = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, UIManager.getColor("Label.foreground"));

        sentencePane.setText(sentence);
        for (int i = 0; i < sentence.length(); i++) {
            AttributeSet style = normal;
            if (i < input.length()) {
                if (input.charAt(i) == sentence.charAt(i)) style = correct;
                else style = error;
            }
            doc.setCharacterAttributes(i, 1, style, true);
        }
    }

    private void updateStats() {
        wpmLabel.setText("WPM: " + calcWPM());
        accuracyLabel.setText("Accuracy: " + calcAccuracy() + "%");
        errorLabel.setText("Errors: " + calcErrors());
    }

    private int calcWPM() {
        if (!timerStarted) return 0;
        long elapsedMillis = System.currentTimeMillis() - startTime;
        double elapsedMin = Math.max(elapsedMillis / 60000.0, 1.0/60); // at least 1 second
        int wordCount = inputField.getText().isEmpty() ? 0 : inputField.getText().trim().split("\\s+").length;
        return (int) Math.round(wordCount / elapsedMin);
    }

    private int calcAccuracy() {
        String input = inputField.getText();
        int correct = 0;
        int len = Math.min(input.length(), sentence.length());
        for (int i = 0; i < len; i++) {
            if (input.charAt(i) == sentence.charAt(i)) correct++;
        }
        return !sentence.isEmpty() ? (int) (100.0 * correct / sentence.length()) : 100;
    }

    private int calcErrors() {
        String input = inputField.getText();
        int errors = 0;
        int len = Math.min(input.length(), sentence.length());
        for (int i = 0; i < len; i++) {
            if (input.charAt(i) != sentence.charAt(i)) errors++;
        }
        errors += Math.abs(sentence.length() - input.length());
        return errors;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");
        JMenuItem customSentences = new JMenuItem("Add Custom Sentences...");
        customSentences.addActionListener(e -> {
            // Open folder
            SentenceProvider.openAppDirectory();
            // Show tutorial
            JOptionPane.showMessageDialog(this, SentenceProvider.getCustomSentenceTutorial(), "How to Add Custom Sentences", JOptionPane.INFORMATION_MESSAGE);
        });
        optionsMenu.add(customSentences);
        menuBar.add(optionsMenu);
        return menuBar;
    }
}