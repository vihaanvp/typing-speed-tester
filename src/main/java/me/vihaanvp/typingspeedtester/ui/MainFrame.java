package me.vihaanvp.typingspeedtester.ui;

import me.vihaanvp.typingspeedtester.logic.HighScoreManager;
import me.vihaanvp.typingspeedtester.logic.SentenceProvider;
import me.vihaanvp.typingspeedtester.model.TestResult;
import me.vihaanvp.typingspeedtester.ui.ResultsDialog;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
    private JComboBox<String> difficultyBox;
    private JComboBox<TestResult.TestMode> testModeBox;
    private JTextPane sentencePane;
    private JTextField inputField;
    private JLabel timerLabel, wpmLabel, accuracyLabel, errorLabel, progressLabel;
    private JButton startButton, pauseButton;
    private JProgressBar progressBar;
    private HighScorePanel highScorePanel;

    private final SentenceProvider sentenceProvider;
    private final HighScoreManager highScoreManager;
    private String sentence = "";
    private long startTime = 0;
    private long pausedTime = 0;
    private boolean running = false;
    private boolean timerStarted = false;
    private boolean paused = false;
    private Timer timer;
    private TestResult.TestMode currentTestMode;
    private int testTimeLimit = -1; // -1 for no limit

    public MainFrame() {
        setTitle("TypeSpeedApp - Typing Speed Tester");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setJMenuBar(createMenuBar());

        sentenceProvider = new SentenceProvider();
        highScoreManager = new HighScoreManager();

        initComponents();
        updateHighScores();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Top panel with controls
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        difficultyBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        testModeBox = new JComboBox<>(TestResult.TestMode.values());
        testModeBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TestResult.TestMode) {
                    setText(((TestResult.TestMode) value).getDisplayName());
                }
                return this;
            }
        });
        
        startButton = new JButton("Start Test");
        pauseButton = new JButton("Pause");
        pauseButton.setEnabled(false);
        
        topPanel.add(new JLabel("Difficulty:"));
        topPanel.add(difficultyBox);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(new JLabel("Test Mode:"));
        topPanel.add(testModeBox);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(startButton);
        topPanel.add(pauseButton);

        // Main content area
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Text display area
        sentencePane = new JTextPane();
        sentencePane.setEditable(false);
        sentencePane.setFont(new Font("Consolas", Font.PLAIN, 18));
        sentencePane.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane sentenceScroll = new JScrollPane(sentencePane);
        sentenceScroll.setPreferredSize(new Dimension(0, 200));

        // Input field
        inputField = new JTextField();
        inputField.setFont(new Font("Consolas", Font.PLAIN, 18));
        inputField.setEnabled(false);
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Type here:"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        mainPanel.add(sentenceScroll, BorderLayout.CENTER);
        mainPanel.add(inputField, BorderLayout.SOUTH);

        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));
        
        timerLabel = new JLabel("Time: 0.0s");
        wpmLabel = new JLabel("WPM: 0");
        accuracyLabel = new JLabel("Accuracy: 100%");
        errorLabel = new JLabel("Errors: 0");
        progressLabel = new JLabel("Progress: 0%");
        
        timerLabel.setFont(timerLabel.getFont().deriveFont(Font.BOLD));
        wpmLabel.setFont(wpmLabel.getFont().deriveFont(Font.BOLD));
        
        statsPanel.add(timerLabel);
        statsPanel.add(wpmLabel);
        statsPanel.add(accuracyLabel);
        statsPanel.add(errorLabel);
        statsPanel.add(progressLabel);
        
        // Progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Ready to start");
        
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBorder(BorderFactory.createTitledBorder("Progress"));
        progressPanel.add(progressBar, BorderLayout.CENTER);
        
        // High scores panel
        highScorePanel = new HighScorePanel();
        
        // Right panel combining stats and high scores
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(250, 0));
        rightPanel.add(statsPanel, BorderLayout.NORTH);
        rightPanel.add(progressPanel, BorderLayout.CENTER);
        rightPanel.add(highScorePanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // Event listeners
        startButton.addActionListener(e -> startTest());
        pauseButton.addActionListener(e -> togglePause());
        
        inputField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (!running || paused) return;

                // Start timer on first key press
                if (!timerStarted) {
                    startTime = System.currentTimeMillis();
                    timer = new Timer(50, evt -> updateTimer());
                    timer.start();
                    timerStarted = true;
                }

                updateSentenceHighlight();
                updateStats();
                
                // Check completion conditions
                if (currentTestMode == TestResult.TestMode.COMPLETION && inputField.getText().equals(sentence)) {
                    endTest();
                } else if (testTimeLimit > 0) {
                    double elapsed = (System.currentTimeMillis() - startTime - pausedTime) / 1000.0;
                    if (elapsed >= testTimeLimit) {
                        endTest();
                    }
                }
            }
        });
    }

    private void startTest() {
        currentTestMode = (TestResult.TestMode) testModeBox.getSelectedItem();
        testTimeLimit = currentTestMode.getTimeLimit();
        
        String difficulty = (String) difficultyBox.getSelectedItem();
        
        // For time-based tests, get multiple sentences
        if (testTimeLimit > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 10; i++) { // Get enough sentences for time-based test
                if (i > 0) sb.append(" ");
                sb.append(sentenceProvider.getRandomSentence(difficulty));
            }
            sentence = sb.toString();
        } else {
            sentence = sentenceProvider.getRandomSentence(difficulty);
        }
        
        sentencePane.setText(sentence);
        inputField.setText("");
        inputField.setEnabled(true);
        inputField.requestFocusInWindow();

        resetStats();
        
        running = true;
        timerStarted = false;
        paused = false;
        pausedTime = 0;
        
        startButton.setEnabled(false);
        pauseButton.setEnabled(true);
        difficultyBox.setEnabled(false);
        testModeBox.setEnabled(false);
        
        if (timer != null) timer.stop();
        
        progressBar.setString("Test in progress...");
        progressBar.setValue(0);
    }

    private void togglePause() {
        if (!running) return;
        
        if (paused) {
            // Resume
            paused = false;
            pausedTime += System.currentTimeMillis() - pauseTime;
            pauseButton.setText("Pause");
            inputField.setEnabled(true);
            inputField.requestFocusInWindow();
            if (timer != null) timer.start();
        } else {
            // Pause
            paused = true;
            pauseTime = System.currentTimeMillis();
            pauseButton.setText("Resume");
            inputField.setEnabled(false);
            if (timer != null) timer.stop();
        }
    }
    
    private long pauseTime = 0;

    private void endTest() {
        if (!running) return;
        
        // Calculate final stats
        double elapsedTime = (System.currentTimeMillis() - startTime - pausedTime) / 1000.0;
        int wpm = calcWPM();
        int accuracy = calcAccuracy();
        int errors = calcErrors();
        String difficulty = (String) difficultyBox.getSelectedItem();
        
        TestResult result = new TestResult(
            wpm, accuracy, errors, elapsedTime, difficulty, currentTestMode,
            inputField.getText().length(), inputField.getText().trim().split("\\s+").length
        );

        running = false;
        timerStarted = false;
        paused = false;
        if (timer != null) timer.stop();
        
        inputField.setEnabled(false);
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        pauseButton.setText("Pause");
        difficultyBox.setEnabled(true);
        testModeBox.setEnabled(true);
        
        progressBar.setValue(100);
        progressBar.setString("Test completed!");
        
        // Record high score for completion tests or significant WPM
        if (currentTestMode == TestResult.TestMode.COMPLETION || wpm >= 20) {
            highScoreManager.recordScore(difficulty, wpm);
            updateHighScores();
        }
        
        // Show results
        ResultsDialog.showResults(this, result);
    }

    private void resetStats() {
        timerLabel.setText("Time: 0.0s");
        wpmLabel.setText("WPM: 0");
        accuracyLabel.setText("Accuracy: 100%");
        errorLabel.setText("Errors: 0");
        progressLabel.setText("Progress: 0%");
    }
    
    private void updateTimer() {
        if (!running || paused || !timerStarted) return;
        
        double elapsed = (System.currentTimeMillis() - startTime - pausedTime) / 1000.0;
        
        if (testTimeLimit > 0) {
            double remaining = testTimeLimit - elapsed;
            if (remaining <= 0) {
                endTest();
                return;
            }
            timerLabel.setText(String.format("Time: %.1fs left", remaining));
            
            // Update progress for time-based tests
            int progress = (int) ((elapsed / testTimeLimit) * 100);
            progressBar.setValue(Math.min(progress, 100));
        } else {
            timerLabel.setText(String.format("Time: %.1fs", elapsed));
            
            // Update progress for completion tests
            if (sentence.length() > 0) {
                int progress = (inputField.getText().length() * 100) / sentence.length();
                progressBar.setValue(Math.min(progress, 100));
            }
        }
    }
    
    private void updateHighScores() {
        highScorePanel.refresh(highScoreManager);
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
        int wpm = calcWPM();
        int accuracy = calcAccuracy();
        int errors = calcErrors();
        
        wpmLabel.setText("WPM: " + wpm);
        accuracyLabel.setText("Accuracy: " + accuracy + "%");
        errorLabel.setText("Errors: " + errors);
        
        // Update progress for completion tests
        if (currentTestMode == TestResult.TestMode.COMPLETION && sentence.length() > 0) {
            int progress = (inputField.getText().length() * 100) / sentence.length();
            progressLabel.setText("Progress: " + Math.min(progress, 100) + "%");
        } else if (testTimeLimit > 0) {
            // For time-based tests, show words typed
            int wordsTyped = inputField.getText().trim().isEmpty() ? 0 : inputField.getText().trim().split("\\s+").length;
            progressLabel.setText("Words: " + wordsTyped);
        }
    }

    private int calcWPM() {
        if (!timerStarted) return 0;
        double elapsedMin = Math.max((System.currentTimeMillis() - startTime - pausedTime) / 60000.0, 1.0/60);
        String text = inputField.getText().trim();
        int wordCount = text.isEmpty() ? 0 : text.split("\\s+").length;
        return (int) Math.round(wordCount / elapsedMin);
    }

    private int calcAccuracy() {
        String input = inputField.getText();
        if (input.isEmpty()) return 100;
        
        int correct = 0;
        int len = Math.min(input.length(), sentence.length());
        for (int i = 0; i < len; i++) {
            if (input.charAt(i) == sentence.charAt(i)) correct++;
        }
        return (int) (100.0 * correct / len);
    }

    private int calcErrors() {
        String input = inputField.getText();
        int errors = 0;
        int len = Math.min(input.length(), sentence.length());
        for (int i = 0; i < len; i++) {
            if (input.charAt(i) != sentence.charAt(i)) errors++;
        }
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